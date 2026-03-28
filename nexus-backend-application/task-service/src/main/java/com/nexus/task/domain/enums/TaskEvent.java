package com.nexus.task.domain.enums;

/**
 * Task state machine events
 */
public enum TaskEvent {
    START,          // TODO -> IN_PROGRESS
    SUBMIT_REVIEW,  // IN_PROGRESS -> IN_REVIEW
    APPROVE,        // IN_REVIEW -> COMPLETED
    REQUEST_CHANGES,// IN_REVIEW -> IN_PROGRESS
    BLOCK,          // * -> BLOCKED
    UNBLOCK,        // BLOCKED -> previous state
    HOLD,           // * -> ON_HOLD
    RESUME,         // ON_HOLD -> IN_PROGRESS
    COMPLETE,       // IN_PROGRESS -> COMPLETED
    CANCEL          // * -> CANCELLED
}

