package com.nexus.common.constants;

/**
 * Centralized Kafka Topic Names
 * All services should reference these constants instead of hardcoding topic names
 */
public final class KafkaTopics {
    
    private KafkaTopics() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // User Events
    public static final String USER_CREATED = "user.created";
    public static final String USER_UPDATED = "user.updated";
    public static final String USER_DELETED = "user.deleted";
    public static final String USER_EVENTS = "user.events";
    
    // Task Events
    public static final String TASK_CREATED = "task.created";
    public static final String TASK_UPDATED = "task.updated";
    public static final String TASK_ASSIGNED = "task.assigned";
    public static final String TASK_STATUS_CHANGED = "task.status.changed";
    public static final String TASK_EVENTS = "task.events";
    
    // File Events
    public static final String FILE_UPLOADED = "file.uploaded";
    public static final String FILE_DELETED = "file.deleted";
    
    // Notification Events
    public static final String NOTIFICATION_SENT = "notification.sent";
    public static final String NOTIFICATION_FAILED = "notification.failed";
    
    // Workflow/Saga Events
    public static final String SAGA_STARTED = "saga.started";
    public static final String SAGA_COMPLETED = "saga.completed";
    public static final String SAGA_FAILED = "saga.failed";
    public static final String SAGA_COMPENSATING = "saga.compensating";
}

