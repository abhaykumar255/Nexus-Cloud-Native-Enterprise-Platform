package com.nexus.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionRequest {
    
    @NotNull(message = "Task ID is required")
    private UUID taskId;
    
    @NotBlank(message = "Task title is required")
    private String title;
    
    private String priority; // HIGH, MEDIUM, LOW
    
    private String complexity; // HIGH, MEDIUM, LOW
    
    private UUID assigneeId;
    
    private List<String> tags;
}

