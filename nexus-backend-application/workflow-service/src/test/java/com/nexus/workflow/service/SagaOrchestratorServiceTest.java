package com.nexus.workflow.service;

import com.nexus.workflow.domain.entity.SagaInstance;
import com.nexus.workflow.domain.repository.SagaInstanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SagaOrchestratorService
 */
@ExtendWith(MockitoExtension.class)
class SagaOrchestratorServiceTest {

    @Mock
    private SagaInstanceRepository sagaInstanceRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private SagaOrchestratorService sagaOrchestratorService;

    private SagaInstance sampleSaga;

    @BeforeEach
    void setUp() {
        sampleSaga = SagaInstance.builder()
                .id(UUID.randomUUID())
                .sagaType("USER_REGISTRATION")
                .correlationId("corr-123")
                .status(SagaInstance.SagaStatus.STARTED)
                .currentStep("STEP_1")
                .payload("{\"userId\":\"user-123\"}")
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Start saga - Success")
    void startSaga_success() {
        // Arrange
        when(sagaInstanceRepository.save(any(SagaInstance.class))).thenReturn(sampleSaga);

        // Act
        SagaInstance result = sagaOrchestratorService.startSaga(
                "USER_REGISTRATION",
                "corr-123",
                "{\"userId\":\"user-123\"}"
        );

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getSagaType()).isEqualTo("USER_REGISTRATION");
        assertThat(result.getCorrelationId()).isEqualTo("corr-123");
        assertThat(result.getStatus()).isEqualTo(SagaInstance.SagaStatus.STARTED);
        assertThat(result.getCurrentStep()).isEqualTo("STEP_1");

        verify(sagaInstanceRepository).save(any(SagaInstance.class));
        verify(kafkaTemplate).send(eq("saga.user_registration.step_1"), eq("corr-123"), any(SagaInstance.class));
    }

    @Test
    @DisplayName("Advance saga to next step - Success")
    void advanceSaga_success() {
        // Arrange
        when(sagaInstanceRepository.findByCorrelationId("corr-123")).thenReturn(Optional.of(sampleSaga));
        when(sagaInstanceRepository.save(any(SagaInstance.class))).thenReturn(sampleSaga);

        // Act
        SagaInstance result = sagaOrchestratorService.advanceSaga(
                "corr-123",
                "STEP_1",
                SagaInstance.SagaStatus.STEP_1_COMPLETE
        );

        // Assert
        assertThat(result).isNotNull();
        verify(sagaInstanceRepository).findByCorrelationId("corr-123");
        verify(sagaInstanceRepository).save(any(SagaInstance.class));
        verify(kafkaTemplate).send(anyString(), eq("corr-123"), any(SagaInstance.class));
    }

