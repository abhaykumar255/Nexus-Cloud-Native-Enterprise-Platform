package com.nexus.ai.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AnomalyDetectionRequest Tests")
class AnomalyDetectionRequestTest {

    @Test
    @DisplayName("Builder creates instance correctly")
    void builder_createsInstance() {
        // Arrange
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("error_rate_pct", 5.5);
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        AnomalyDetectionRequest request = AnomalyDetectionRequest.builder()
                .service("task-service")
                .metrics(metrics)
                .windowMinutes(10)
                .timestamp(timestamp)
                .build();

        // Assert
        assertThat(request.getService()).isEqualTo("task-service");
        assertThat(request.getMetrics()).containsEntry("error_rate_pct", 5.5);
        assertThat(request.getWindowMinutes()).isEqualTo(10);
        assertThat(request.getTimestamp()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Default window minutes is 5")
    void defaultWindowMinutes_is5() {
        // Act
        AnomalyDetectionRequest request = new AnomalyDetectionRequest();

        // Assert
        assertThat(request.getWindowMinutes()).isEqualTo(5);
    }
}

