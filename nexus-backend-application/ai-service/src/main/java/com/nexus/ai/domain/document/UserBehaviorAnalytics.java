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
 * MongoDB document for storing user behavior analytics for AI/ML processing
 */
@Document(collection = "user_behavior_analytics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBehaviorAnalytics {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    private Map<String, Integer> tasksByStatus; // OPEN: 5, IN_PROGRESS: 3, DONE: 20

    private Map<String, Integer> tasksByPriority; // HIGH: 8, MEDIUM: 10, LOW: 10

    private List<String> frequentTags; // ["backend", "api", "database"]

    private Double avgTaskCompletionTime; // in days

    private Integer totalTasksCreated;

    private Integer totalTasksCompleted;

    private LocalDateTime lastActiveAt;

    private Map<String, Object> customMetrics; // Extensible for future analytics

    @Indexed
    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;
}

