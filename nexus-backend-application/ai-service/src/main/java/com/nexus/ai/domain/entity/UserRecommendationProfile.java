package com.nexus.ai.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PostgreSQL entity storing user recommendation profiles
 * Used for collaborative filtering and personalized task recommendations
 */
@Entity
@Table(name = "user_recommendation_profiles", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_updated_at", columnList = "updated_at")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRecommendationProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills; // JSON array of skills

    @Column(name = "preferred_tags", columnDefinition = "TEXT")
    private String preferredTags; // JSON array of preferred tags

    @Column(name = "task_completion_rate")
    private Double taskCompletionRate;

    @Column(name = "avg_task_duration_days")
    private Double avgTaskDurationDays;

    @Column(name = "total_tasks_completed")
    private Integer totalTasksCompleted;

    @Column(name = "model_version")
    private String modelVersion; // e.g., "1.3.0"

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

