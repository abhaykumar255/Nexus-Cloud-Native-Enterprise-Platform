package com.nexus.task.domain.enums;

/**
 * Task status enumeration for state machine
 */
public enum TaskStatus {
    TODO,           // Initial state
    IN_PROGRESS,    // Work started
    IN_REVIEW,      // Under review
    BLOCKED,        // Blocked by dependencies
    ON_HOLD,        // Temporarily paused
    COMPLETED,      // Finished successfully
    CANCELLED       // Cancelled/Abandoned
}

