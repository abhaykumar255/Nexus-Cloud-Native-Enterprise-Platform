package com.nexus.workflow.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Saga Instance entity for orchestrating distributed transactions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "saga_instances")
public class SagaInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String sagaType; // e.g., "USER_REGISTRATION", "TASK_ASSIGNMENT"

    @Column(nullable = false)
    private String correlationId; // business transaction ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SagaStatus status;

    @Column(nullable = false)
    private String currentStep;

    @Column(columnDefinition = "TEXT")
    private String payload; // JSON payload with saga data

    @Column(columnDefinition = "TEXT")
    private String compensationData; // Data for rollback

    private String errorMessage;

    private Integer retryCount;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    public enum SagaStatus {
        STARTED,
        STEP_1_COMPLETE,
        STEP_2_COMPLETE,
        STEP_3_COMPLETE,
        COMPLETED,
        COMPENSATING,
        COMPENSATED,
        FAILED
    }
}

