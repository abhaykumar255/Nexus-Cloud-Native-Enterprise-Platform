package com.nexus.task.event;

import com.nexus.task.domain.enums.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TaskEvent Unit Tests")
class TaskEventTest {

    @Test
    @DisplayName("Builder creates event with all fields")
    void builder_createsEventWithAllFields() {
        // Arrange
        Instant now = Instant.now();

        // Act
        TaskEvent event = TaskEvent.builder()
            .eventId("event-123")
            .eventType(TaskEvent.EventType.TASK_CREATED)
            .taskId("task-123")
            .title("Test Task")
            .status(TaskStatus.TODO)
            .assigneeId("user-456")
            .creatorId("user-123")
            .projectId("project-123")
            .timestamp(now)
            .build();

        // Assert
        assertThat(event.getEventId()).isEqualTo("event-123");
        assertThat(event.getEventType()).isEqualTo(TaskEvent.EventType.TASK_CREATED);
        assertThat(event.getTaskId()).isEqualTo("task-123");
        assertThat(event.getTitle()).isEqualTo("Test Task");
        assertThat(event.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(event.getAssigneeId()).isEqualTo("user-456");
        assertThat(event.getCreatorId()).isEqualTo("user-123");
        assertThat(event.getProjectId()).isEqualTo("project-123");
        assertThat(event.getTimestamp()).isEqualTo(now);
    }

    @Test
    @DisplayName("No-args constructor creates empty event")
    void noArgsConstructor_createsEmptyEvent() {
        // Act
        TaskEvent event = new TaskEvent();

        // Assert
        assertThat(event.getEventId()).isNull();
        assertThat(event.getEventType()).isNull();
        assertThat(event.getTaskId()).isNull();
    }

    @Test
    @DisplayName("All-args constructor creates event with all fields")
    void allArgsConstructor_createsEventWithAllFields() {
        // Arrange
        Instant now = Instant.now();

        // Act
        TaskEvent event = new TaskEvent(
            "event-456",
            TaskEvent.EventType.TASK_UPDATED,
            "task-456",
            "Updated Task",
            TaskStatus.IN_PROGRESS,
            "user-789",
            "user-456",
            "project-456",
            now
        );

        // Assert
        assertThat(event.getEventId()).isEqualTo("event-456");
        assertThat(event.getEventType()).isEqualTo(TaskEvent.EventType.TASK_UPDATED);
        assertThat(event.getTaskId()).isEqualTo("task-456");
        assertThat(event.getTitle()).isEqualTo("Updated Task");
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        TaskEvent event = new TaskEvent();
        Instant now = Instant.now();

        // Act
        event.setEventId("event-789");
        event.setEventType(TaskEvent.EventType.TASK_DELETED);
        event.setTaskId("task-789");
        event.setTitle("Deleted Task");
        event.setStatus(TaskStatus.COMPLETED);
        event.setAssigneeId("user-999");
        event.setCreatorId("user-888");
        event.setProjectId("project-789");
        event.setTimestamp(now);

        // Assert
        assertThat(event.getEventId()).isEqualTo("event-789");
        assertThat(event.getEventType()).isEqualTo(TaskEvent.EventType.TASK_DELETED);
        assertThat(event.getTaskId()).isEqualTo("task-789");
        assertThat(event.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("All event types are accessible")
    void allEventTypes_areAccessible() {
        // Act & Assert
        assertThat(TaskEvent.EventType.values()).containsExactlyInAnyOrder(
            TaskEvent.EventType.TASK_CREATED,
            TaskEvent.EventType.TASK_UPDATED,
            TaskEvent.EventType.TASK_DELETED,
            TaskEvent.EventType.TASK_ASSIGNED,
            TaskEvent.EventType.TASK_STATUS_CHANGED
        );
    }

    @Test
    @DisplayName("Event type TASK_ASSIGNED works")
    void eventType_taskAssigned() {
        // Act
        TaskEvent event = TaskEvent.builder()
            .eventId("event-assign")
            .eventType(TaskEvent.EventType.TASK_ASSIGNED)
            .taskId("task-assign")
            .assigneeId("user-new")
            .build();

        // Assert
        assertThat(event.getEventType()).isEqualTo(TaskEvent.EventType.TASK_ASSIGNED);
    }

    @Test
    @DisplayName("Event type TASK_STATUS_CHANGED works")
    void eventType_taskStatusChanged() {
        // Act
        TaskEvent event = TaskEvent.builder()
            .eventId("event-status")
            .eventType(TaskEvent.EventType.TASK_STATUS_CHANGED)
            .taskId("task-status")
            .status(TaskStatus.COMPLETED)
            .build();

        // Assert
        assertThat(event.getEventType()).isEqualTo(TaskEvent.EventType.TASK_STATUS_CHANGED);
        assertThat(event.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        // Arrange
        Instant now = Instant.now();

        TaskEvent event1 = TaskEvent.builder()
            .eventId("event-123")
            .eventType(TaskEvent.EventType.TASK_CREATED)
            .taskId("task-123")
            .title("Test Task")
            .timestamp(now)
            .build();

        TaskEvent event2 = TaskEvent.builder()
            .eventId("event-123")
            .eventType(TaskEvent.EventType.TASK_CREATED)
            .taskId("task-123")
            .title("Test Task")
            .timestamp(now)
            .build();

        TaskEvent event3 = TaskEvent.builder()
            .eventId("event-456")
            .eventType(TaskEvent.EventType.TASK_UPDATED)
            .taskId("task-456")
            .build();

        // Assert
        assertThat(event1).isEqualTo(event2);
        assertThat(event1).isNotEqualTo(event3);
        assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
        assertThat(event1).isEqualTo(event1);
        assertThat(event1).isNotEqualTo(null);
        assertThat(event1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        TaskEvent event = TaskEvent.builder()
            .eventId("event-123")
            .eventType(TaskEvent.EventType.TASK_CREATED)
            .taskId("task-123")
            .title("Test Task")
            .status(TaskStatus.TODO)
            .creatorId("user-123")
            .build();

        // Act
        String result = event.toString();

        // Assert
        assertThat(result).contains("event-123");
        assertThat(result).contains("TASK_CREATED");
        assertThat(result).contains("task-123");
        assertThat(result).contains("Test Task");
    }
}

