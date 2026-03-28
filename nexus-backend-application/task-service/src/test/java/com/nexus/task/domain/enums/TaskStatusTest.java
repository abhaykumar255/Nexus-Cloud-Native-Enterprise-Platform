package com.nexus.task.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for TaskStatus enum
 */
class TaskStatusTest {

    @Test
    @DisplayName("Should have all expected task statuses")
    void shouldHaveAllExpectedStatuses() {
        // Assert
        assertThat(TaskStatus.values()).hasSize(7);
        assertThat(TaskStatus.valueOf("TODO")).isEqualTo(TaskStatus.TODO);
        assertThat(TaskStatus.valueOf("IN_PROGRESS")).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(TaskStatus.valueOf("IN_REVIEW")).isEqualTo(TaskStatus.IN_REVIEW);
        assertThat(TaskStatus.valueOf("BLOCKED")).isEqualTo(TaskStatus.BLOCKED);
        assertThat(TaskStatus.valueOf("ON_HOLD")).isEqualTo(TaskStatus.ON_HOLD);
        assertThat(TaskStatus.valueOf("COMPLETED")).isEqualTo(TaskStatus.COMPLETED);
        assertThat(TaskStatus.valueOf("CANCELLED")).isEqualTo(TaskStatus.CANCELLED);
    }

    @Test
    @DisplayName("Should convert to string correctly")
    void shouldConvertToString() {
        // Assert
        assertThat(TaskStatus.TODO.toString()).isEqualTo("TODO");
        assertThat(TaskStatus.COMPLETED.toString()).isEqualTo("COMPLETED");
    }

    @Test
    @DisplayName("Should support name() method")
    void shouldSupportNameMethod() {
        // Assert
        assertThat(TaskStatus.TODO.name()).isEqualTo("TODO");
        assertThat(TaskStatus.IN_PROGRESS.name()).isEqualTo("IN_PROGRESS");
    }
}

