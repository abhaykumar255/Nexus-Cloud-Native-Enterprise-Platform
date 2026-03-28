package com.nexus.ai.service;

import com.nexus.ai.config.AiServiceProperties;
import com.nexus.ai.domain.document.AnomalyDetectionLog;
import com.nexus.ai.domain.repository.AnomalyDetectionLogRepository;
import com.nexus.ai.dto.AnomalyDetectionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Anomaly Detection Service - Detects anomalies in service metrics
 * Uses Z-score algorithm for basic anomaly detection
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnomalyDetectionService {

    private final AnomalyDetectionLogRepository anomalyLogRepository;
    private final AiServiceProperties properties;

    /**
     * Detect anomalies in service metrics
     */
    public AnomalyDetectionResponse detectAnomalies(String serviceName, Map<String, Double> metrics, LocalDateTime timestamp) {
        log.info("Detecting anomalies for service: {} with metrics: {}", serviceName, metrics);
        
        List<AnomalyDetectionResponse.AnomalyDetail> anomalies = new ArrayList<>();
        boolean isAnomaly = false;
        String severity = "LOW";
        
        // Analyze each metric
        for (Map.Entry<String, Double> entry : metrics.entrySet()) {
            String metricName = entry.getKey();
            Double value = entry.getValue();

            Double threshold = properties.getAnomaly().getMetricThresholds().getOrDefault(metricName, Double.MAX_VALUE);
            Double zScoreThreshold = properties.getAnomaly().getZScoreThresholds().getOrDefault(metricName, 3.0);

            // Calculate Z-score (simplified - in real scenario, use historical data)
            double zScore = calculateZScore(value, threshold);

            if (zScore > zScoreThreshold || value > threshold) {
                isAnomaly = true;
                String status = zScore > properties.getAnomaly().getCriticalZScore() ? "CRITICAL" : "WARNING";
                
                anomalies.add(AnomalyDetectionResponse.AnomalyDetail.builder()
                        .metric(metricName)
                        .value(value)
                        .threshold(threshold)
                        .zScore(zScore)
                        .status(status)
                        .build());
                
                // Update severity
                if ("CRITICAL".equals(status)) {
                    severity = "HIGH";
                } else if ("MEDIUM".equals(severity) && "WARNING".equals(status)) {
                    severity = "MEDIUM";
                }
            }
        }
        
        String recommendation = generateRecommendation(serviceName, anomalies);
        
        // Save to MongoDB
        saveAnomalyLog(serviceName, metrics, isAnomaly, severity, anomalies, recommendation, timestamp);
        
        return AnomalyDetectionResponse.builder()
                .isAnomaly(isAnomaly)
                .severity(severity)
                .anomalies(anomalies)
                .recommendation(recommendation)
                .build();
    }

    /**
     * Calculate Z-score (simplified)
     */
    private double calculateZScore(double value, double threshold) {
        // Simplified Z-score calculation
        // In real scenario, this would use historical mean and standard deviation
        double mean = threshold * 0.7;
        double stdDev = threshold * 0.15;
        return Math.abs((value - mean) / stdDev);
    }

    /**
     * Generate recommendation based on anomalies detected
     */
    private String generateRecommendation(String serviceName, List<AnomalyDetectionResponse.AnomalyDetail> anomalies) {
        if (anomalies.isEmpty()) {
            return "No anomalies detected. Service metrics are within normal range.";
        }

        StringBuilder recommendation = new StringBuilder();

        for (AnomalyDetectionResponse.AnomalyDetail anomaly : anomalies) {
            String metricRecommendation = properties.getAnomaly().getRecommendations().get(anomaly.getMetric());
            if (metricRecommendation != null) {
                recommendation.append(metricRecommendation.replace("{service}", serviceName)).append(" ");
            }
        }

        return recommendation.toString().trim();
    }

    /**
     * Save anomaly detection log to MongoDB
     */
    private void saveAnomalyLog(String serviceName, Map<String, Double> metrics, boolean isAnomaly, 
                                String severity, List<AnomalyDetectionResponse.AnomalyDetail> anomalies,
                                String recommendation, LocalDateTime timestamp) {
        
        List<AnomalyDetectionLog.AnomalyDetail> logAnomalies = anomalies.stream()
                .map(a -> AnomalyDetectionLog.AnomalyDetail.builder()
                        .metric(a.getMetric())
                        .value(a.getValue())
                        .threshold(a.getThreshold())
                        .zScore(a.getZScore())
                        .status(a.getStatus())
                        .build())
                .toList();

        AnomalyDetectionLog detectionLog = AnomalyDetectionLog.builder()
                .serviceName(serviceName)
                .metrics(metrics)
                .isAnomaly(isAnomaly)
                .severity(severity)
                .anomalies(logAnomalies)
                .recommendation(recommendation)
                .timestamp(timestamp != null ? timestamp : LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        anomalyLogRepository.save(detectionLog);
        log.info("Anomaly detection log saved for service: {}", serviceName);
    }
}

