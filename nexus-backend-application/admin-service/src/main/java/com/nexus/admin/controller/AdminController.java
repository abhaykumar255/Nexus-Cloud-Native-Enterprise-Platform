package com.nexus.admin.controller;

import com.nexus.admin.domain.ContentModeration;
import com.nexus.admin.domain.ContentModeration.EntityType;
import com.nexus.admin.domain.ContentModeration.ModerationAction;
import com.nexus.admin.domain.SellerVerification;
import com.nexus.admin.domain.SellerVerification.VerificationStatus;
import com.nexus.admin.domain.UserModeration;
import com.nexus.admin.domain.UserModeration.ModerationStatus;
import com.nexus.admin.dto.ContentModerationRequest;
import com.nexus.admin.dto.SellerVerificationRequest;
import com.nexus.admin.dto.UserModerationRequest;
import com.nexus.admin.service.ContentModerationService;
import com.nexus.admin.service.SellerVerificationService;
import com.nexus.admin.service.UserModerationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin Controller
 * Platform administration and governance endpoints
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final UserModerationService userModerationService;
    private final SellerVerificationService sellerVerificationService;
    private final ContentModerationService contentModerationService;
    
    // ========== User Moderation Endpoints ==========
    
    @PostMapping("/users/moderate")
    public ResponseEntity<UserModeration> moderateUser(@Valid @RequestBody UserModerationRequest request) {
        log.info("POST /api/admin/users/moderate - userId: {}", request.getUserId());
        UserModeration moderation = userModerationService.moderateUser(request);
        return ResponseEntity.ok(moderation);
    }
    
    @PostMapping("/users/{userId}/suspend")
    public ResponseEntity<UserModeration> suspendUser(
            @PathVariable Long userId,
            @RequestParam String reason,
            @RequestParam(defaultValue = "30") Integer days,
            @RequestParam Long moderatorId) {
        log.info("POST /api/admin/users/{}/suspend - days: {}", userId, days);
        UserModeration moderation = userModerationService.suspendUser(userId, reason, days, moderatorId);
        return ResponseEntity.ok(moderation);
    }
    
    @PostMapping("/users/{userId}/ban")
    public ResponseEntity<UserModeration> banUser(
            @PathVariable Long userId,
            @RequestParam String reason,
            @RequestParam Long moderatorId) {
        log.info("POST /api/admin/users/{}/ban", userId);
        UserModeration moderation = userModerationService.banUser(userId, reason, moderatorId);
        return ResponseEntity.ok(moderation);
    }
    
    @PostMapping("/users/{userId}/reinstate")
    public ResponseEntity<UserModeration> reinstateUser(
            @PathVariable Long userId,
            @RequestParam Long moderatorId) {
        log.info("POST /api/admin/users/{}/reinstate", userId);
        UserModeration moderation = userModerationService.reinstateUser(userId, moderatorId);
        return ResponseEntity.ok(moderation);
    }
    
    @GetMapping("/users/moderation")
    public ResponseEntity<List<UserModeration>> getUserModerations(
            @RequestParam(required = false) ModerationStatus status) {
        log.info("GET /api/admin/users/moderation - status: {}", status);
        List<UserModeration> moderations = status != null 
            ? userModerationService.getModerationsByStatus(status)
            : userModerationService.getHighViolationUsers(3);
        return ResponseEntity.ok(moderations);
    }
    
    @GetMapping("/users/{userId}/moderation")
    public ResponseEntity<UserModeration> getUserModeration(@PathVariable Long userId) {
        log.info("GET /api/admin/users/{}/moderation", userId);
        UserModeration moderation = userModerationService.getUserModeration(userId);
        return ResponseEntity.ok(moderation);
    }
    
    // ========== Seller Verification Endpoints ==========
    
    @PostMapping("/sellers/verification/submit")
    public ResponseEntity<SellerVerification> submitSellerVerification(
            @Valid @RequestBody SellerVerificationRequest request) {
        log.info("POST /api/admin/sellers/verification/submit - sellerId: {}", request.getSellerId());
        SellerVerification verification = sellerVerificationService.submitVerification(request);
        return ResponseEntity.ok(verification);
    }
    
    @PostMapping("/sellers/{sellerId}/approve")
    public ResponseEntity<SellerVerification> approveSeller(
            @PathVariable Long sellerId,
            @RequestParam Long reviewerId,
            @RequestParam String reviewerName,
            @RequestParam(defaultValue = "365") Integer expiryDays) {
        log.info("POST /api/admin/sellers/{}/approve", sellerId);
        SellerVerification verification = sellerVerificationService.approveSeller(
            sellerId, reviewerId, reviewerName, expiryDays);
        return ResponseEntity.ok(verification);
    }
    
    @PostMapping("/sellers/{sellerId}/reject")
    public ResponseEntity<SellerVerification> rejectSeller(
            @PathVariable Long sellerId,
            @RequestParam String reason,
            @RequestParam Long reviewerId,
            @RequestParam String reviewerName) {
        log.info("POST /api/admin/sellers/{}/reject", sellerId);
        SellerVerification verification = sellerVerificationService.rejectSeller(
            sellerId, reason, reviewerId, reviewerName);
        return ResponseEntity.ok(verification);
    }
    
    @GetMapping("/sellers/verification")
    public ResponseEntity<List<SellerVerification>> getSellerVerifications(
            @RequestParam(required = false) VerificationStatus status) {
        log.info("GET /api/admin/sellers/verification - status: {}", status);
        List<SellerVerification> verifications = status != null
            ? sellerVerificationService.getVerificationsByStatus(status)
            : sellerVerificationService.getPendingVerifications();
        return ResponseEntity.ok(verifications);
    }
    
    @GetMapping("/sellers/{sellerId}/verification")
    public ResponseEntity<SellerVerification> getSellerVerification(@PathVariable Long sellerId) {
        log.info("GET /api/admin/sellers/{}/verification", sellerId);
        SellerVerification verification = sellerVerificationService.getSellerVerification(sellerId);
        return ResponseEntity.ok(verification);
    }
    
    // ========== Content Moderation Endpoints ==========
    
    @PostMapping("/content/report")
    public ResponseEntity<ContentModeration> reportContent(@Valid @RequestBody ContentModerationRequest request) {
        log.info("POST /api/admin/content/report - {}: {}", request.getEntityType(), request.getEntityId());
        ContentModeration moderation = contentModerationService.reportContent(request);
        return ResponseEntity.ok(moderation);
    }
    
    @PostMapping("/content/{moderationId}/review")
    public ResponseEntity<ContentModeration> reviewContent(
            @PathVariable Long moderationId,
            @RequestParam ModerationAction action,
            @RequestParam Long moderatorId,
            @RequestParam String moderatorName,
            @RequestParam(required = false) String notes) {
        log.info("POST /api/admin/content/{}/review - action: {}", moderationId, action);
        ContentModeration moderation = contentModerationService.reviewContent(
            moderationId, action, moderatorId, moderatorName, notes);
        return ResponseEntity.ok(moderation);
    }
    
    @GetMapping("/content/reports")
    public ResponseEntity<List<ContentModeration>> getContentReports(
            @RequestParam(required = false) EntityType entityType,
            @RequestParam(required = false) Long entityId) {
        log.info("GET /api/admin/content/reports");
        List<ContentModeration> reports = entityType != null && entityId != null
            ? contentModerationService.getReportsByEntityType(entityType, entityId)
            : contentModerationService.getPendingReports();
        return ResponseEntity.ok(reports);
    }
    
    @GetMapping("/content/reports/high-priority")
    public ResponseEntity<List<ContentModeration>> getHighPriorityReports(
            @RequestParam(defaultValue = "5") Integer threshold) {
        log.info("GET /api/admin/content/reports/high-priority");
        List<ContentModeration> reports = contentModerationService.getHighPriorityReports(threshold);
        return ResponseEntity.ok(reports);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Admin Service is running");
    }
}

