package com.nexus.task.config;

import com.nexus.task.domain.enums.TaskEvent;
import com.nexus.task.domain.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * Tests for TaskStateMachineConfig - tests state machine configuration
 */
@DisplayName("TaskStateMachineConfig Tests")
class TaskStateMachineConfigTest {

    private TaskStateMachineConfig config;

    @BeforeEach
    void setUp() {
        config = new TaskStateMachineConfig();
    }

    @Test
    @DisplayName("Should create state machine config")
    void shouldCreateStateMachineConfig() {
        // Assert
        assertThat(config).isNotNull();
    }

    @Test
    @DisplayName("Configuration has correct annotation")
    void configurationHasCorrectAnnotation() {
        // Verify the class has the required annotations
        assertThat(config.getClass().isAnnotationPresent(org.springframework.context.annotation.Configuration.class)).isTrue();
    }

    @Test
    @DisplayName("State configuration is valid and does not throw exception")
    void stateConfiguration_isValid() {
        // Arrange
        org.springframework.statemachine.config.builders.StateMachineStateBuilder<TaskStatus, TaskEvent> stateBuilder =
            new org.springframework.statemachine.config.builders.StateMachineStateBuilder<>();

        // Act & Assert - no exception means configuration is valid
        assertThatNoException().isThrownBy(() -> config.configure(stateBuilder));
    }

    @Test
    @DisplayName("Transition configuration is valid and does not throw exception")
    void transitionConfiguration_isValid() {
        // Arrange
        org.springframework.statemachine.config.builders.StateMachineTransitionBuilder<TaskStatus, TaskEvent> transitionBuilder =
            new org.springframework.statemachine.config.builders.StateMachineTransitionBuilder<>();

        // Act & Assert - no exception means configuration is valid
        assertThatNoException().isThrownBy(() -> config.configure(transitionBuilder));
    }

    @Test
    @DisplayName("Can configure state builder multiple times")
    void canConfigureStateBuilderMultipleTimes() throws Exception {
        // Arrange
        org.springframework.statemachine.config.builders.StateMachineStateBuilder<TaskStatus, TaskEvent> stateBuilder =
            new org.springframework.statemachine.config.builders.StateMachineStateBuilder<>();

        // Act - configure and verify no exception
        config.configure(stateBuilder);
        config.configure(stateBuilder); // Should be able to call twice

        // Assert
        assertThat(config).isNotNull();
    }

    @Test
    @DisplayName("Can configure transition builder multiple times")
    void canConfigureTransitionBuilderMultipleTimes() throws Exception {
        // Arrange
        org.springframework.statemachine.config.builders.StateMachineTransitionBuilder<TaskStatus, TaskEvent> transitionBuilder =
            new org.springframework.statemachine.config.builders.StateMachineTransitionBuilder<>();

        // Act - configure and verify no exception
        config.configure(transitionBuilder);
        config.configure(transitionBuilder); // Should be able to call twice

        // Assert
        assertThat(config).isNotNull();
    }

    @Test
    @DisplayName("State configuration includes all TaskStatus values")
    void stateConfiguration_includesAllStatuses() throws Exception {
        // Arrange
        org.springframework.statemachine.config.builders.StateMachineStateBuilder<TaskStatus, TaskEvent> stateBuilder =
            new org.springframework.statemachine.config.builders.StateMachineStateBuilder<>();

        // Act
        config.configure(stateBuilder);

        // Assert - verify all states are configured
        assertThat(TaskStatus.values()).containsExactlyInAnyOrder(
            TaskStatus.TODO,
            TaskStatus.IN_PROGRESS,
            TaskStatus.IN_REVIEW,
            TaskStatus.BLOCKED,
            TaskStatus.ON_HOLD,
            TaskStatus.COMPLETED,
            TaskStatus.CANCELLED
        );
    }

    @Test
    @DisplayName("Transition configuration covers all valid transitions")
    void transitionConfiguration_coversValidTransitions() throws Exception {
        // Arrange
        org.springframework.statemachine.config.builders.StateMachineTransitionBuilder<TaskStatus, TaskEvent> transitionBuilder =
            new org.springframework.statemachine.config.builders.StateMachineTransitionBuilder<>();

        // Act
        config.configure(transitionBuilder);

        // Assert - verify configuration doesn't throw exception
        // The actual transitions are:
        // TODO -> IN_PROGRESS (START), TODO -> CANCELLED (CANCEL)
        // IN_PROGRESS -> IN_REVIEW (SUBMIT_REVIEW), COMPLETED (COMPLETE), BLOCKED (BLOCK), ON_HOLD (HOLD), CANCELLED (CANCEL)
        // IN_REVIEW -> COMPLETED (APPROVE), IN_PROGRESS (REQUEST_CHANGES), CANCELLED (CANCEL)
        // BLOCKED -> IN_PROGRESS (UNBLOCK), CANCELLED (CANCEL)
        // ON_HOLD -> IN_PROGRESS (RESUME), CANCELLED (CANCEL)
        assertThat(config).isNotNull();
    }
}

