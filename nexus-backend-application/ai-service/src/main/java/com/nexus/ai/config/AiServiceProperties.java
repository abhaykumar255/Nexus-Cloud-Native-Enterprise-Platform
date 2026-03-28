package com.nexus.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for AI Service
 * All configurable values externalized from code
 */
@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiServiceProperties {

    private ModelConfig model = new ModelConfig();
    private RecommendationConfig recommendation = new RecommendationConfig();
    private AnomalyConfig anomaly = new AnomalyConfig();
    private PredictionConfig prediction = new PredictionConfig();

    @Data
    public static class ModelConfig {
        private String version = "1.3.0";
    }

    @Data
    public static class RecommendationConfig {
        private int cacheTtlMinutes = 30;
        private int defaultLimit = 10;
        private int maxRecommendations = 5;
        private int defaultRecommendations = 3;
        private double initialConfidenceScore = 0.85;
        private double confidenceDecrement = 0.05;
        private double defaultConfidenceScore = 0.70;
    }

    @Data
    public static class AnomalyConfig {
        private double zScoreThreshold = 2.5;
        private int windowMinutes = 5;
        private double criticalZScore = 3.5;
        
        private Map<String, Double> metricThresholds = new HashMap<>() {{
            put("error_rate_pct", 5.0);
            put("p99_latency_ms", 2000.0);
            put("kafka_consumer_lag", 10000.0);
            put("heap_usage_pct", 80.0);
        }};
        
        private Map<String, Double> zScoreThresholds = new HashMap<>() {{
            put("error_rate_pct", 2.5);
            put("p99_latency_ms", 2.5);
            put("kafka_consumer_lag", 3.0);
            put("heap_usage_pct", 2.0);
        }};
        
        private Map<String, String> recommendations = new HashMap<>() {{
            put("kafka_consumer_lag", "Scale {service} consumers - lag spike detected.");
            put("error_rate_pct", "Investigate error logs in {service}.");
            put("p99_latency_ms", "Check database query performance or external API latency.");
            put("heap_usage_pct", "Possible memory leak - analyze heap dump.");
        }};
    }

    @Data
    public static class PredictionConfig {
        private double baseEstimateDays = 3.0;
        private double baseConfidence = 0.75;
        private double confidenceBoostForAssignee = 0.05;
        private double confidenceBoostForTags = 0.03;
        private double maxConfidence = 0.95;
        private double authTaskMultiplier = 1.2;
        private double highComplexityVariance = 30.0;
        private double authUnderestimation = 20.0;
        
        private Map<String, Double> complexityMultipliers = new HashMap<>() {{
            put("LOW", 1.0);
            put("MEDIUM", 1.5);
            put("HIGH", 2.5);
        }};
        
        private Map<String, Double> priorityMultipliers = new HashMap<>() {{
            put("LOW", 1.2);
            put("MEDIUM", 1.0);
            put("HIGH", 0.9);
        }};
        
        private Map<String, String> riskMessages = new HashMap<>() {{
            put("HIGH_COMPLEXITY", "High complexity tasks have {variance}% variance in completion time");
            put("AUTH_TAG", "Auth complexity historically underestimated by {underestimation}%");
            put("UNASSIGNED", "Unassigned tasks typically take longer to start");
        }};
    }
}

