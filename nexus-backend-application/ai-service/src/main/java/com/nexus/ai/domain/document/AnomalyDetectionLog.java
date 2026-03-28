package com.nexus.ai.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * MongoDB document for storing anomaly detection logs and metrics
 */
@Document(collection = "anomaly_detection_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnomalyDetectionLog {

    @Id
    private String id;

    @Indexed
    private String serviceName; // e.g., "task-service"

    private Map<String, Double> metrics; // error_rate_pct, p99_latency_ms, kafka_consumer_lag, heap_usage_pct

    private Boolean isAnomaly;

    private String severity; // LOW, MEDIUM, HIGH, CRITICAL

    private List<AnomalyDetail> anomalies;

    private String recommendation;

    @Indexed
    private LocalDateTime timestamp;

    private LocalDateTime createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnomalyDetail {
        private String metric;
        private Double value;
        private Double threshold;
        private Double zScore;
        private String status; // WARNING, CRITICAL
    }
}

