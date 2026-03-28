package com.nexus.task.dto;

import com.nexus.task.domain.enums.TaskPriority;
import com.nexus.task.domain.enums.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TaskDto Unit Tests")
class TaskDtoTest {

    @Test
    @DisplayName("Builder creates DTO with all fields")
    void builder_createsDtoWithAllFields() {
        // Arrange
        Set<String> tags = new HashSet<>();
        tags.add("backend");
        tags.add("urgent");

        Instant now = Instant.now();
        LocalDateTime dueDate = LocalDateTime.now().plusDays(7);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime completedDate = LocalDateTime.now().plusHours(5);

        // Act
        TaskDto dto = TaskDto.builder()
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
            .completedDate(completedDate)
            .estimatedHours(5)
            .actualHours(3)
            .category("Development")
            .tags(tags)
            .parentTaskId("parent-123")
            .archived(false)
            .createdAt(now)
            .updatedAt(now)
            .build();

        // Assert
        assertThat(dto.getId()).isEqualTo("task-123");
        assertThat(dto.getTitle()).isEqualTo("Test Task");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(dto.getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(dto.getProjectId()).isEqualTo("project-123");
        assertThat(dto.getAssigneeId()).isEqualTo("user-456");
        assertThat(dto.getCreatorId()).isEqualTo("user-123");
        assertThat(dto.getReporterId()).isEqualTo("user-789");
        assertThat(dto.getDueDate()).isEqualTo(dueDate);
        assertThat(dto.getStartDate()).isEqualTo(startDate);
        assertThat(dto.getCompletedDate()).isEqualTo(completedDate);
        assertThat(dto.getEstimatedHours()).isEqualTo(5);
        assertThat(dto.getActualHours()).isEqualTo(3);
        assertThat(dto.getCategory()).isEqualTo("Development");
        assertThat(dto.getTags()).containsExactlyInAnyOrder("backend", "urgent");
        assertThat(dto.getParentTaskId()).isEqualTo("parent-123");
        assertThat(dto.isArchived()).isFalse();
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("No-args constructor creates empty DTO")
    void noArgsConstructor_createsEmptyDto() {
        // Act
        TaskDto dto = new TaskDto();

        // Assert
        assertThat(dto.getId()).isNull();
        assertThat(dto.getTitle()).isNull();
        assertThat(dto.getStatus()).isNull();
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        TaskDto dto = new TaskDto();
        Set<String> tags = new HashSet<>();
        tags.add("frontend");

        // Act
        dto.setId("task-456");
        dto.setTitle("New Task");
        dto.setDescription("New Description");
        dto.setStatus(TaskStatus.IN_PROGRESS);
        dto.setPriority(TaskPriority.LOW);
        dto.setProjectId("project-456");
        dto.setAssigneeId("user-999");
        dto.setCreatorId("user-888");
        dto.setEstimatedHours(10);
        dto.setActualHours(8);
        dto.setCategory("Testing");
        dto.setTags(tags);
        dto.setArchived(true);

        // Assert
        assertThat(dto.getId()).isEqualTo("task-456");
        assertThat(dto.getTitle()).isEqualTo("New Task");
        assertThat(dto.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(dto.getPriority()).isEqualTo(TaskPriority.LOW);
        assertThat(dto.isArchived()).isTrue();
        assertThat(dto.getTags()).containsExactly("frontend");
    }

    @Test
    @DisplayName("All-args constructor creates DTO with all fields")
    void allArgsConstructor_createsDtoWithAllFields() {
        // Arrange
        Set<String> tags = new HashSet<>();
        tags.add("test");
        Instant now = Instant.now();

        // Act
        TaskDto dto = new TaskDto(
            "task-789",
            "All Args Task",
            "All Args Description",
            TaskStatus.COMPLETED,
            TaskPriority.MEDIUM,
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
            now,
            now
        );

        // Assert
        assertThat(dto.getId()).isEqualTo("task-789");
        assertThat(dto.getTitle()).isEqualTo("All Args Task");
        assertThat(dto.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(dto.getEstimatedHours()).isEqualTo(8);
        assertThat(dto.getActualHours()).isEqualTo(8);
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        // Arrange
        TaskDto dto1 = TaskDto.builder()
            .id("task-123")
            .title("Test Task")
            .status(TaskStatus.TODO)
            .build();

        TaskDto dto2 = TaskDto.builder()
            .id("task-123")
            .title("Test Task")
            .status(TaskStatus.TODO)
            .build();

        TaskDto dto3 = TaskDto.builder()
            .id("task-456")
            .title("Different Task")
            .status(TaskStatus.TODO)
            .build();

        // Assert
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1).isNotEqualTo(dto3);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isEqualTo(dto1);
        assertThat(dto1).isNotEqualTo(null);
        assertThat(dto1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        TaskDto dto = TaskDto.builder()
            .id("task-123")
            .title("Test Task")
            .status(TaskStatus.TODO)
            .priority(TaskPriority.HIGH)
            .build();

        // Act
        String result = dto.toString();

        // Assert
        assertThat(result).contains("task-123");
        assertThat(result).contains("Test Task");
        assertThat(result).contains("TODO");
        assertThat(result).contains("HIGH");
    }
}

