package com.nexus.search.consumer;

import com.nexus.search.document.TaskDocument;
import com.nexus.search.service.TaskSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("KafkaEventConsumer Tests")
class KafkaEventConsumerTest {

    @Mock
    private TaskSearchService taskSearchService;

    @InjectMocks
    private KafkaEventConsumer kafkaEventConsumer;

    private Map<String, Object> taskEvent;

    @BeforeEach
    void setUp() {
        taskEvent = new HashMap<>();
        taskEvent.put("taskId", "task-123");
        taskEvent.put("title", "Test Task");
        taskEvent.put("description", "Test description");
        taskEvent.put("status", "OPEN");
        taskEvent.put("priority", "HIGH");
        taskEvent.put("assigneeId", "user-123");
        taskEvent.put("creatorId", "user-456");
        taskEvent.put("projectId", "project-789");
        taskEvent.put("category", "DEVELOPMENT");
    }

    @Test
    @DisplayName("Consume task event - Success")
    void consumeTaskEvent_success() {
        // Arrange
        doNothing().when(taskSearchService).indexTask(any(TaskDocument.class));

        // Act
        kafkaEventConsumer.consumeTaskEvent(taskEvent);

        // Assert
        verify(taskSearchService).indexTask(argThat(doc -> 
            doc.getId().equals("task-123") &&
            doc.getTitle().equals("Test Task") &&
            doc.getDescription().equals("Test description") &&
            doc.getStatus().equals("OPEN") &&
            doc.getPriority().equals("HIGH")
        ));
    }

    @Test
    @DisplayName("Consume task event - Handles exception gracefully")
    void consumeTaskEvent_handlesException() {
        // Arrange
        doThrow(new RuntimeException("Indexing failed"))
                .when(taskSearchService).indexTask(any(TaskDocument.class));

        // Act - should not throw
        kafkaEventConsumer.consumeTaskEvent(taskEvent);

        // Assert
        verify(taskSearchService).indexTask(any(TaskDocument.class));
    }

    @Test
    @DisplayName("Consume task event - Handles malformed event")
    void consumeTaskEvent_handlesMalformedEvent() {
        // Arrange
        Map<String, Object> malformedEvent = new HashMap<>();
        malformedEvent.put("taskId", null);

        // Act - should not throw
        kafkaEventConsumer.consumeTaskEvent(malformedEvent);

        // Assert
        verify(taskSearchService, times(1)).indexTask(any(TaskDocument.class));
    }
}

