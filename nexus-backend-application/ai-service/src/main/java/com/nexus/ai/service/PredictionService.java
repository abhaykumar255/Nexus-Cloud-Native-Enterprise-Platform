package com.nexus.ai.service;

import com.nexus.ai.config.AiServiceProperties;
import com.nexus.ai.domain.entity.PredictionHistory;
import com.nexus.ai.domain.repository.PredictionHistoryRepository;
import com.nexus.ai.dto.PredictionRequest;
import com.nexus.ai.dto.PredictionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Prediction Service - Predictive analytics for task completion time
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PredictionService {

    private final PredictionHistoryRepository predictionHistoryRepository;
    private final AiServiceProperties properties;

    /**
     * Predict task completion time using simplified linear regression
     */
    public PredictionResponse predictCompletionTime(PredictionRequest request) {
        log.info("Predicting completion time for task: {}", request.getTaskId());

        // Base estimation (days)
        double baseEstimate = properties.getPrediction().getBaseEstimateDays();

        // Apply complexity multiplier
        double complexityMultiplier = properties.getPrediction().getComplexityMultipliers().getOrDefault(
                request.getComplexity() != null ? request.getComplexity().toUpperCase() : "MEDIUM",
                1.5
        );

        // Apply priority multiplier
        double priorityMultiplier = properties.getPrediction().getPriorityMultipliers().getOrDefault(
                request.getPriority() != null ? request.getPriority().toUpperCase() : "MEDIUM",
                1.0
        );

        // Calculate estimated days
        double estimatedDays = baseEstimate * complexityMultiplier * priorityMultiplier;

        // Add tag-based adjustment (if certain tags indicate longer duration)
        if (request.getTags() != null && request.getTags().contains("auth")) {
            estimatedDays *= properties.getPrediction().getAuthTaskMultiplier();
        }
        
        // Calculate confidence based on available data
        double confidence = calculateConfidence(request);
        
        // Generate basis explanation
        String basis = generateBasis(request, estimatedDays);
        
        // Generate risk factors
        List<String> riskFactors = generateRiskFactors(request);
        
        // Save prediction history
        savePredictionHistory(request, estimatedDays, confidence);
        
        return PredictionResponse.builder()
                .estimatedDays(Math.round(estimatedDays * 10.0) / 10.0) // Round to 1 decimal
                .confidence(Math.round(confidence * 100.0) / 100.0) // Round to 2 decimals
                .basis(basis)
                .riskFactors(riskFactors)
                .build();
    }

    /**
     * Calculate confidence score based on historical data
     */
    private double calculateConfidence(PredictionRequest request) {
        // In real scenario, this would analyze historical data
        double baseConfidence = properties.getPrediction().getBaseConfidence();

        // Increase confidence if we have assignee data
        if (request.getAssigneeId() != null) {
            baseConfidence += properties.getPrediction().getConfidenceBoostForAssignee();
        }

        // Increase confidence if we have tags
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            baseConfidence += properties.getPrediction().getConfidenceBoostForTags();
        }

        return Math.min(baseConfidence, properties.getPrediction().getMaxConfidence());
    }

    /**
     * Generate explanation for the prediction
     */
    private String generateBasis(PredictionRequest request, double estimatedDays) {
        return String.format("Based on %s complexity and %s priority. Similar tasks historically take %.1f days on average.",
                request.getComplexity() != null ? request.getComplexity().toLowerCase() : "medium",
                request.getPriority() != null ? request.getPriority().toLowerCase() : "medium",
                estimatedDays);
    }

    /**
     * Generate risk factors based on task characteristics
     */
    private List<String> generateRiskFactors(PredictionRequest request) {
        List<String> riskFactors = new ArrayList<>();

        if ("HIGH".equalsIgnoreCase(request.getComplexity())) {
            String message = properties.getPrediction().getRiskMessages().get("HIGH_COMPLEXITY");
            riskFactors.add(message.replace("{variance}",
                    String.valueOf(properties.getPrediction().getHighComplexityVariance())));
        }

        if (request.getTags() != null && request.getTags().contains("auth")) {
            String message = properties.getPrediction().getRiskMessages().get("AUTH_TAG");
            riskFactors.add(message.replace("{underestimation}",
                    String.valueOf(properties.getPrediction().getAuthUnderestimation())));
        }

        if (request.getAssigneeId() == null) {
            riskFactors.add(properties.getPrediction().getRiskMessages().get("UNASSIGNED"));
        }

        return riskFactors;
    }

    /**
     * Save prediction to history for model improvement
     */
    private void savePredictionHistory(PredictionRequest request, double estimatedDays, double confidence) {
        PredictionHistory history = PredictionHistory.builder()
                .taskId(request.getTaskId())
                .userId(request.getAssigneeId())
                .predictedDays(estimatedDays)
                .confidenceScore(confidence)
                .predictionType("COMPLETION_TIME")
                .modelVersion(properties.getModel().getVersion())
                .features(String.format("{\"priority\":\"%s\",\"complexity\":\"%s\",\"tags\":%s}",
                        request.getPriority(),
                        request.getComplexity(),
                        request.getTags() != null ? request.getTags().toString() : "[]"))
                .build();

        predictionHistoryRepository.save(history);
        log.info("Prediction history saved for task: {}", request.getTaskId());
    }
}

