package com.nexus.ai.controller;

import com.nexus.ai.dto.*;
import com.nexus.ai.service.AnomalyDetectionService;
import com.nexus.ai.service.PredictionService;
import com.nexus.ai.service.RecommendationService;
import io.micrometer.tracing.Tracer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * AI Service REST Controller
 * Endpoints for recommendations, anomaly detection, and predictive analytics
 */
@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Slf4j
public class AiController {

    private final RecommendationService recommendationService;
    private final AnomalyDetectionService anomalyDetectionService;
    private final PredictionService predictionService;
    private final Tracer tracer;

    /**
     * GET /api/v1/ai/recommendations/tasks?userId={id}&limit=10
     * Generate task recommendations for a user
     */
    @GetMapping("/recommendations/tasks")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getTaskRecommendations(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        log.info("Received request for task recommendations: userId={}, limit={}", userId, limit);
        
        RecommendationResponse recommendations = recommendationService.generateRecommendations(userId, limit);
        
        return ResponseEntity.ok(ApiResponse.<RecommendationResponse>builder()
                .success(true)
                .message("Task recommendations generated successfully")
                .data(recommendations)
                .traceId(getTraceId())
                .build());
    }

    /**
     * POST /api/v1/ai/anomaly/detect
     * Detect anomalies in service metrics
     */
    @PostMapping("/anomaly/detect")
    public ResponseEntity<ApiResponse<AnomalyDetectionResponse>> detectAnomalies(
            @Valid @RequestBody AnomalyDetectionRequest request) {
        
        log.info("Received anomaly detection request for service: {}", request.getService());
        
        LocalDateTime timestamp = request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now();
        
        AnomalyDetectionResponse response = anomalyDetectionService.detectAnomalies(
                request.getService(),
                request.getMetrics(),
                timestamp
        );
        
        return ResponseEntity.ok(ApiResponse.<AnomalyDetectionResponse>builder()
                .success(true)
                .message("Anomaly detection completed")
                .data(response)
                .traceId(getTraceId())
                .build());
    }

    /**
     * POST /api/v1/ai/predict/completion
     * Predict task completion time
     */
    @PostMapping("/predict/completion")
    public ResponseEntity<ApiResponse<PredictionResponse>> predictTaskCompletion(
            @Valid @RequestBody PredictionRequest request) {
        
        log.info("Received prediction request for task: {}", request.getTaskId());
        
        PredictionResponse prediction = predictionService.predictCompletionTime(request);
        
        return ResponseEntity.ok(ApiResponse.<PredictionResponse>builder()
                .success(true)
                .message("Task completion prediction generated")
                .data(prediction)
                .traceId(getTraceId())
                .build());
    }

    /**
     * GET /api/v1/ai/health
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("AI Service is healthy")
                .data("OK")
                .traceId(getTraceId())
                .build());
    }

    /**
     * Get current trace ID
     */
    private String getTraceId() {
        return tracer != null && tracer.currentSpan() != null 
                ? tracer.currentSpan().context().traceId() 
                : UUID.randomUUID().toString();
    }

    /**
     * Global exception handler
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error("Error processing request", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message("Error: " + ex.getMessage())
                        .traceId(getTraceId())
                        .build());
    }
}

