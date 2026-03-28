package com.nexus.review.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.review.domain.entity.Review;
import com.nexus.review.dto.CreateReviewRequest;
import com.nexus.review.dto.ReviewDto;
import com.nexus.review.dto.ReviewStatsDto;
import com.nexus.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Create a new review
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(
            @Valid @RequestBody CreateReviewRequest request) {
        log.info("Creating review for {} {}", request.getTargetType(), request.getTargetId());
        ReviewDto review = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Review submitted for moderation", review));
    }

    /**
     * Get reviews for a target (product, restaurant, etc.)
     */
    @GetMapping("/{targetType}/{targetId}")
    public ResponseEntity<ApiResponse<Page<ReviewDto>>> getReviewsForTarget(
            @PathVariable Review.TargetType targetType,
            @PathVariable String targetId,
            Pageable pageable) {
        log.info("Fetching reviews for {} {}", targetType, targetId);
        Page<ReviewDto> reviews = reviewService.getReviewsForTarget(targetType, targetId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Reviews retrieved successfully", reviews));
    }

    /**
     * Get user's reviews
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<ReviewDto>>> getUserReviews(
            @PathVariable String userId,
            Pageable pageable) {
        log.info("Fetching reviews for user {}", userId);
        Page<ReviewDto> reviews = reviewService.getUserReviews(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success("User reviews retrieved successfully", reviews));
    }

    /**
     * Get review stats for a target
     */
    @GetMapping("/{targetType}/{targetId}/stats")
    public ResponseEntity<ApiResponse<ReviewStatsDto>> getReviewStats(
            @PathVariable Review.TargetType targetType,
            @PathVariable String targetId) {
        log.info("Fetching review stats for {} {}", targetType, targetId);
        ReviewStatsDto stats = reviewService.getReviewStats(targetType, targetId);
        return ResponseEntity.ok(ApiResponse.success("Review stats retrieved successfully", stats));
    }

    /**
     * Mark review as helpful/not helpful
     */
    @PostMapping("/{reviewId}/helpful")
    public ResponseEntity<ApiResponse<Void>> markHelpful(
            @PathVariable String reviewId,
            @RequestParam String userId,
            @RequestParam boolean helpful) {
        log.info("Marking review {} as {} by user {}", reviewId, helpful ? "helpful" : "not helpful", userId);
        reviewService.markReviewHelpfulness(reviewId, userId, helpful);
        return ResponseEntity.ok(ApiResponse.success("Review marked successfully", null));
    }

    /**
     * Approve review (moderation endpoint)
     */
    @PutMapping("/{reviewId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveReview(
            @PathVariable String reviewId,
            @RequestParam String moderatorId) {
        log.info("Approving review {} by moderator {}", reviewId, moderatorId);
        reviewService.approveReview(reviewId, moderatorId);
        return ResponseEntity.ok(ApiResponse.success("Review approved successfully", null));
    }

    /**
     * Reject review (moderation endpoint)
     */
    @PutMapping("/{reviewId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectReview(
            @PathVariable String reviewId,
            @RequestParam String moderatorId,
            @RequestParam String notes) {
        log.info("Rejecting review {} by moderator {}", reviewId, moderatorId);
        reviewService.rejectReview(reviewId, moderatorId, notes);
        return ResponseEntity.ok(ApiResponse.success("Review rejected successfully", null));
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Review Service is running");
    }
}

