package com.nexus.task.domain.entity;

import com.nexus.task.domain.enums.TaskPriority;
import com.nexus.task.domain.enums.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Task Entity Unit Tests")
class TaskTest {

    @Test
    @DisplayName("Builder creates entity with all fields")
    void builder_createsEntityWithAllFields() {
        // Arrange
        Set<String> tags = new HashSet<>();
        tags.add("backend");
        tags.add("urgent");

        LocalDateTime dueDate = LocalDateTime.now().plusDays(7);
        LocalDateTime startDate = LocalDateTime.now();

        // Act
        Task task = Task.builder()
            .id("task-123")
            .title("Test Task")
            .description("Test Description")
            .status(TaskStatus.TODO)
            .priority(TaskPriority.HIGH)
            .projectId("project-123")
            .assigneeId("user-456")
            .creatorId("user-123")
            .reporterId("user-789")
            .dueDate(dueDate)
            .startDate(startDate)
            .estimatedHours(5)
            .actualHours(3)
            .category("Development")
            .tags(tags)
            .parentTaskId("parent-123")
            .archived(false)
            .build();

        // Assert
        assertThat(task.getId()).isEqualTo("task-123");
        assertThat(task.getTitle()).isEqualTo("Test Task");
        assertThat(task.getDescription()).isEqualTo("Test Description");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(task.getProjectId()).isEqualTo("project-123");
        assertThat(task.getAssigneeId()).isEqualTo("user-456");
        assertThat(task.getCreatorId()).isEqualTo("user-123");
        assertThat(task.getReporterId()).isEqualTo("user-789");
        assertThat(task.getDueDate()).isEqualTo(dueDate);
        assertThat(task.getStartDate()).isEqualTo(startDate);
        assertThat(task.getEstimatedHours()).isEqualTo(5);
        assertThat(task.getActualHours()).isEqualTo(3);
        assertThat(task.getCategory()).isEqualTo("Development");
        assertThat(task.getTags()).containsExactlyInAnyOrder("backend", "urgent");
        assertThat(task.getParentTaskId()).isEqualTo("parent-123");
        assertThat(task.isArchived()).isFalse();
    }

    @Test
    @DisplayName("Builder uses default values")
    void builder_usesDefaultValues() {
        // Act
        Task task = Task.builder()
            .title("Default Task")
            .creatorId("user-123")
            .build();

        // Assert
        assertThat(task.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.MEDIUM);
        assertThat(task.getEstimatedHours()).isEqualTo(0);
        assertThat(task.getActualHours()).isEqualTo(0);
        assertThat(task.isArchived()).isFalse();
    }

    @Test
    @DisplayName("No-args constructor creates empty entity")
    void noArgsConstructor_createsEmptyEntity() {
        // Act
        Task task = new Task();

        // Assert
        assertThat(task.getId()).isNull();
        assertThat(task.getTitle()).isNull();
        assertThat(task.getCreatorId()).isNull();
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        Task task = new Task();
        Set<String> tags = new HashSet<>();
        tags.add("frontend");

        // Act
        task.setId("task-456");
        task.setTitle("New Task");
        task.setDescription("New Description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(TaskPriority.LOW);
        task.setProjectId("project-456");
        task.setAssigneeId("user-999");
        task.setCreatorId("user-888");
        task.setEstimatedHours(10);
        task.setActualHours(8);
        task.setCategory("Testing");
        task.setTags(tags);
        task.setArchived(true);

        // Assert
        assertThat(task.getId()).isEqualTo("task-456");
        assertThat(task.getTitle()).isEqualTo("New Task");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.LOW);
        assertThat(task.isArchived()).isTrue();
        assertThat(task.getTags()).containsExactly("frontend");
    }

    @Test
    @DisplayName("Can update task status through lifecycle")
    void taskLifecycle_statusChanges() {
        // Arrange
        Task task = Task.builder()
            .title("Lifecycle Task")
            .creatorId("user-123")
            .status(TaskStatus.TODO)
            .build();

        // Act & Assert - Start work
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setStartDate(LocalDateTime.now());
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task.getStartDate()).isNotNull();

        // Complete task
        task.setStatus(TaskStatus.COMPLETED);
        task.setCompletedDate(LocalDateTime.now());
        assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(task.getCompletedDate()).isNotNull();
    }

    @Test
    @DisplayName("All-args constructor creates entity with all fields")
    void allArgsConstructor_createsEntityWithAllFields() {
        // Arrange
        Set<String> tags = new HashSet<>();
        tags.add("test");

        // Act
        Task task = new Task(
            "task-789",
            "All Args Task",
            "All Args Description",
            TaskStatus.COMPLETED,
            TaskPriority.URGENT,
            "project-789",
            "user-111",
            "user-222",
            "user-333",
            null,
            null,
            null,
            8,
            8,
            "Design",
            tags,
            null,
            false,
            null,
            null
        );

        // Assert
        assertThat(task.getId()).isEqualTo("task-789");
        assertThat(task.getTitle()).isEqualTo("All Args Task");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.URGENT);
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        // Arrange
        Task task1 = Task.builder()
            .id("task-123")
            .title("Test Task")
            .status(TaskStatus.TODO)
            .creatorId("user-123")
            .build();

        Task task2 = Task.builder()
            .id("task-123")
            .title("Test Task")
            .status(TaskStatus.TODO)
            .creatorId("user-123")
            .build();

        Task task3 = Task.builder()
            .id("task-456")
            .title("Different Task")
            .status(TaskStatus.TODO)
            .creatorId("user-456")
            .build();

        // Assert
        assertThat(task1).isEqualTo(task2);
        assertThat(task1).isNotEqualTo(task3);
        assertThat(task1.hashCode()).isEqualTo(task2.hashCode());
        assertThat(task1).isEqualTo(task1);
        assertThat(task1).isNotEqualTo(null);
        assertThat(task1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        Task task = Task.builder()
            .id("task-123")
            .title("Test Task")
            .status(TaskStatus.TODO)
            .priority(TaskPriority.HIGH)
            .creatorId("user-123")
            .build();

        // Act
        String result = task.toString();

        // Assert
        assertThat(result).contains("task-123");
        assertThat(result).contains("Test Task");
        assertThat(result).contains("TODO");
        assertThat(result).contains("HIGH");
    }
}

