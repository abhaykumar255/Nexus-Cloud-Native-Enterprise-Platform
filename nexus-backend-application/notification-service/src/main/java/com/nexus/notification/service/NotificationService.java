package com.nexus.notification.service;

import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.notification.config.NotificationProperties;
import com.nexus.notification.domain.entity.Notification;
import com.nexus.notification.domain.enums.NotificationStatus;
import com.nexus.notification.domain.enums.NotificationType;
import com.nexus.notification.domain.repository.NotificationRepository;
import com.nexus.notification.dto.NotificationDto;
import com.nexus.notification.dto.SendNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Notification service for managing notifications
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final WebSocketNotificationService webSocketService;
    private final NotificationProperties properties;
    
    /**
     * Send notification
     */
    public NotificationDto sendNotification(SendNotificationRequest request) {
        log.info("Sending notification to user: {} via: {}", request.getUserId(), request.getType());
        
        // Create notification record
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .status(NotificationStatus.PENDING)
                .priority(request.getPriority())
                .subject(request.getSubject())
                .message(request.getMessage())
                .template(request.getTemplate())
                .templateData(request.getTemplateData())
                .recipient(request.getRecipient())
                .category(request.getCategory())
                .metadata(request.getMetadata())
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plus(properties.getExpiration().getDefaultExpirationDays(), ChronoUnit.DAYS))
                .retryCount(0)
                .build();
        
        notification = notificationRepository.save(notification);
        
        // Dispatch notification based on type
        try {
            dispatchNotification(notification);
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(Instant.now());
        } catch (Exception e) {
            log.error("Failed to send notification: {}", notification.getId(), e);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
        }
        
        notification = notificationRepository.save(notification);
        
        return toDto(notification);
    }
    
    /**
     * Dispatch notification based on type
     */
    private void dispatchNotification(Notification notification) {
        switch (notification.getType()) {
            case EMAIL -> {
                if (notification.getTemplate() != null) {
                    emailService.sendTemplateEmail(
                            notification.getRecipient(),
                            notification.getSubject(),
                            notification.getTemplate(),
                            notification.getTemplateData()
                    );
                } else {
                    emailService.sendSimpleEmail(
                            notification.getRecipient(),
                            notification.getSubject(),
                            notification.getMessage()
                    );
                }
            }
            case IN_APP, WEBSOCKET -> webSocketService.sendNotification(notification.getUserId(), notification);
            case SMS -> log.warn("SMS notifications not yet implemented");
            case PUSH -> log.warn("Push notifications not yet implemented");
        }
    }
    
    /**
     * Get user notifications
     */
    public Page<NotificationDto> getUserNotifications(String userId, Pageable pageable) {
        log.debug("Fetching notifications for user: {}", userId);
        return notificationRepository.findByUserId(userId, pageable).map(this::toDto);
    }
    
    /**
     * Get unread notifications count
     */
    public long getUnreadCount(String userId) {
        return notificationRepository.countByUserIdAndStatus(userId, NotificationStatus.SENT);
    }
    
    /**
     * Mark notification as read
     */
    public NotificationDto markAsRead(String notificationId, String userId) {
        log.info("Marking notification as read: {}", notificationId);
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", notificationId));
        
        if (!notification.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Notification does not belong to user");
        }
        
        notification.setStatus(NotificationStatus.READ);
        notification.setReadAt(Instant.now());
        
        notification = notificationRepository.save(notification);
        
        return toDto(notification);
    }
    
    /**
     * Mark all notifications as read for user
     */
    public void markAllAsRead(String userId) {
        log.info("Marking all notifications as read for user: {}", userId);
        
        Page<Notification> unreadNotifications = notificationRepository.findByUserIdAndStatus(
                userId, NotificationStatus.SENT, Pageable.unpaged());
        
        unreadNotifications.forEach(notification -> {
            notification.setStatus(NotificationStatus.READ);
            notification.setReadAt(Instant.now());
        });
        
        notificationRepository.saveAll(unreadNotifications);
    }
    
    /**
     * Delete notification
     */
    public void deleteNotification(String notificationId, String userId) {
        log.info("Deleting notification: {}", notificationId);
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", notificationId));
        
        if (!notification.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Notification does not belong to user");
        }
        
        notificationRepository.delete(notification);
    }
    
    /**
     * Convert entity to DTO
     */
    private NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .type(notification.getType())
                .status(notification.getStatus())
                .priority(notification.getPriority())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .template(notification.getTemplate())
                .templateData(notification.getTemplateData())
                .recipient(notification.getRecipient())
                .sender(notification.getSender())
                .category(notification.getCategory())
                .metadata(notification.getMetadata())
                .createdAt(notification.getCreatedAt())
                .sentAt(notification.getSentAt())
                .deliveredAt(notification.getDeliveredAt())
                .readAt(notification.getReadAt())
                .build();
    }
}

