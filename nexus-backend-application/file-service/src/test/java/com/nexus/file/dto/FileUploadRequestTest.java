package com.nexus.file.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FileUploadRequest Tests")
class FileUploadRequestTest {

    @Test
    @DisplayName("Builder creates request with all fields")
    void builder_createsRequestWithAllFields() {
        // Arrange
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");

        // Act
        FileUploadRequest request = FileUploadRequest.builder()
            .description("Test description")
            .category("DOCUMENT")
            .projectId("project-123")
            .taskId("task-456")
            .metadata(metadata)
            .expiresInDays(7)
            .build();

        // Assert
        assertThat(request.getDescription()).isEqualTo("Test description");
        assertThat(request.getCategory()).isEqualTo("DOCUMENT");
        assertThat(request.getProjectId()).isEqualTo("project-123");
        assertThat(request.getTaskId()).isEqualTo("task-456");
        assertThat(request.getMetadata()).containsKey("key");
        assertThat(request.getExpiresInDays()).isEqualTo(7);
    }

    @Test
    @DisplayName("No-args constructor creates empty request")
    void noArgsConstructor_createsEmptyRequest() {
        // Act
        FileUploadRequest request = new FileUploadRequest();

        // Assert
        assertThat(request).isNotNull();
        assertThat(request.getDescription()).isNull();
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        FileUploadRequest request = new FileUploadRequest();

        // Act
        request.setDescription("New description");
        request.setCategory("IMAGE");
        request.setProjectId("project-789");

        // Assert
        assertThat(request.getDescription()).isEqualTo("New description");
        assertThat(request.getCategory()).isEqualTo("IMAGE");
        assertThat(request.getProjectId()).isEqualTo("project-789");
    }

    @Test
    @DisplayName("All setters work")
    void allSetters_work() {
        // Arrange
        FileUploadRequest request = new FileUploadRequest();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("test", "value");

        // Act
        request.setDescription("Description");
        request.setCategory("VIDEO");
        request.setProjectId("proj-1");
        request.setTaskId("task-1");
        request.setMetadata(metadata);
        request.setExpiresInDays(30);

        // Assert
        assertThat(request.getDescription()).isEqualTo("Description");
        assertThat(request.getCategory()).isEqualTo("VIDEO");
        assertThat(request.getProjectId()).isEqualTo("proj-1");
        assertThat(request.getTaskId()).isEqualTo("task-1");
        assertThat(request.getMetadata()).isEqualTo(metadata);
        assertThat(request.getExpiresInDays()).isEqualTo(30);
    }

    @Test
    @DisplayName("toString contains all fields")
    void toString_containsFields() {
        // Arrange
        FileUploadRequest request = FileUploadRequest.builder()
            .description("Test")
            .category("DOC")
            .build();

        // Act
        String result = request.toString();

        // Assert
        assertThat(result).contains("description");
        assertThat(result).contains("Test");
    }

    @Test
    @DisplayName("Equals and hashCode work")
    void equalsAndHashCode_work() {
        // Arrange
        FileUploadRequest req1 = FileUploadRequest.builder()
            .description("Same")
            .category("CAT")
            .build();

        FileUploadRequest req2 = FileUploadRequest.builder()
            .description("Same")
            .category("CAT")
            .build();

        FileUploadRequest req3 = FileUploadRequest.builder()
            .description("Different")
            .build();

        // Assert
        assertThat(req1).isEqualTo(req2);
        assertThat(req1.hashCode()).isEqualTo(req2.hashCode());
        assertThat(req1).isNotEqualTo(req3);
    }
}

