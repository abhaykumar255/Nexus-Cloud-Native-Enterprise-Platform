package com.nexus.task.dto;

import com.nexus.task.domain.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    private String description;
    
    private TaskPriority priority = TaskPriority.MEDIUM;
    
    private String projectId;
    
    private String assigneeId;
    
    private LocalDateTime dueDate;
    
    private Integer estimatedHours;
    
    private String category;
    
    private Set<String> tags;
    
    private String parentTaskId;
}

