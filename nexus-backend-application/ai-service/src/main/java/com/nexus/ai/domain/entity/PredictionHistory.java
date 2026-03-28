package com.nexus.ai.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PostgreSQL entity storing prediction history for model improvement
 */
@Entity
@Table(name = "prediction_history", indexes = {
    @Index(name = "idx_task_id", columnList = "task_id"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "task_id", nullable = false)
    private UUID taskId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "predicted_days")
    private Double predictedDays;

    @Column(name = "actual_days")
    private Double actualDays;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "prediction_type") // COMPLETION_TIME, RISK_SCORE, etc.
    private String predictionType;

    @Column(name = "model_version")
    private String modelVersion;

    @Column(name = "features", columnDefinition = "TEXT")
    private String features; // JSON of input features

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

