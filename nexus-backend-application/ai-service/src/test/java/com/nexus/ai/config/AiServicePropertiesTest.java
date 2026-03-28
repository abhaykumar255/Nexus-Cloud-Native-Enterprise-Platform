package com.nexus.ai.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AiServiceProperties Tests")
class AiServicePropertiesTest {

    @Test
    @DisplayName("Model config initializes with defaults")
    void modelConfig_hasDefaults() {
        // Arrange
        AiServiceProperties.ModelConfig config = new AiServiceProperties.ModelConfig();

        // Assert
        assertThat(config.getVersion()).isEqualTo("1.3.0");
    }

    @Test
    @DisplayName("Recommendation config initializes with defaults")
    void recommendationConfig_hasDefaults() {
        // Arrange
        AiServiceProperties.RecommendationConfig config = new AiServiceProperties.RecommendationConfig();

        // Assert
        assertThat(config.getCacheTtlMinutes()).isEqualTo(30);
        assertThat(config.getDefaultLimit()).isEqualTo(10);
        assertThat(config.getMaxRecommendations()).isEqualTo(5);
        assertThat(config.getInitialConfidenceScore()).isEqualTo(0.85);
    }

    @Test
    @DisplayName("Anomaly config initializes with defaults")
    void anomalyConfig_hasDefaults() {
        // Arrange
        AiServiceProperties.AnomalyConfig config = new AiServiceProperties.AnomalyConfig();

        // Assert
        assertThat(config.getZScoreThreshold()).isEqualTo(2.5);
        assertThat(config.getCriticalZScore()).isEqualTo(3.5);
        assertThat(config.getMetricThresholds()).containsEntry("error_rate_pct", 5.0);
    }

    @Test
    @DisplayName("Prediction config initializes with defaults")
    void predictionConfig_hasDefaults() {
        // Arrange
        AiServiceProperties.PredictionConfig config = new AiServiceProperties.PredictionConfig();

        // Assert
        assertThat(config.getBaseEstimateDays()).isEqualTo(3.0);
        assertThat(config.getBaseConfidence()).isEqualTo(0.75);
        assertThat(config.getComplexityMultipliers()).containsEntry("HIGH", 2.5);
    }

    @Test
    @DisplayName("Properties instance has all sub-configs")
    void properties_hasAllConfigs() {
        // Arrange
        AiServiceProperties properties = new AiServiceProperties();

        // Assert
        assertThat(properties.getModel()).isNotNull();
        assertThat(properties.getRecommendation()).isNotNull();
        assertThat(properties.getAnomaly()).isNotNull();
        assertThat(properties.getPrediction()).isNotNull();
    }
}

