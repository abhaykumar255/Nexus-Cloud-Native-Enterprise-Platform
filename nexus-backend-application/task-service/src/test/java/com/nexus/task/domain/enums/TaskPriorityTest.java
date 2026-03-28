package com.nexus.task.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for TaskPriority enum
 */
class TaskPriorityTest {

    @Test
    @DisplayName("Should have all expected task priorities")
    void shouldHaveAllExpectedPriorities() {
        // Assert
        assertThat(TaskPriority.values()).hasSize(4);
        assertThat(TaskPriority.valueOf("LOW")).isEqualTo(TaskPriority.LOW);
        assertThat(TaskPriority.valueOf("MEDIUM")).isEqualTo(TaskPriority.MEDIUM);
        assertThat(TaskPriority.valueOf("HIGH")).isEqualTo(TaskPriority.HIGH);
        assertThat(TaskPriority.valueOf("URGENT")).isEqualTo(TaskPriority.URGENT);
    }

    @Test
    @DisplayName("Should convert to string correctly")
    void shouldConvertToString() {
        // Assert
        assertThat(TaskPriority.LOW.toString()).isEqualTo("LOW");
        assertThat(TaskPriority.URGENT.toString()).isEqualTo("URGENT");
    }

    @Test
    @DisplayName("Should maintain priority order")
    void shouldMaintainPriorityOrder() {
        // Arrange
        TaskPriority[] priorities = TaskPriority.values();

        // Assert
        assertThat(priorities[0]).isEqualTo(TaskPriority.LOW);
        assertThat(priorities[1]).isEqualTo(TaskPriority.MEDIUM);
        assertThat(priorities[2]).isEqualTo(TaskPriority.HIGH);
        assertThat(priorities[3]).isEqualTo(TaskPriority.URGENT);
    }
}

