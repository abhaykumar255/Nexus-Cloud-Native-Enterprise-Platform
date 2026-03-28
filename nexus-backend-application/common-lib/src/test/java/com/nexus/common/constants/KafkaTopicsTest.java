package com.nexus.common.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for KafkaTopics constant class
 */
@DisplayName("KafkaTopics Tests")
class KafkaTopicsTest {

    @Test
    @DisplayName("Should not be able to instantiate KafkaTopics")
    void shouldNotInstantiate() throws NoSuchMethodException {
        // Arrange
        Constructor<KafkaTopics> constructor = KafkaTopics.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        // Act & Assert
        assertThatThrownBy(constructor::newInstance)
            .isInstanceOf(InvocationTargetException.class)
            .hasCauseInstanceOf(UnsupportedOperationException.class)
            .cause()
            .hasMessage("This is a utility class and cannot be instantiated");
    }

    @Test
    @DisplayName("User event topics are defined")
    void userEventTopics() {
        // Assert
        assertThat(KafkaTopics.USER_CREATED).isEqualTo("user.created");
        assertThat(KafkaTopics.USER_UPDATED).isEqualTo("user.updated");
        assertThat(KafkaTopics.USER_DELETED).isEqualTo("user.deleted");
        assertThat(KafkaTopics.USER_EVENTS).isEqualTo("user.events");
    }

    @Test
    @DisplayName("Task event topics are defined")
    void taskEventTopics() {
        // Assert
        assertThat(KafkaTopics.TASK_CREATED).isEqualTo("task.created");
        assertThat(KafkaTopics.TASK_UPDATED).isEqualTo("task.updated");
        assertThat(KafkaTopics.TASK_ASSIGNED).isEqualTo("task.assigned");
        assertThat(KafkaTopics.TASK_STATUS_CHANGED).isEqualTo("task.status.changed");
        assertThat(KafkaTopics.TASK_EVENTS).isEqualTo("task.events");
    }

    @Test
    @DisplayName("File event topics are defined")
    void fileEventTopics() {
        // Assert
        assertThat(KafkaTopics.FILE_UPLOADED).isEqualTo("file.uploaded");
        assertThat(KafkaTopics.FILE_DELETED).isEqualTo("file.deleted");
    }

    @Test
    @DisplayName("Notification event topics are defined")
    void notificationEventTopics() {
        // Assert
        assertThat(KafkaTopics.NOTIFICATION_SENT).isEqualTo("notification.sent");
        assertThat(KafkaTopics.NOTIFICATION_FAILED).isEqualTo("notification.failed");
    }

    @Test
    @DisplayName("Saga event topics are defined")
    void sagaEventTopics() {
        // Assert
        assertThat(KafkaTopics.SAGA_STARTED).isEqualTo("saga.started");
        assertThat(KafkaTopics.SAGA_COMPLETED).isEqualTo("saga.completed");
        assertThat(KafkaTopics.SAGA_FAILED).isEqualTo("saga.failed");
        assertThat(KafkaTopics.SAGA_COMPENSATING).isEqualTo("saga.compensating");
    }

    @Test
    @DisplayName("All Kafka topics follow dot notation convention")
    void kafkaTopicsFollowNamingConvention() {
        // Assert - all topics should use dot notation
        assertThat(KafkaTopics.USER_CREATED).contains(".");
        assertThat(KafkaTopics.USER_UPDATED).contains(".");
        assertThat(KafkaTopics.USER_DELETED).contains(".");
        assertThat(KafkaTopics.USER_EVENTS).contains(".");
        assertThat(KafkaTopics.TASK_CREATED).contains(".");
        assertThat(KafkaTopics.TASK_UPDATED).contains(".");
        assertThat(KafkaTopics.TASK_ASSIGNED).contains(".");
        assertThat(KafkaTopics.TASK_STATUS_CHANGED).contains(".");
        assertThat(KafkaTopics.TASK_EVENTS).contains(".");
        assertThat(KafkaTopics.FILE_UPLOADED).contains(".");
        assertThat(KafkaTopics.FILE_DELETED).contains(".");
        assertThat(KafkaTopics.NOTIFICATION_SENT).contains(".");
        assertThat(KafkaTopics.NOTIFICATION_FAILED).contains(".");
        assertThat(KafkaTopics.SAGA_STARTED).contains(".");
        assertThat(KafkaTopics.SAGA_COMPLETED).contains(".");
        assertThat(KafkaTopics.SAGA_FAILED).contains(".");
        assertThat(KafkaTopics.SAGA_COMPENSATING).contains(".");
    }

    @Test
    @DisplayName("Kafka topics are not null or empty")
    void kafkaTopicsAreNotNullOrEmpty() {
        // Assert
        assertThat(KafkaTopics.USER_CREATED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.USER_UPDATED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.USER_DELETED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.USER_EVENTS).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.TASK_CREATED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.TASK_UPDATED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.TASK_ASSIGNED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.TASK_STATUS_CHANGED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.TASK_EVENTS).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.FILE_UPLOADED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.FILE_DELETED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.NOTIFICATION_SENT).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.NOTIFICATION_FAILED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.SAGA_STARTED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.SAGA_COMPLETED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.SAGA_FAILED).isNotNull().isNotEmpty();
        assertThat(KafkaTopics.SAGA_COMPENSATING).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("All topics are unique")
    void allTopicsAreUnique() {
        // Create a set to ensure no duplicates
        java.util.Set<String> topics = new java.util.HashSet<>();
        
        // Add all topics
        topics.add(KafkaTopics.USER_CREATED);
        topics.add(KafkaTopics.USER_UPDATED);
        topics.add(KafkaTopics.USER_DELETED);
        topics.add(KafkaTopics.USER_EVENTS);
        topics.add(KafkaTopics.TASK_CREATED);
        topics.add(KafkaTopics.TASK_UPDATED);
        topics.add(KafkaTopics.TASK_ASSIGNED);
        topics.add(KafkaTopics.TASK_STATUS_CHANGED);
        topics.add(KafkaTopics.TASK_EVENTS);
        topics.add(KafkaTopics.FILE_UPLOADED);
        topics.add(KafkaTopics.FILE_DELETED);
        topics.add(KafkaTopics.NOTIFICATION_SENT);
        topics.add(KafkaTopics.NOTIFICATION_FAILED);
        topics.add(KafkaTopics.SAGA_STARTED);
        topics.add(KafkaTopics.SAGA_COMPLETED);
        topics.add(KafkaTopics.SAGA_FAILED);
        topics.add(KafkaTopics.SAGA_COMPENSATING);
        
        // Assert - should have 17 unique topics
        assertThat(topics).hasSize(17);
    }
}

