package com.nexus.workflow.controller;

import com.nexus.workflow.domain.entity.SagaInstance;
import com.nexus.workflow.service.SagaOrchestratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for WorkflowController
 */
@ExtendWith(MockitoExtension.class)
class WorkflowControllerTest {

    @Mock
    private SagaOrchestratorService sagaOrchestratorService;

    @InjectMocks
    private WorkflowController workflowController;

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
    @DisplayName("Should start saga successfully")
    void startSaga_success() {
        // Arrange
        Map<String, String> request = new HashMap<>();
        request.put("sagaType", "USER_REGISTRATION");
        request.put("correlationId", "corr-123");
        request.put("payload", "{\"userId\":\"user-123\"}");

        when(sagaOrchestratorService.startSaga(anyString(), anyString(), anyString()))
                .thenReturn(sampleSaga);

        // Act
        ResponseEntity<?> response = workflowController.startSaga(request, "user-123");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Should get saga by correlationId successfully")
    void getSaga_success() {
        // Arrange
        when(sagaOrchestratorService.getSaga("corr-123")).thenReturn(sampleSaga);

        // Act
        ResponseEntity<?> response = workflowController.getSaga("corr-123", "user-123");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should return error when saga not found")
    void getSaga_notFound() {
        // Arrange
        when(sagaOrchestratorService.getSaga("invalid"))
                .thenThrow(new RuntimeException("Saga not found: invalid"));

        // Act & Assert
        try {
            workflowController.getSaga("invalid", "user-123");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("Saga not found");
        }
    }

    @Test
    @DisplayName("Should get all sagas successfully")
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

        when(sagaOrchestratorService.getAllSagas()).thenReturn(Arrays.asList(sampleSaga, saga2));

        // Act
        ResponseEntity<?> response = workflowController.getAllSagas("user-123");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Health check returns OK")
    void health_returnsOk() {
        // Act
        ResponseEntity<String> response = workflowController.health();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Workflow Service is running");
    }
}

