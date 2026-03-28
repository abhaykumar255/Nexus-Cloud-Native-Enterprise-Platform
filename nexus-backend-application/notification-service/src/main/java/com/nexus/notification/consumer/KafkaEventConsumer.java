package com.nexus.notification.consumer;

import com.nexus.common.constants.KafkaTopics;
import com.nexus.notification.domain.enums.NotificationPriority;
import com.nexus.notification.domain.enums.NotificationType;
import com.nexus.notification.dto.SendNotificationRequest;
import com.nexus.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Kafka event consumer for processing domain events and sending notifications
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final NotificationService notificationService;

    @Value("${notification.messages.welcome.subject:Welcome to Nexus Platform!}")
    private String welcomeSubject;

    @Value("${notification.messages.welcome.body:Welcome %s! Your account has been created successfully.}")
    private String welcomeBodyTemplate;

    @Value("${notification.messages.task-created.subject:New Task Created}")
    private String taskCreatedSubject;

    @Value("${notification.messages.task-created.body:A new task '%s' has been created.}")
    private String taskCreatedBodyTemplate;

    @Value("${notification.messages.task-assigned.subject:Task Assigned to You}")
    private String taskAssignedSubject;

    @Value("${notification.messages.task-assigned.body:Task '%s' has been assigned to you.}")
    private String taskAssignedBodyTemplate;

    @Value("${notification.messages.task-status.subject:Task Status Changed}")
    private String taskStatusSubject;

    @Value("${notification.messages.task-status.body:Task '%s' status changed from %s to %s.}")
    private String taskStatusBodyTemplate;

    @Value("${notification.category.user:user}")
    private String userCategory;

    @Value("${notification.category.task:task}")
    private String taskCategory;

    /**
     * Consume user created events
     */
    @KafkaListener(topics = KafkaTopics.USER_CREATED, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUserCreatedEvent(Map<String, Object> event) {
        log.info("Received {} event: {}", KafkaTopics.USER_CREATED, event);
        
        try {
            String userId = (String) event.get("userId");
            String email = (String) event.get("email");
            String username = (String) event.get("username");
            
            SendNotificationRequest request = new SendNotificationRequest();
            request.setUserId(userId);
            request.setType(NotificationType.EMAIL);
            request.setPriority(NotificationPriority.HIGH);
            request.setSubject(welcomeSubject);
            request.setMessage(String.format(welcomeBodyTemplate, username));
            request.setRecipient(email);
            request.setCategory(userCategory);
            
            notificationService.sendNotification(request);
            
            log.info("Welcome notification sent to user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to process user.created event", e);
        }
    }
    
    /**
     * Consume task created events
     */
    @KafkaListener(topics = KafkaTopics.TASK_CREATED, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTaskCreatedEvent(Map<String, Object> event) {
        log.info("Received {} event: {}", KafkaTopics.TASK_CREATED, event);

        try {
            String taskId = (String) event.get("taskId");
            String title = (String) event.get("title");
            String assigneeId = (String) event.get("assigneeId");

            if (assigneeId != null) {
                SendNotificationRequest request = new SendNotificationRequest();
                request.setUserId(assigneeId);
                request.setType(NotificationType.IN_APP);
                request.setPriority(NotificationPriority.MEDIUM);
                request.setSubject(taskCreatedSubject);
                request.setMessage(String.format(taskCreatedBodyTemplate, title));
                request.setCategory(taskCategory);
                request.setMetadata(Map.of("taskId", taskId));
                
                notificationService.sendNotification(request);
                
                log.info("Task creation notification sent to user: {}", assigneeId);
            }
        } catch (Exception e) {
            log.error("Failed to process task.created event", e);
        }
    }
    
    /**
     * Consume task assigned events
     */
    @KafkaListener(topics = KafkaTopics.TASK_ASSIGNED, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTaskAssignedEvent(Map<String, Object> event) {
        log.info("Received {} event: {}", KafkaTopics.TASK_ASSIGNED, event);

        try {
            String taskId = (String) event.get("taskId");
            String title = (String) event.get("title");
            String assigneeId = (String) event.get("assigneeId");

            if (assigneeId != null) {
                SendNotificationRequest request = new SendNotificationRequest();
                request.setUserId(assigneeId);
                request.setType(NotificationType.IN_APP);
                request.setPriority(NotificationPriority.HIGH);
                request.setSubject(taskAssignedSubject);
                request.setMessage(String.format(taskAssignedBodyTemplate, title));
                request.setCategory(taskCategory);
                request.setMetadata(Map.of("taskId", taskId));
                
                notificationService.sendNotification(request);
                
                log.info("Task assignment notification sent to user: {}", assigneeId);
            }
        } catch (Exception e) {
            log.error("Failed to process task.assigned event", e);
        }
    }
    
    /**
     * Consume task status changed events
     */
    @KafkaListener(topics = "task.status.changed", groupId = "notification-service")
    public void consumeTaskStatusChangedEvent(Map<String, Object> event) {
        log.info("Received task.status.changed event: {}", event);
        
        try {
            String taskId = (String) event.get("taskId");
            String title = (String) event.get("title");
            String status = (String) event.get("status");
            String creatorId = (String) event.get("creatorId");
            
            if (creatorId != null) {
                SendNotificationRequest request = new SendNotificationRequest();
                request.setUserId(creatorId);
                request.setType(NotificationType.IN_APP);
                request.setPriority(NotificationPriority.MEDIUM);
                request.setSubject("Task Status Updated");
                request.setMessage(String.format("Task '%s' status changed to %s.", title, status));
                request.setCategory("task");
                request.setMetadata(Map.of("taskId", taskId, "status", status));
                
                notificationService.sendNotification(request);
                
                log.info("Task status change notification sent to user: {}", creatorId);
            }
        } catch (Exception e) {
            log.error("Failed to process task.status.changed event", e);
        }
    }
}

