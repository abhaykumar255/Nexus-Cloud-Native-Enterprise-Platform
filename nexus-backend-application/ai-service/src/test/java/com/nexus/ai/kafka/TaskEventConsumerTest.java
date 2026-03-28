package com.nexus.ai.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.ai.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskEventConsumer Tests")
class TaskEventConsumerTest {

    @Mock
    private RecommendationService recommendationService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TaskEventConsumer taskEventConsumer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        taskEventConsumer = new TaskEventConsumer(recommendationService, objectMapper);
    }

    @Test
    @DisplayName("Consume task event - STATUS_CHANGED to DONE updates profile")
    void consumeTaskEvent_statusChangedToDone() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        Map<String, Object> event = Map.of(
                "eventType", "STATUS_CHANGED",
                "toStatus", "DONE",
                "assigneeId", userId.toString(),
                "skills", "Java,Spring",
                "tags", List.of("backend", "api")
        );
        String message = objectMapper.writeValueAsString(event);

        // Act
        taskEventConsumer.consumeTaskEvent(message, "task.events", "task-123");

        // Assert
        verify(recommendationService).updateUserProfile(
                any(UUID.class),
                anyString(),
                anyList(),
                anyDouble()
        );
    }

    @Test
    @DisplayName("Consume task event - STATUS_CHANGED without assigneeId")
    void consumeTaskEvent_statusChangedWithoutAssignee() throws Exception {
        // Arrange
        Map<String, Object> event = Map.of(
                "eventType", "STATUS_CHANGED",
                "toStatus", "DONE"
        );
        String message = objectMapper.writeValueAsString(event);

        // Act
        taskEventConsumer.consumeTaskEvent(message, "task.events", "task-123");

        // Assert
        verify(recommendationService, never()).updateUserProfile(any(), anyString(), anyList(), anyDouble());
    }

    @Test
    @DisplayName("Consume task event - Different event type")
    void consumeTaskEvent_differentEventType() throws Exception {
        // Arrange
        Map<String, Object> event = Map.of(
                "eventType", "TASK_CREATED",
                "taskId", UUID.randomUUID().toString()
        );
        String message = objectMapper.writeValueAsString(event);

        // Act
        taskEventConsumer.consumeTaskEvent(message, "task.events", "task-123");

        // Assert
        verify(recommendationService, never()).updateUserProfile(any(), anyString(), anyList(), anyDouble());
    }

    @Test
    @DisplayName("Consume task event - Invalid JSON handles error")
    void consumeTaskEvent_invalidJson() {
        // Arrange
        String invalidMessage = "{invalid-json}";

        // Act
        taskEventConsumer.consumeTaskEvent(invalidMessage, "task.events", "task-123");

        // Assert - Should not throw exception, just log error
        verify(recommendationService, never()).updateUserProfile(any(), anyString(), anyList(), anyDouble());
    }

    @Test
    @DisplayName("Consume user event - Processes successfully")
    void consumeUserEvent_success() throws Exception {
        // Arrange
        Map<String, Object> event = Map.of(
                "eventType", "USER_CREATED",
                "userId", UUID.randomUUID().toString(),
                "email", "test@example.com"
        );
        String message = objectMapper.writeValueAsString(event);

        // Act
        taskEventConsumer.consumeUserEvent(message, "user.events");

        // Assert - Just verify no exception is thrown
        // In real implementation, this would update UserBehaviorAnalytics
    }

    @Test
    @DisplayName("Consume user event - Invalid JSON handles error")
    void consumeUserEvent_invalidJson() {
        // Arrange
        String invalidMessage = "{invalid-json}";

        // Act
        taskEventConsumer.consumeUserEvent(invalidMessage, "user.events");

        // Assert - Should not throw exception, just log error
    }
}

