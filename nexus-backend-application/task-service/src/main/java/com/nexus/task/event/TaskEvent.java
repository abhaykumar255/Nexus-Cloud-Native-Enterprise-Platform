package com.nexus.task.event;

import com.nexus.task.domain.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Task domain event published to Kafka
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEvent {
    
    private String eventId;
    private EventType eventType;
    private String taskId;
    private String title;
    private TaskStatus status;
    private String assigneeId;
    private String creatorId;
    private String projectId;
    private Instant timestamp;
    
    public enum EventType {
        TASK_CREATED,
        TASK_UPDATED,
        TASK_DELETED,
        TASK_ASSIGNED,
        TASK_STATUS_CHANGED
    }
}

