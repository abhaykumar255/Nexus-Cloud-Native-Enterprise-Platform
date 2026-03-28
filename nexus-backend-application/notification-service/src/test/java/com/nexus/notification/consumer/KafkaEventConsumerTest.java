package com.nexus.notification.consumer;

import com.nexus.notification.domain.enums.NotificationPriority;
import com.nexus.notification.domain.enums.NotificationType;
import com.nexus.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for KafkaEventConsumer
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("KafkaEventConsumer Unit Tests")
class KafkaEventConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private KafkaEventConsumer kafkaEventConsumer;

    @BeforeEach
    void setUp() {
        // Inject field values using reflection
        ReflectionTestUtils.setField(kafkaEventConsumer, "welcomeSubject", "Welcome!");
        ReflectionTestUtils.setField(kafkaEventConsumer, "welcomeBodyTemplate", "Welcome %s!");
        ReflectionTestUtils.setField(kafkaEventConsumer, "taskCreatedSubject", "Task Created");
        ReflectionTestUtils.setField(kafkaEventConsumer, "taskCreatedBodyTemplate", "Task '%s' created");
        ReflectionTestUtils.setField(kafkaEventConsumer, "taskAssignedSubject", "Task Assigned");
        ReflectionTestUtils.setField(kafkaEventConsumer, "taskAssignedBodyTemplate", "Task '%s' assigned");
        ReflectionTestUtils.setField(kafkaEventConsumer, "taskStatusSubject", "Task Status");
        ReflectionTestUtils.setField(kafkaEventConsumer, "taskStatusBodyTemplate", "Task '%s' status %s to %s");
        ReflectionTestUtils.setField(kafkaEventConsumer, "userCategory", "user");
        ReflectionTestUtils.setField(kafkaEventConsumer, "taskCategory", "task");
    }

    @Test
    @DisplayName("consumeUserCreatedEvent sends welcome notification")
    void consumeUserCreatedEvent_sendsNotification() {
        // Arrange
        Map<String, Object> event = new HashMap<>();
        event.put("userId", "user-123");
        event.put("email", "test@example.com");
        event.put("username", "testuser");

        // Act
        kafkaEventConsumer.consumeUserCreatedEvent(event);

        // Assert
        verify(notificationService).sendNotification(argThat(request ->
            request.getUserId().equals("user-123") &&
            request.getType() == NotificationType.EMAIL &&
            request.getPriority() == NotificationPriority.HIGH &&
            request.getSubject().equals("Welcome!") &&
            request.getMessage().contains("testuser") &&
            request.getRecipient().equals("test@example.com") &&
            request.getCategory().equals("user")
        ));
    }

    @Test
    @DisplayName("consumeUserCreatedEvent handles errors gracefully")
    void consumeUserCreatedEvent_handlesErrors() {
        // Arrange
        Map<String, Object> event = new HashMap<>();
        event.put("userId", "user-123");
        event.put("email", "test@example.com");
        event.put("username", "testuser");

        doThrow(new RuntimeException("Test exception"))
            .when(notificationService).sendNotification(any());

        // Act & Assert - should not throw
        kafkaEventConsumer.consumeUserCreatedEvent(event);
    }

    @Test
    @DisplayName("consumeTaskCreatedEvent sends notification when assignee exists")
    void consumeTaskCreatedEvent_sendsNotification() {
        // Arrange
        Map<String, Object> event = new HashMap<>();
        event.put("taskId", "task-123");
        event.put("title", "Test Task");
        event.put("assigneeId", "user-456");

        // Act
        kafkaEventConsumer.consumeTaskCreatedEvent(event);

        // Assert
        verify(notificationService).sendNotification(argThat(request ->
            request.getUserId().equals("user-456") &&
            request.getType() == NotificationType.IN_APP &&
            request.getPriority() == NotificationPriority.MEDIUM
        ));
    }

    @Test
    @DisplayName("consumeTaskCreatedEvent skips notification when no assignee")
    void consumeTaskCreatedEvent_skipsWhenNoAssignee() {
        // Arrange
        Map<String, Object> event = new HashMap<>();
        event.put("taskId", "task-123");
        event.put("title", "Test Task");
        event.put("assigneeId", null);

        // Act
        kafkaEventConsumer.consumeTaskCreatedEvent(event);

        // Assert
        verify(notificationService, never()).sendNotification(any());
    }

    @Test
    @DisplayName("consumeTaskCreatedEvent handles errors gracefully")
    void consumeTaskCreatedEvent_handlesErrors() {
        // Arrange
        Map<String, Object> event = new HashMap<>();
        event.put("taskId", "task-123");
        event.put("title", "Test Task");
        event.put("assigneeId", "user-456");

        doThrow(new RuntimeException("Test exception"))
            .when(notificationService).sendNotification(any());

        // Act & Assert - should not throw
        kafkaEventConsumer.consumeTaskCreatedEvent(event);
    }

    @Test
    @DisplayName("consumeTaskAssignedEvent sends notification when assignee exists")
    void consumeTaskAssignedEvent_sendsNotification() {
        // Arrange
        Map<String, Object> event = new HashMap<>();
        event.put("taskId", "task-789");
        event.put("title", "Assigned Task");
        event.put("assigneeId", "user-789");

        // Act
        kafkaEventConsumer.consumeTaskAssignedEvent(event);

        // Assert
        verify(notificationService).sendNotification(argThat(request ->
            request.getUserId().equals("user-789") &&
            request.getPriority() == NotificationPriority.HIGH
        ));
    }

    @Test
    @DisplayName("consumeTaskAssignedEvent skips notification when no assignee")
    void consumeTaskAssignedEvent_skipsWhenNoAssignee() {
        // Arrange
        Map<String, Object> event = new HashMap<>();
        event.put("taskId", "task-789");
        event.put("title", "Assigned Task");
        event.put("assigneeId", null);

        // Act
        kafkaEventConsumer.consumeTaskAssignedEvent(event);

        // Assert
        verify(notificationService, never()).sendNotification(any());
    }

    @Test
    @DisplayName("consumeTaskAssignedEvent handles errors gracefully")
    void consumeTaskAssignedEvent_handlesErrors() {
        // Arrange
        Map<String, Object> event = new HashMap<>();
        event.put("taskId", "task-789");
        event.put("title", "Assigned Task");
        event.put("assigneeId", "user-789");

        doThrow(new RuntimeException("Test exception"))
            .when(notificationService).sendNotification(any());

        // Act & Assert - should not throw
        kafkaEventConsumer.consumeTaskAssignedEvent(event);
    }

    @Test
    @DisplayName("consumeTaskStatusChangedEvent sends notification when creator exists")
    void consumeTaskStatusChangedEvent_sendsNotification() {
        // Arrange
        Map<String, Object> event = new HashMap<>();
        event.put("taskId", "task-999");
        event.put("title", "Status Task");
        event.put("status", "COMPLETED");
        event.put("creatorId", "user-999");

        // Act
        kafkaEventConsumer.consumeTaskStatusChangedEvent(event);

        // Assert
        verify(notificationService).sendNotification(argThat(request ->
            request.getUserId().equals("user-999") &&
            request.getPriority() == NotificationPriority.MEDIUM
        ));
    }

    @Test
    @DisplayName("consumeTaskStatusChangedEvent skips notification when no creator")
    void consumeTaskStatusChangedEvent_skipsWhenNoCreator() {
        // Arrange
        Map<String, Object> event = new HashMap<>();
        event.put("taskId", "task-999");
        event.put("title", "Status Task");
        event.put("status", "COMPLETED");
        event.put("creatorId", null);

        // Act
        kafkaEventConsumer.consumeTaskStatusChangedEvent(event);

        // Assert
        verify(notificationService, never()).sendNotification(any());
    }

    @Test
    @DisplayName("consumeTaskStatusChangedEvent handles errors gracefully")
    void consumeTaskStatusChangedEvent_handlesErrors() {
        // Arrange
        Map<String, Object> event = new HashMap<>();
        event.put("taskId", "task-999");
        event.put("title", "Status Task");
        event.put("status", "COMPLETED");
        event.put("creatorId", "user-999");

        doThrow(new RuntimeException("Test exception"))
            .when(notificationService).sendNotification(any());

        // Act & Assert - should not throw
        kafkaEventConsumer.consumeTaskStatusChangedEvent(event);
    }
}

