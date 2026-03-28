package com.nexus.search.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Task document for Elasticsearch
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDocument {

    private String id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String assigneeId;
    private String assigneeName;
    private String creatorId;
    private String creatorName;
    private String projectId;
    private List<String> tags;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;
}

