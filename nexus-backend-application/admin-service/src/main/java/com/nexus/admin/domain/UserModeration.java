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
 * User Moderation Entity
 * Tracks user violations, suspensions, and bans
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_moderation", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
public class UserModeration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 100)
    private String username;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ModerationStatus status; // ACTIVE, WARNING, SUSPENDED, BANNED
    
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ViolationType violationType; // SPAM, FRAUD, ABUSIVE_CONTENT, POLICY_VIOLATION
    
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    @Column(nullable = false)
    private Integer violationCount = 0;
    
    private LocalDateTime suspensionStartDate;
    private LocalDateTime suspensionEndDate;
    
    @Column(nullable = false)
    private Long moderatorId;
    
    @Column(length = 100)
    private String moderatorName;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    public enum ModerationStatus {
        ACTIVE,
        WARNING,
        SUSPENDED,
        BANNED
    }
    
    public enum ViolationType {
        SPAM,
        FRAUD,
        ABUSIVE_CONTENT,
        POLICY_VIOLATION,
        FAKE_REVIEWS,
        HARASSMENT,
        OTHER
    }
}

