package com.nexus.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRecommendation {
    private UUID taskId;
    private String title;
    private Double confidenceScore;
    private String reason;
}

