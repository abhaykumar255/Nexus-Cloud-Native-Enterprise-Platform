package com.nexus.notification.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.common.dto.PaginationInfo;
import com.nexus.notification.dto.NotificationDto;
import com.nexus.notification.dto.SendNotificationRequest;
import com.nexus.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Notification REST controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    
    /**
     * Send notification
     * POST /api/v1/notifications/send
     */
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<NotificationDto>> sendNotification(
            @Valid @RequestBody SendNotificationRequest request) {
        log.info("Send notification request for user: {}", request.getUserId());
        
        NotificationDto notification = notificationService.sendNotification(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification sent successfully", notification));
    }
    
    /**
     * Get my notifications
     * GET /api/v1/notifications/my
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyNotifications(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Get my notifications request for user: {}", userId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<NotificationDto> notifications = notificationService.getUserNotifications(userId, pageable);
        
        return ResponseEntity.ok(ApiResponse.paginated(
                notifications.getContent(), 
                PaginationInfo.from(notifications)));
    }
    
    /**
     * Get unread count
     * GET /api/v1/notifications/unread/count
     */
    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @RequestHeader("X-User-Id") String userId) {
        log.info("Get unread count request for user: {}", userId);
        
        long count = notificationService.getUnreadCount(userId);
        
        return ResponseEntity.ok(ApiResponse.success(count));
    }
    
    /**
     * Mark notification as read
     * PUT /api/v1/notifications/{notificationId}/read
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<NotificationDto>> markAsRead(
            @PathVariable String notificationId,
            @RequestHeader("X-User-Id") String userId) {
        log.info("Mark as read request for notification: {}", notificationId);
        
        NotificationDto notification = notificationService.markAsRead(notificationId, userId);
        
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", notification));
    }
    
    /**
     * Mark all notifications as read
     * PUT /api/v1/notifications/read-all
     */
    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @RequestHeader("X-User-Id") String userId) {
        log.info("Mark all as read request for user: {}", userId);
        
        notificationService.markAllAsRead(userId);
        
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read", null));
    }
    
    /**
     * Delete notification
     * DELETE /api/v1/notifications/{notificationId}
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable String notificationId,
            @RequestHeader("X-User-Id") String userId) {
        log.info("Delete notification request: {}", notificationId);
        
        notificationService.deleteNotification(notificationId, userId);
        
        return ResponseEntity.ok(ApiResponse.success("Notification deleted successfully", null));
    }
}

