package com.nexus.ai.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PredictionRequest Tests")
class PredictionRequestTest {

    @Test
    @DisplayName("Builder creates instance correctly")
    void builder_createsInstance() {
        // Arrange
        UUID taskId = UUID.randomUUID();
        UUID assigneeId = UUID.randomUUID();

        // Act
        PredictionRequest request = PredictionRequest.builder()
                .taskId(taskId)
                .title("Test Task")
                .priority("HIGH")
                .complexity("MEDIUM")
                .assigneeId(assigneeId)
                .tags(Arrays.asList("auth", "api"))
                .build();

        // Assert
        assertThat(request.getTaskId()).isEqualTo(taskId);
        assertThat(request.getTitle()).isEqualTo("Test Task");
        assertThat(request.getPriority()).isEqualTo("HIGH");
        assertThat(request.getComplexity()).isEqualTo("MEDIUM");
        assertThat(request.getAssigneeId()).isEqualTo(assigneeId);
        assertThat(request.getTags()).containsExactly("auth", "api");
    }

    @Test
    @DisplayName("No-args constructor works")
    void noArgsConstructor_works() {
        // Act
        PredictionRequest request = new PredictionRequest();
        request.setTaskId(UUID.randomUUID());
        request.setTitle("Test");

        // Assert
        assertThat(request.getTaskId()).isNotNull();
        assertThat(request.getTitle()).isEqualTo("Test");
    }

    @Test
    @DisplayName("All-args constructor works")
    void allArgsConstructor_works() {
        // Arrange
        UUID taskId = UUID.randomUUID();
        UUID assigneeId = UUID.randomUUID();

        // Act
        PredictionRequest request = new PredictionRequest(
                taskId, "Test", "LOW", "LOW", assigneeId, Arrays.asList("tag1")
        );

        // Assert
        assertThat(request.getTaskId()).isEqualTo(taskId);
        assertThat(request.getTitle()).isEqualTo("Test");
        assertThat(request.getPriority()).isEqualTo("LOW");
    }
}

