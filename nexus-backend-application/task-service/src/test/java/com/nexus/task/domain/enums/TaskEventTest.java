package com.nexus.task.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for TaskEvent enum
 */
class TaskEventTest {

    @Test
    @DisplayName("Should have all expected task events")
    void shouldHaveAllExpectedEvents() {
        // Assert
        assertThat(TaskEvent.values()).hasSize(10);
        assertThat(TaskEvent.valueOf("START")).isEqualTo(TaskEvent.START);
        assertThat(TaskEvent.valueOf("SUBMIT_REVIEW")).isEqualTo(TaskEvent.SUBMIT_REVIEW);
        assertThat(TaskEvent.valueOf("APPROVE")).isEqualTo(TaskEvent.APPROVE);
        assertThat(TaskEvent.valueOf("REQUEST_CHANGES")).isEqualTo(TaskEvent.REQUEST_CHANGES);
        assertThat(TaskEvent.valueOf("BLOCK")).isEqualTo(TaskEvent.BLOCK);
        assertThat(TaskEvent.valueOf("UNBLOCK")).isEqualTo(TaskEvent.UNBLOCK);
        assertThat(TaskEvent.valueOf("HOLD")).isEqualTo(TaskEvent.HOLD);
        assertThat(TaskEvent.valueOf("RESUME")).isEqualTo(TaskEvent.RESUME);
        assertThat(TaskEvent.valueOf("COMPLETE")).isEqualTo(TaskEvent.COMPLETE);
        assertThat(TaskEvent.valueOf("CANCEL")).isEqualTo(TaskEvent.CANCEL);
    }

    @Test
    @DisplayName("Should convert to string correctly")
    void shouldConvertToString() {
        // Assert
        assertThat(TaskEvent.START.toString()).isEqualTo("START");
        assertThat(TaskEvent.COMPLETE.toString()).isEqualTo("COMPLETE");
    }

    @Test
    @DisplayName("Should support name() method")
    void shouldSupportNameMethod() {
        // Assert
        assertThat(TaskEvent.START.name()).isEqualTo("START");
        assertThat(TaskEvent.APPROVE.name()).isEqualTo("APPROVE");
    }
}

