package com.nexus.task.dto;

import com.nexus.task.domain.enums.TaskPriority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CreateTaskRequest Unit Tests")
class CreateTaskRequestTest {

    @Test
    @DisplayName("No-args constructor creates request with default priority")
    void noArgsConstructor_createsRequestWithDefaults() {
        // Act
        CreateTaskRequest request = new CreateTaskRequest();

        // Assert
        assertThat(request.getTitle()).isNull();
        assertThat(request.getDescription()).isNull();
        assertThat(request.getPriority()).isEqualTo(TaskPriority.MEDIUM);
    }

    @Test
    @DisplayName("All-args constructor creates request with all fields")
    void allArgsConstructor_createsRequestWithAllFields() {
        // Arrange
        Set<String> tags = new HashSet<>();
        tags.add("backend");
        tags.add("urgent");
        LocalDateTime dueDate = LocalDateTime.now().plusDays(7);

        // Act
        CreateTaskRequest request = new CreateTaskRequest(
            "Test Task",
            "Test Description",
            TaskPriority.HIGH,
            "project-123",
            "user-456",
            dueDate,
            5,
            "Development",
            tags,
            "parent-123"
        );

        // Assert
        assertThat(request.getTitle()).isEqualTo("Test Task");
        assertThat(request.getDescription()).isEqualTo("Test Description");
        assertThat(request.getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(request.getProjectId()).isEqualTo("project-123");
        assertThat(request.getAssigneeId()).isEqualTo("user-456");
        assertThat(request.getDueDate()).isEqualTo(dueDate);
        assertThat(request.getEstimatedHours()).isEqualTo(5);
        assertThat(request.getCategory()).isEqualTo("Development");
        assertThat(request.getTags()).containsExactlyInAnyOrder("backend", "urgent");
        assertThat(request.getParentTaskId()).isEqualTo("parent-123");
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest();
        Set<String> tags = new HashSet<>();
        tags.add("frontend");
        LocalDateTime dueDate = LocalDateTime.now().plusWeeks(2);

        // Act
        request.setTitle("New Task");
        request.setDescription("New Description");
        request.setPriority(TaskPriority.LOW);
        request.setProjectId("project-456");
        request.setAssigneeId("user-789");
        request.setDueDate(dueDate);
        request.setEstimatedHours(10);
        request.setCategory("Testing");
        request.setTags(tags);
        request.setParentTaskId("parent-456");

        // Assert
        assertThat(request.getTitle()).isEqualTo("New Task");
        assertThat(request.getDescription()).isEqualTo("New Description");
        assertThat(request.getPriority()).isEqualTo(TaskPriority.LOW);
        assertThat(request.getProjectId()).isEqualTo("project-456");
        assertThat(request.getAssigneeId()).isEqualTo("user-789");
        assertThat(request.getDueDate()).isEqualTo(dueDate);
        assertThat(request.getEstimatedHours()).isEqualTo(10);
        assertThat(request.getCategory()).isEqualTo("Testing");
        assertThat(request.getTags()).containsExactly("frontend");
        assertThat(request.getParentTaskId()).isEqualTo("parent-456");
    }

    @Test
    @DisplayName("Can create minimal request with only title")
    void minimalRequest_onlyTitle() {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest();

        // Act
        request.setTitle("Minimal Task");

        // Assert
        assertThat(request.getTitle()).isEqualTo("Minimal Task");
        assertThat(request.getPriority()).isEqualTo(TaskPriority.MEDIUM);
        assertThat(request.getProjectId()).isNull();
        assertThat(request.getAssigneeId()).isNull();
    }

    @Test
    @DisplayName("Priority can be set to all values")
    void priority_allValues() {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest();

        // Act & Assert - LOW
        request.setPriority(TaskPriority.LOW);
        assertThat(request.getPriority()).isEqualTo(TaskPriority.LOW);

        // MEDIUM
        request.setPriority(TaskPriority.MEDIUM);
        assertThat(request.getPriority()).isEqualTo(TaskPriority.MEDIUM);

        // HIGH
        request.setPriority(TaskPriority.HIGH);
        assertThat(request.getPriority()).isEqualTo(TaskPriority.HIGH);

        // URGENT
        request.setPriority(TaskPriority.URGENT);
        assertThat(request.getPriority()).isEqualTo(TaskPriority.URGENT);
    }

    @Test
    @DisplayName("Tags can be null or empty")
    void tags_canBeNullOrEmpty() {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest();

        // Act & Assert - null
        request.setTags(null);
        assertThat(request.getTags()).isNull();

        // Empty set
        request.setTags(new HashSet<>());
        assertThat(request.getTags()).isEmpty();
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        // Arrange
        CreateTaskRequest request1 = new CreateTaskRequest();
        request1.setTitle("Test Task");
        request1.setDescription("Test Description");
        request1.setPriority(TaskPriority.HIGH);

        CreateTaskRequest request2 = new CreateTaskRequest();
        request2.setTitle("Test Task");
        request2.setDescription("Test Description");
        request2.setPriority(TaskPriority.HIGH);

        CreateTaskRequest request3 = new CreateTaskRequest();
        request3.setTitle("Different Task");

        // Assert
        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1).isEqualTo(request1);
        assertThat(request1).isNotEqualTo(null);
        assertThat(request1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        request.setPriority(TaskPriority.HIGH);
        request.setProjectId("project-123");

        // Act
        String result = request.toString();

        // Assert
        assertThat(result).contains("Test Task");
        assertThat(result).contains("HIGH");
        assertThat(result).contains("project-123");
    }
}

