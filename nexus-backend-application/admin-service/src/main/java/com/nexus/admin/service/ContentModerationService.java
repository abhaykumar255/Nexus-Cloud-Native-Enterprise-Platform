package com.nexus.admin.service;

import com.nexus.admin.domain.ContentModeration;
import com.nexus.admin.domain.ContentModeration.*;
import com.nexus.admin.dto.ContentModerationRequest;
import com.nexus.admin.repository.ContentModerationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Content Moderation Service
 * Handles content reports and moderation actions
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContentModerationService {
    
    private final ContentModerationRepository moderationRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Transactional
    public ContentModeration reportContent(ContentModerationRequest request) {
        log.info("Reporting {} with ID: {}", request.getEntityType(), request.getEntityId());
        
        // Check if already reported
        var existing = moderationRepository.findByEntityTypeAndEntityIdAndStatus(
            request.getEntityType(), request.getEntityId(), ModerationStatus.PENDING
        );
        
        if (existing.isPresent()) {
            ContentModeration moderation = existing.get();
            moderation.setReportCount(moderation.getReportCount() + 1);
            return moderationRepository.save(moderation);
        }
        
        ContentModeration moderation = ContentModeration.builder()
            .entityType(request.getEntityType())
            .entityId(request.getEntityId())
            .reportedByUserId(request.getReportedByUserId())
            .reason(request.getReason())
            .description(request.getDescription())
            .status(ModerationStatus.PENDING)
            .reportCount(1)
            .build();
        
        ContentModeration saved = moderationRepository.save(moderation);
        publishModerationEvent(saved, "reported");
        
        return saved;
    }
    
    @Transactional
    public ContentModeration reviewContent(Long moderationId, ModerationAction action, 
                                          Long moderatorId, String moderatorName, String notes) {
        log.info("Reviewing content moderation ID: {} with action: {}", moderationId, action);
        
        ContentModeration moderation = moderationRepository.findById(moderationId)
            .orElseThrow(() -> new RuntimeException("Content moderation not found"));
        
        moderation.setStatus(ModerationStatus.UNDER_REVIEW);
        moderation.setModeratorId(moderatorId);
        moderation.setModeratorName(moderatorName);
        moderation.setModerationNotes(notes);
        moderation.setReviewedAt(LocalDateTime.now());
        moderation.setActionTaken(action);
        
        // Update final status based on action
        switch (action) {
            case REMOVED:
                moderation.setStatus(ModerationStatus.REMOVED);
                break;
            case DISMISSED:
                moderation.setStatus(ModerationStatus.DISMISSED);
                break;
            default:
                moderation.setStatus(ModerationStatus.APPROVED);
        }
        
        ContentModeration saved = moderationRepository.save(moderation);
        publishModerationEvent(saved, action.name().toLowerCase());
        
        return saved;
    }
    
    @Transactional
    public ContentModeration removeContent(Long moderationId, Long moderatorId, String moderatorName) {
        return reviewContent(moderationId, ModerationAction.REMOVED, moderatorId, moderatorName, 
            "Content removed due to policy violation");
    }
    
    @Transactional
    public ContentModeration dismissReport(Long moderationId, Long moderatorId, String moderatorName, String reason) {
        return reviewContent(moderationId, ModerationAction.DISMISSED, moderatorId, moderatorName, reason);
    }
    
    public List<ContentModeration> getPendingReports() {
        return moderationRepository.findByStatus(ModerationStatus.PENDING);
    }
    
    public List<ContentModeration> getHighPriorityReports(Integer threshold) {
        return moderationRepository.findHighPriorityReports(threshold);
    }
    
    public List<ContentModeration> getReportsByEntityType(EntityType entityType, Long entityId) {
        return moderationRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }
    
    public List<ContentModeration> getReportsByUser(Long userId) {
        return moderationRepository.findByReportedByUserId(userId);
    }
    
    private void publishModerationEvent(ContentModeration moderation, String action) {
        try {
            kafkaTemplate.send("content.moderation." + action, 
                moderation.getEntityId().toString(), moderation);
            log.info("Published {} event for {} ID: {}", 
                action, moderation.getEntityType(), moderation.getEntityId());
        } catch (Exception e) {
            log.error("Failed to publish moderation event: {}", e.getMessage());
        }
    }
}

