package com.nexus.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnomalyDetectionRequest {
    
    @NotBlank(message = "Service name is required")
    private String service;
    
    @NotNull(message = "Metrics are required")
    private Map<String, Double> metrics; // error_rate_pct, p99_latency_ms, kafka_consumer_lag, heap_usage_pct
    
    private Integer windowMinutes = 5;
    
    private LocalDateTime timestamp;
}

