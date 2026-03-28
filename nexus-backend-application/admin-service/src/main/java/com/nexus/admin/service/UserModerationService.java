package com.nexus.admin.service;

import com.nexus.admin.domain.UserModeration;
import com.nexus.admin.domain.UserModeration.ModerationStatus;
import com.nexus.admin.domain.UserModeration.ViolationType;
import com.nexus.admin.dto.UserModerationRequest;
import com.nexus.admin.repository.UserModerationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User Moderation Service
 * Manages user violations, suspensions, and bans
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserModerationService {
    
    private final UserModerationRepository moderationRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Transactional
    public UserModeration moderateUser(UserModerationRequest request) {
        log.info("Moderating user: {}", request.getUserId());
        
        UserModeration moderation = moderationRepository.findByUserId(request.getUserId())
            .orElseGet(() -> createNewModeration(request));
        
        moderation.setStatus(request.getStatus());
        moderation.setViolationType(request.getViolationType());
        moderation.setReason(request.getReason());
        moderation.setViolationCount(moderation.getViolationCount() + 1);
        moderation.setModeratorId(request.getModeratorId());
        moderation.setModeratorName(request.getModeratorName());
        
        // Handle suspension
        if (request.getStatus() == ModerationStatus.SUSPENDED) {
            moderation.setSuspensionStartDate(LocalDateTime.now());
            moderation.setSuspensionEndDate(
                LocalDateTime.now().plusDays(request.getSuspensionDays() != null ? request.getSuspensionDays() : 30)
            );
        }
        
        UserModeration saved = moderationRepository.save(moderation);
        
        // Publish event
        publishModerationEvent(saved);
        
        return saved;
    }
    
    @Transactional
    public UserModeration suspendUser(Long userId, String reason, Integer days, Long moderatorId) {
        log.info("Suspending user {} for {} days", userId, days);
        
        UserModeration moderation = moderationRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("User moderation record not found"));
        
        moderation.setStatus(ModerationStatus.SUSPENDED);
        moderation.setReason(reason);
        moderation.setSuspensionStartDate(LocalDateTime.now());
        moderation.setSuspensionEndDate(LocalDateTime.now().plusDays(days));
        moderation.setModeratorId(moderatorId);
        
        UserModeration saved = moderationRepository.save(moderation);
        publishModerationEvent(saved);
        
        return saved;
    }
    
    @Transactional
    public UserModeration banUser(Long userId, String reason, Long moderatorId) {
        log.info("Permanently banning user: {}", userId);
        
        UserModeration moderation = moderationRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("User moderation record not found"));
        
        moderation.setStatus(ModerationStatus.BANNED);
        moderation.setReason(reason);
        moderation.setModeratorId(moderatorId);
        
        UserModeration saved = moderationRepository.save(moderation);
        publishModerationEvent(saved);
        
        return saved;
    }
    
    @Transactional
    public UserModeration reinstateUser(Long userId, Long moderatorId) {
        log.info("Reinstating user: {}", userId);
        
        UserModeration moderation = moderationRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("User moderation record not found"));
        
        moderation.setStatus(ModerationStatus.ACTIVE);
        moderation.setSuspensionStartDate(null);
        moderation.setSuspensionEndDate(null);
        moderation.setModeratorId(moderatorId);
        
        UserModeration saved = moderationRepository.save(moderation);
        publishModerationEvent(saved);
        
        return saved;
    }
    
    public List<UserModeration> getModerationsByStatus(ModerationStatus status) {
        return moderationRepository.findByStatus(status);
    }
    
    public UserModeration getUserModeration(Long userId) {
        return moderationRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("User moderation record not found"));
    }
    
    public List<UserModeration> getHighViolationUsers(Integer threshold) {
        return moderationRepository.findUsersWithHighViolations(threshold);
    }
    
    private UserModeration createNewModeration(UserModerationRequest request) {
        return UserModeration.builder()
            .userId(request.getUserId())
            .username(request.getUsername())
            .status(ModerationStatus.ACTIVE)
            .violationCount(0)
            .build();
    }
    
    private void publishModerationEvent(UserModeration moderation) {
        try {
            kafkaTemplate.send("user.moderation", moderation.getUserId().toString(), moderation);
            log.info("Published moderation event for user: {}", moderation.getUserId());
        } catch (Exception e) {
            log.error("Failed to publish moderation event: {}", e.getMessage());
        }
    }
}

