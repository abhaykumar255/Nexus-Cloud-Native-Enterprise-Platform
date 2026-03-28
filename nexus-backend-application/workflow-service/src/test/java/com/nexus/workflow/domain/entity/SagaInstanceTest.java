package com.nexus.workflow.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for SagaInstance entity
 */
class SagaInstanceTest {

    @Test
    @DisplayName("Builder creates SagaInstance with all fields")
    void builder_createsInstanceWithAllFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        // Act
        SagaInstance saga = SagaInstance.builder()
                .id(id)
                .sagaType("USER_REGISTRATION")
                .correlationId("corr-123")
                .status(SagaInstance.SagaStatus.STARTED)
                .currentStep("STEP_1")
                .payload("{\"userId\":\"user-123\"}")
                .compensationData("{\"rollback\":\"data\"}")
                .errorMessage("Test error")
                .retryCount(3)
                .createdAt(now)
                .updatedAt(now)
                .completedAt(now)
                .build();

        // Assert
        assertThat(saga.getId()).isEqualTo(id);
        assertThat(saga.getSagaType()).isEqualTo("USER_REGISTRATION");
        assertThat(saga.getCorrelationId()).isEqualTo("corr-123");
        assertThat(saga.getStatus()).isEqualTo(SagaInstance.SagaStatus.STARTED);
        assertThat(saga.getCurrentStep()).isEqualTo("STEP_1");
        assertThat(saga.getPayload()).isEqualTo("{\"userId\":\"user-123\"}");
        assertThat(saga.getCompensationData()).isEqualTo("{\"rollback\":\"data\"}");
        assertThat(saga.getErrorMessage()).isEqualTo("Test error");
        assertThat(saga.getRetryCount()).isEqualTo(3);
        assertThat(saga.getCreatedAt()).isEqualTo(now);
        assertThat(saga.getUpdatedAt()).isEqualTo(now);
        assertThat(saga.getCompletedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("NoArgsConstructor creates empty instance")
    void noArgsConstructor_createsEmptyInstance() {
        // Act
        SagaInstance saga = new SagaInstance();

        // Assert
        assertThat(saga).isNotNull();
        assertThat(saga.getId()).isNull();
        assertThat(saga.getSagaType()).isNull();
    }

    @Test
    @DisplayName("AllArgsConstructor creates instance with all parameters")
    void allArgsConstructor_createsInstanceWithAllParameters() {
        // Arrange
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        // Act
        SagaInstance saga = new SagaInstance(
                id,
                "TASK_ASSIGNMENT",
                "corr-456",
                SagaInstance.SagaStatus.COMPLETED,
                "COMPLETED",
                "{\"taskId\":\"task-789\"}",
                null,
                null,
                0,
                now,
                now,
                now
        );

        // Assert
        assertThat(saga.getId()).isEqualTo(id);
        assertThat(saga.getSagaType()).isEqualTo("TASK_ASSIGNMENT");
        assertThat(saga.getStatus()).isEqualTo(SagaInstance.SagaStatus.COMPLETED);
    }

    @Test
    @DisplayName("Setters modify instance fields")
    void setters_modifyFields() {
        // Arrange
        SagaInstance saga = new SagaInstance();
        LocalDateTime now = LocalDateTime.now();

        // Act
        saga.setId(UUID.randomUUID());
        saga.setSagaType("ORDER_PROCESSING");
        saga.setCorrelationId("corr-789");
        saga.setStatus(SagaInstance.SagaStatus.COMPENSATING);
        saga.setCurrentStep("STEP_2");
        saga.setPayload("{\"orderId\":\"order-123\"}");
        saga.setCompensationData("{\"refund\":\"data\"}");
        saga.setErrorMessage("Payment failed");
        saga.setRetryCount(5);
        saga.setCreatedAt(now);
        saga.setUpdatedAt(now);
        saga.setCompletedAt(now);

        // Assert
        assertThat(saga.getSagaType()).isEqualTo("ORDER_PROCESSING");
        assertThat(saga.getStatus()).isEqualTo(SagaInstance.SagaStatus.COMPENSATING);
        assertThat(saga.getCurrentStep()).isEqualTo("STEP_2");
        assertThat(saga.getErrorMessage()).isEqualTo("Payment failed");
        assertThat(saga.getRetryCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("SagaStatus enum has all expected values")
    void sagaStatus_hasAllExpectedValues() {
        // Assert
        assertThat(SagaInstance.SagaStatus.values()).containsExactlyInAnyOrder(
                SagaInstance.SagaStatus.STARTED,
                SagaInstance.SagaStatus.STEP_1_COMPLETE,
                SagaInstance.SagaStatus.STEP_2_COMPLETE,
                SagaInstance.SagaStatus.STEP_3_COMPLETE,
                SagaInstance.SagaStatus.COMPLETED,
                SagaInstance.SagaStatus.COMPENSATING,
                SagaInstance.SagaStatus.COMPENSATED,
                SagaInstance.SagaStatus.FAILED
        );
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        // Arrange
        UUID id = UUID.randomUUID();
        SagaInstance saga1 = SagaInstance.builder()
                .id(id)
                .sagaType("TEST")
                .correlationId("corr-1")
                .status(SagaInstance.SagaStatus.STARTED)
                .currentStep("STEP_1")
                .retryCount(0)
                .build();

        SagaInstance saga2 = SagaInstance.builder()
                .id(id)
                .sagaType("TEST")
                .correlationId("corr-1")
                .status(SagaInstance.SagaStatus.STARTED)
                .currentStep("STEP_1")
                .retryCount(0)
                .build();

        // Assert
        assertThat(saga1).isEqualTo(saga2);
        assertThat(saga1.hashCode()).isEqualTo(saga2.hashCode());
    }
}

