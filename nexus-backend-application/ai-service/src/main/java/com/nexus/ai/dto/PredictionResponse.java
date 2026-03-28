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
public class PredictionResponse {
    private Double estimatedDays;
    private Double confidence;
    private String basis;
    private List<String> riskFactors;
}

