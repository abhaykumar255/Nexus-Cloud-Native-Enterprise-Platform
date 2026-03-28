package com.nexus.admin.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Content Moderation Entity
 * Tracks flagged content (products, reviews, comments)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "content_moderation", indexes = {
    @Index(name = "idx_entity_type_id", columnList = "entity_type, entity_id"),
    @Index(name = "idx_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
public class ContentModeration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EntityType entityType; // PRODUCT, REVIEW, COMMENT, SELLER_PROFILE
    
    @Column(nullable = false)
    private Long entityId;
    
    @Column(nullable = false)
    private Long reportedByUserId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReportReason reason;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ModerationStatus status; // PENDING, UNDER_REVIEW, APPROVED, REMOVED, DISMISSED
    
    @Column(nullable = false)
    private Integer reportCount = 1;
    
    private Long moderatorId;
    
    @Column(length = 100)
    private String moderatorName;
    
    @Column(columnDefinition = "TEXT")
    private String moderationNotes;
    
    private LocalDateTime reviewedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ModerationAction actionTaken; // REMOVED, WARNING_ISSUED, USER_SUSPENDED, DISMISSED
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    public enum EntityType {
        PRODUCT,
        REVIEW,
        COMMENT,
        SELLER_PROFILE,
        USER_PROFILE,
        RESTAURANT
    }
    
    public enum ReportReason {
        SPAM,
        INAPPROPRIATE_CONTENT,
        MISLEADING_INFORMATION,
        COPYRIGHT_VIOLATION,
        FAKE_REVIEW,
        OFFENSIVE_LANGUAGE,
        SCAM,
        OTHER
    }
    
    public enum ModerationStatus {
        PENDING,
        UNDER_REVIEW,
        APPROVED,
        REMOVED,
        DISMISSED
    }
    
    public enum ModerationAction {
        REMOVED,
        WARNING_ISSUED,
        USER_SUSPENDED,
        CONTENT_EDITED,
        DISMISSED,
        ESCALATED
    }
}

