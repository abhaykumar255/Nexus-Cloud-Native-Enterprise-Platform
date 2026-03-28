package com.nexus.search.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskDocument Tests")
class TaskDocumentTest {

    @Test
    @DisplayName("Builder creates instance with all fields")
    void builder_createsInstanceWithAllFields() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        List<String> tags = Arrays.asList("urgent", "backend");

        // Act
        TaskDocument document = TaskDocument.builder()
                .id("task-123")
                .title("Implement feature")
                .description("Feature description")
                .status("IN_PROGRESS")
                .priority("HIGH")
                .assigneeId("user-123")
                .assigneeName("John Doe")
                .creatorId("user-456")
                .creatorName("Jane Smith")
                .projectId("project-789")
                .tags(tags)
                .category("DEVELOPMENT")
                .createdAt(now)
                .updatedAt(now)
                .dueDate(now.plusDays(7))
                .completedAt(null)
                .build();

        // Assert
        assertThat(document.getId()).isEqualTo("task-123");
        assertThat(document.getTitle()).isEqualTo("Implement feature");
        assertThat(document.getDescription()).isEqualTo("Feature description");
        assertThat(document.getStatus()).isEqualTo("IN_PROGRESS");
        assertThat(document.getPriority()).isEqualTo("HIGH");
        assertThat(document.getAssigneeId()).isEqualTo("user-123");
        assertThat(document.getAssigneeName()).isEqualTo("John Doe");
        assertThat(document.getCreatorId()).isEqualTo("user-456");
        assertThat(document.getCreatorName()).isEqualTo("Jane Smith");
        assertThat(document.getProjectId()).isEqualTo("project-789");
        assertThat(document.getTags()).containsExactly("urgent", "backend");
        assertThat(document.getCategory()).isEqualTo("DEVELOPMENT");
        assertThat(document.getCreatedAt()).isEqualTo(now);
        assertThat(document.getUpdatedAt()).isEqualTo(now);
        assertThat(document.getDueDate()).isEqualTo(now.plusDays(7));
        assertThat(document.getCompletedAt()).isNull();
    }

    @Test
    @DisplayName("No-args constructor creates instance")
    void noArgsConstructor_createsInstance() {
        // Act
        TaskDocument document = new TaskDocument();

        // Assert
        assertThat(document).isNotNull();
    }

    @Test
    @DisplayName("Setters update fields")
    void setters_updateFields() {
        // Arrange
        TaskDocument document = new TaskDocument();
        LocalDateTime now = LocalDateTime.now();

        // Act
        document.setId("task-999");
        document.setTitle("New Title");
        document.setStatus("COMPLETED");
        document.setCompletedAt(now);

        // Assert
        assertThat(document.getId()).isEqualTo("task-999");
        assertThat(document.getTitle()).isEqualTo("New Title");
        assertThat(document.getStatus()).isEqualTo("COMPLETED");
        assertThat(document.getCompletedAt()).isEqualTo(now);
    }
}