    @Test
    @DisplayName("Advance saga to completed - Success")
    void advanceSaga_toCompleted_success() {
        // Arrange
        when(sagaInstanceRepository.findByCorrelationId("corr-123")).thenReturn(Optional.of(sampleSaga));
        when(sagaInstanceRepository.save(any(SagaInstance.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        SagaInstance result = sagaOrchestratorService.advanceSaga(
                "corr-123",
                "STEP_3",
                SagaInstance.SagaStatus.COMPLETED
        );

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(SagaInstance.SagaStatus.COMPLETED);
        assertThat(result.getCompletedAt()).isNotNull();
        verify(sagaInstanceRepository).save(any(SagaInstance.class));
        verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("Advance saga - Saga not found throws exception")
    void advanceSaga_sagaNotFound_throwsException() {
        // Arrange
        when(sagaInstanceRepository.findByCorrelationId("invalid")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> sagaOrchestratorService.advanceSaga(
                "invalid",
                "STEP_1",
                SagaInstance.SagaStatus.STEP_1_COMPLETE
        ))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Saga not found");

        verify(sagaInstanceRepository).findByCorrelationId("invalid");
        verify(sagaInstanceRepository, never()).save(any());
    }

    @Test
    @DisplayName("Compensate saga - Success")
    void compensateSaga_success() {
        // Arrange
        when(sagaInstanceRepository.findByCorrelationId("corr-123")).thenReturn(Optional.of(sampleSaga));
        when(sagaInstanceRepository.save(any(SagaInstance.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        SagaInstance result = sagaOrchestratorService.compensateSaga("corr-123", "Payment failed");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(SagaInstance.SagaStatus.COMPENSATING);
        assertThat(result.getErrorMessage()).isEqualTo("Payment failed");
        verify(kafkaTemplate).send(eq("saga.user_registration.compensate"), eq("corr-123"), any(SagaInstance.class));
    }

    @Test
    @DisplayName("Mark saga as compensated - Success")
    void markCompensated_success() {
        // Arrange
        when(sagaInstanceRepository.findByCorrelationId("corr-123")).thenReturn(Optional.of(sampleSaga));
        when(sagaInstanceRepository.save(any(SagaInstance.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        SagaInstance result = sagaOrchestratorService.markCompensated("corr-123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(SagaInstance.SagaStatus.COMPENSATED);
        verify(sagaInstanceRepository).save(any(SagaInstance.class));
    }

    @Test
    @DisplayName("Get saga by correlation ID - Success")
    void getSaga_success() {
        // Arrange
        when(sagaInstanceRepository.findByCorrelationId("corr-123")).thenReturn(Optional.of(sampleSaga));

        // Act
        SagaInstance result = sagaOrchestratorService.getSaga("corr-123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCorrelationId()).isEqualTo("corr-123");
        verify(sagaInstanceRepository).findByCorrelationId("corr-123");
    }

    @Test
    @DisplayName("Get saga - Not found throws exception")
    void getSaga_notFound_throwsException() {
        // Arrange
        when(sagaInstanceRepository.findByCorrelationId("invalid")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> sagaOrchestratorService.getSaga("invalid"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Saga not found");
    }

    @Test
    @DisplayName("Get all sagas - Success")
    void getAllSagas_success() {
        // Arrange
        SagaInstance saga2 = SagaInstance.builder()
                .id(UUID.randomUUID())
                .sagaType("TASK_ASSIGNMENT")
                .correlationId("corr-456")
                .status(SagaInstance.SagaStatus.COMPLETED)
                .currentStep("COMPLETED")
                .retryCount(0)
                .build();

        when(sagaInstanceRepository.findAll()).thenReturn(Arrays.asList(sampleSaga, saga2));

        // Act
        List<SagaInstance> result = sagaOrchestratorService.getAllSagas();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains(sampleSaga, saga2);
        verify(sagaInstanceRepository).findAll();
    }

    @Test
    @DisplayName("Compensate saga - Saga not found throws exception")
    void compensateSaga_sagaNotFound_throwsException() {
        // Arrange
        when(sagaInstanceRepository.findByCorrelationId("invalid")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> sagaOrchestratorService.compensateSaga("invalid", "Error"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Saga not found");
    }

    @Test
    @DisplayName("Mark compensated - Saga not found throws exception")
    void markCompensated_sagaNotFound_throwsException() {
        // Arrange
        when(sagaInstanceRepository.findByCorrelationId("invalid")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> sagaOrchestratorService.markCompensated("invalid"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Saga not found");
    }

    @Test
    @DisplayName("Advance saga from STEP_2 to STEP_3")
    void advanceSaga_fromStep2ToStep3() {
        // Arrange
        when(sagaInstanceRepository.findByCorrelationId("corr-123")).thenReturn(Optional.of(sampleSaga));
        when(sagaInstanceRepository.save(any(SagaInstance.class))).thenReturn(sampleSaga);

        // Act
        sagaOrchestratorService.advanceSaga(
                "corr-123",
                "STEP_2",
                SagaInstance.SagaStatus.STEP_2_COMPLETE
        );

        // Assert
        verify(sagaInstanceRepository).save(any(SagaInstance.class));
        verify(kafkaTemplate).send(anyString(), anyString(), any(SagaInstance.class));
    }

    @Test
    @DisplayName("Advance saga from STEP_3 to COMPLETED")
    void advanceSaga_fromStep3ToCompleted() {
        // Arrange
        when(sagaInstanceRepository.findByCorrelationId("corr-123")).thenReturn(Optional.of(sampleSaga));
        when(sagaInstanceRepository.save(any(SagaInstance.class))).thenReturn(sampleSaga);

        // Act
        sagaOrchestratorService.advanceSaga(
                "corr-123",
                "STEP_3",
                SagaInstance.SagaStatus.STEP_3_COMPLETE
        );

        // Assert
        verify(sagaInstanceRepository).save(any(SagaInstance.class));
    }

    @Test
    @DisplayName("Advance saga with unknown step defaults to COMPLETED")
    void advanceSaga_unknownStepDefaultsToCompleted() {
        // Arrange
        when(sagaInstanceRepository.findByCorrelationId("corr-123")).thenReturn(Optional.of(sampleSaga));
        when(sagaInstanceRepository.save(any(SagaInstance.class))).thenReturn(sampleSaga);

        // Act
        sagaOrchestratorService.advanceSaga(
                "corr-123",
                "UNKNOWN_STEP",
                SagaInstance.SagaStatus.STARTED
        );

        // Assert
        verify(sagaInstanceRepository).save(any(SagaInstance.class));
    }
}

