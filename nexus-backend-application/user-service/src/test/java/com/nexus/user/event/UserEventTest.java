package com.nexus.user.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UserEvent Unit Tests")
class UserEventTest {

    @Test
    @DisplayName("Builder creates event with all fields")
    void builder_createsEventWithAllFields() {
        // Arrange
        Instant now = Instant.now();

        // Act
        UserEvent event = UserEvent.builder()
            .eventId("event-123")
            .eventType(UserEvent.EventType.USER_CREATED)
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .timestamp(now)
            .build();

        // Assert
        assertThat(event.getEventId()).isEqualTo("event-123");
        assertThat(event.getEventType()).isEqualTo(UserEvent.EventType.USER_CREATED);
        assertThat(event.getUserId()).isEqualTo("user-123");
        assertThat(event.getEmail()).isEqualTo("test@example.com");
        assertThat(event.getUsername()).isEqualTo("testuser");
        assertThat(event.getTimestamp()).isEqualTo(now);
    }

    @Test
    @DisplayName("No-args constructor creates empty event")
    void noArgsConstructor_createsEmptyEvent() {
        // Act
        UserEvent event = new UserEvent();

        // Assert
        assertThat(event.getEventId()).isNull();
        assertThat(event.getEventType()).isNull();
        assertThat(event.getUserId()).isNull();
        assertThat(event.getEmail()).isNull();
        assertThat(event.getUsername()).isNull();
        assertThat(event.getTimestamp()).isNull();
    }

    @Test
    @DisplayName("All-args constructor creates event with all fields")
    void allArgsConstructor_createsEventWithAllFields() {
        // Arrange
        Instant now = Instant.now();

        // Act
        UserEvent event = new UserEvent(
            "event-456",
            UserEvent.EventType.USER_UPDATED,
            "user-456",
            "new@example.com",
            "newuser",
            now
        );

        // Assert
        assertThat(event.getEventId()).isEqualTo("event-456");
        assertThat(event.getEventType()).isEqualTo(UserEvent.EventType.USER_UPDATED);
        assertThat(event.getUserId()).isEqualTo("user-456");
        assertThat(event.getEmail()).isEqualTo("new@example.com");
        assertThat(event.getUsername()).isEqualTo("newuser");
        assertThat(event.getTimestamp()).isEqualTo(now);
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        UserEvent event = new UserEvent();
        Instant now = Instant.now();

        // Act
        event.setEventId("event-789");
        event.setEventType(UserEvent.EventType.USER_DELETED);
        event.setUserId("user-789");
        event.setEmail("deleted@example.com");
        event.setUsername("deleteduser");
        event.setTimestamp(now);

        // Assert
        assertThat(event.getEventId()).isEqualTo("event-789");
        assertThat(event.getEventType()).isEqualTo(UserEvent.EventType.USER_DELETED);
        assertThat(event.getUserId()).isEqualTo("user-789");
        assertThat(event.getEmail()).isEqualTo("deleted@example.com");
        assertThat(event.getUsername()).isEqualTo("deleteduser");
        assertThat(event.getTimestamp()).isEqualTo(now);
    }

    @Test
    @DisplayName("All event types are accessible")
    void allEventTypes_areAccessible() {
        // Act & Assert
        assertThat(UserEvent.EventType.values()).containsExactlyInAnyOrder(
            UserEvent.EventType.USER_CREATED,
            UserEvent.EventType.USER_UPDATED,
            UserEvent.EventType.USER_DELETED,
            UserEvent.EventType.PROFILE_COMPLETED
        );
    }

    @Test
    @DisplayName("Event type can be set to PROFILE_COMPLETED")
    void eventType_canBeSetToProfileCompleted() {
        // Act
        UserEvent event = UserEvent.builder()
            .eventId("event-pc")
            .eventType(UserEvent.EventType.PROFILE_COMPLETED)
            .userId("user-pc")
            .build();

        // Assert
        assertThat(event.getEventType()).isEqualTo(UserEvent.EventType.PROFILE_COMPLETED);
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        // Arrange
        Instant now = Instant.now();

        UserEvent event1 = UserEvent.builder()
            .eventId("event-123")
            .eventType(UserEvent.EventType.USER_CREATED)
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .timestamp(now)
            .build();

        UserEvent event2 = UserEvent.builder()
            .eventId("event-123")
            .eventType(UserEvent.EventType.USER_CREATED)
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .timestamp(now)
            .build();

        UserEvent event3 = UserEvent.builder()
            .eventId("event-456")
            .eventType(UserEvent.EventType.USER_UPDATED)
            .userId("user-456")
            .build();

        // Assert
        assertThat(event1).isEqualTo(event2);
        assertThat(event1).isNotEqualTo(event3);
        assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
        assertThat(event1).isEqualTo(event1);
        assertThat(event1).isNotEqualTo(null);
        assertThat(event1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        UserEvent event = UserEvent.builder()
            .eventId("event-123")
            .eventType(UserEvent.EventType.USER_CREATED)
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .build();

        // Act
        String result = event.toString();

        // Assert
        assertThat(result).contains("event-123");
        assertThat(result).contains("USER_CREATED");
        assertThat(result).contains("user-123");
        assertThat(result).contains("test@example.com");
        assertThat(result).contains("testuser");
    }
}

