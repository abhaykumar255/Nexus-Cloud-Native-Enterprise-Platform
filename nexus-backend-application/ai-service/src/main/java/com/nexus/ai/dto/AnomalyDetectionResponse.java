package com.nexus.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnomalyDetectionResponse {
    private Boolean isAnomaly;
    private String severity;
    private List<AnomalyDetail> anomalies;
    private String recommendation;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnomalyDetail {
        private String metric;
        private Double value;
        private Double threshold;
        private Double zScore;
        private String status;
    }
}

