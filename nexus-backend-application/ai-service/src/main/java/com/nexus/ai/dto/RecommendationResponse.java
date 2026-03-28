package com.nexus.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationResponse {
    private List<TaskRecommendation> recommendations;
    private String modelVersion;
    private LocalDateTime computedAt;
    private LocalDateTime cachedUntil;
}

