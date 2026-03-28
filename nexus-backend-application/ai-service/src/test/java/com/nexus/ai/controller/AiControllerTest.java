package com.nexus.ai.controller;

import com.nexus.ai.dto.*;
import com.nexus.ai.service.AnomalyDetectionService;
import com.nexus.ai.service.PredictionService;
import com.nexus.ai.service.RecommendationService;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.TraceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DisplayName("AiController Tests")
class AiControllerTest {

    @Mock
    private RecommendationService recommendationService;

    @Mock
    private AnomalyDetectionService anomalyDetectionService;

    @Mock
    private PredictionService predictionService;

    @Mock
    private Tracer tracer;

    @InjectMocks
    private AiController aiController;

    @BeforeEach
    void setUp() {
        var span = mock(io.micrometer.tracing.Span.class);
        var context = mock(TraceContext.class);
        lenient().when(tracer.currentSpan()).thenReturn(span);
        lenient().when(span.context()).thenReturn(context);
        lenient().when(context.traceId()).thenReturn("test-trace-id");
    }

    @Test
    @DisplayName("Get task recommendations - Success")
    void getTaskRecommendations_success() {
        // Arrange
        UUID userId = UUID.randomUUID();
        List<TaskRecommendation> recommendations = Arrays.asList(
                TaskRecommendation.builder()
                        .taskId(UUID.randomUUID())
                        .title("Task 1")
                        .confidenceScore(0.85)
                        .reason("Skills match")
                        .build()
        );
        
        RecommendationResponse recommendationResponse = RecommendationResponse.builder()
                .recommendations(recommendations)
                .modelVersion("1.3.0")
                .computedAt(LocalDateTime.now())
                .cachedUntil(LocalDateTime.now().plusMinutes(30))
                .build();

        when(recommendationService.generateRecommendations(userId, 10))
                .thenReturn(recommendationResponse);

        // Act
        ResponseEntity<ApiResponse<RecommendationResponse>> response = 
                aiController.getTaskRecommendations(userId, 10);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isTrue();
        assertThat(response.getBody().getData().getRecommendations()).hasSize(1);
        assertThat(response.getBody().getTraceId()).isEqualTo("test-trace-id");
    }

    @Test
    @DisplayName("Detect anomalies - Success")
    void detectAnomalies_success() {
        // Arrange
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("error_rate_pct", 15.0);
        
        AnomalyDetectionRequest request = AnomalyDetectionRequest.builder()
                .service("task-service")
                .metrics(metrics)
                .timestamp(LocalDateTime.now())
                .build();

        AnomalyDetectionResponse anomalyResponse = AnomalyDetectionResponse.builder()
                .isAnomaly(true)
                .severity("CRITICAL")
                .anomalies(new ArrayList<>())
                .recommendation("Investigate errors")
                .build();

        when(anomalyDetectionService.detectAnomalies(anyString(), any(), any()))
                .thenReturn(anomalyResponse);

        // Act
        ResponseEntity<ApiResponse<AnomalyDetectionResponse>> response = 
                aiController.detectAnomalies(request);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isTrue();
        assertThat(response.getBody().getData().getIsAnomaly()).isTrue();
        assertThat(response.getBody().getData().getSeverity()).isEqualTo("CRITICAL");
    }

    @Test
    @DisplayName("Predict task completion - Success")
    void predictTaskCompletion_success() {
        // Arrange
        PredictionRequest request = PredictionRequest.builder()
                .taskId(UUID.randomUUID())
                .title("Implement feature")
                .priority("HIGH")
                .complexity("MEDIUM")
                .build();

        PredictionResponse prediction = PredictionResponse.builder()
                .estimatedDays(4.5)
                .confidence(0.80)
                .basis("Based on complexity and priority")
                .riskFactors(Arrays.asList("High complexity"))
                .build();

        when(predictionService.predictCompletionTime(any()))
                .thenReturn(prediction);

        // Act
        ResponseEntity<ApiResponse<PredictionResponse>> response = 
                aiController.predictTaskCompletion(request);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isTrue();
        assertThat(response.getBody().getData().getEstimatedDays()).isEqualTo(4.5);
    }

    @Test
    @DisplayName("Health check - Success")
    void healthCheck_success() {
        // Act
        ResponseEntity<ApiResponse<String>> response = aiController.healthCheck();

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isTrue();
        assertThat(response.getBody().getData()).isEqualTo("OK");
    }

    @Test
    @DisplayName("Detect anomalies - With null timestamp")
    void detectAnomalies_withNullTimestamp() {
        // Arrange
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("error_rate_pct", 5.0);

        AnomalyDetectionRequest request = AnomalyDetectionRequest.builder()
                .service("user-service")
                .metrics(metrics)
                .timestamp(null)  // Null timestamp
                .build();

        AnomalyDetectionResponse anomalyResponse = AnomalyDetectionResponse.builder()
                .isAnomaly(false)
                .severity("LOW")
                .anomalies(new ArrayList<>())
                .build();

        when(anomalyDetectionService.detectAnomalies(anyString(), any(), any()))
                .thenReturn(anomalyResponse);

        // Act
        ResponseEntity<ApiResponse<AnomalyDetectionResponse>> response =
                aiController.detectAnomalies(request);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getData().getIsAnomaly()).isFalse();
    }

    @Test
    @DisplayName("Get recommendations - With null tracer")
    void getRecommendations_withNullTracer() {
        // Arrange
        when(tracer.currentSpan()).thenReturn(null);
        UUID userId = UUID.randomUUID();
        RecommendationResponse mockResponse = RecommendationResponse.builder()
                .recommendations(new ArrayList<>())
                .build();

        when(recommendationService.generateRecommendations(userId, 10))
                .thenReturn(mockResponse);

        // Act
        ResponseEntity<ApiResponse<RecommendationResponse>> response =
                aiController.getTaskRecommendations(userId, 10);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getTraceId()).isNotEmpty();
    }

    @Test
    @DisplayName("Exception handler - Handles exceptions")
    void exceptionHandler_handlesException() {
        // Arrange
        Exception exception = new RuntimeException("Test exception");

        // Act
        ResponseEntity<ApiResponse<Void>> response = aiController.handleException(exception);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).contains("Test exception");
    }
}

