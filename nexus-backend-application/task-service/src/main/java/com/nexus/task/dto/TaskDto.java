package com.nexus.task.dto;

import com.nexus.task.domain.enums.TaskPriority;
import com.nexus.task.domain.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private String projectId;
    private String assigneeId;
    private String creatorId;
    private String reporterId;
    private LocalDateTime dueDate;
    private LocalDateTime startDate;
    private LocalDateTime completedDate;
    private int estimatedHours;
    private int actualHours;
    private String category;
    private Set<String> tags;
    private String parentTaskId;
    private boolean archived;
    private Instant createdAt;
    private Instant updatedAt;
}

