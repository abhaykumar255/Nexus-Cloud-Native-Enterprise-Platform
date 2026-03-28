package com.nexus.review.service;

import com.nexus.review.domain.entity.Review;
import com.nexus.review.domain.entity.ReviewHelpfulness;
import com.nexus.review.domain.repository.ReviewHelpfulnessRepository;
import com.nexus.review.domain.repository.ReviewRepository;
import com.nexus.review.dto.CreateReviewRequest;
import com.nexus.review.dto.ReviewDto;
import com.nexus.review.dto.ReviewStatsDto;
import com.nexus.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewHelpfulnessRepository helpfulnessRepository;
    private final ReviewMapper reviewMapper;

    /**
     * Create a new review
     */
    @Transactional
    @CacheEvict(value = "reviews", allEntries = true)
    public ReviewDto createReview(CreateReviewRequest request) {
        log.info("Creating review for {} {}", request.getTargetType(), request.getTargetId());

        // Check if user already reviewed
        if (reviewRepository.existsByTargetTypeAndTargetIdAndUserId(
                request.getTargetType(), request.getTargetId(), request.getUserId())) {
            throw new IllegalArgumentException("User has already reviewed this item");
        }

        Review review = Review.builder()
                .targetType(request.getTargetType())
                .targetId(request.getTargetId())
                .userId(request.getUserId())
                .userName(request.getUserName())
                .orderId(request.getOrderId())
                .rating(request.getRating())
                .title(request.getTitle())
                .comment(request.getComment())
                .images(request.getImages())
                .verifiedPurchase(request.getOrderId() != null)
                .status(Review.ReviewStatus.PENDING)
                .build();

        Review savedReview = reviewRepository.save(review);
        log.info("Review created: {}", savedReview.getId());

        // TODO: Publish REVIEW_CREATED event to Kafka for moderation

        return reviewMapper.toDto(savedReview);
    }

    /**
     * Get reviews for a target (product, restaurant, etc.)
     */
    @Cacheable(value = "reviews", key = "#targetType + '_' + #targetId + '_' + #pageable.pageNumber")
    public Page<ReviewDto> getReviewsForTarget(Review.TargetType targetType, String targetId, Pageable pageable) {
        return reviewRepository.findByTargetTypeAndTargetIdAndStatus(
                        targetType, targetId, Review.ReviewStatus.APPROVED, pageable)
                .map(reviewMapper::toDto);
    }

    /**
     * Get user's reviews
     */
    public Page<ReviewDto> getUserReviews(String userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
                .map(reviewMapper::toDto);
    }

    /**
     * Get review stats for a target
     */
    @Cacheable(value = "review-stats", key = "#targetType + '_' + #targetId")
    public ReviewStatsDto getReviewStats(Review.TargetType targetType, String targetId) {
        long totalReviews = reviewRepository.countByTargetTypeAndTargetIdAndStatus(
                targetType, targetId, Review.ReviewStatus.APPROVED);

        Double avgRating = reviewRepository.calculateAverageRating(
                targetType, targetId, Review.ReviewStatus.APPROVED);

        BigDecimal averageRating = avgRating != null 
                ? BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;

        return ReviewStatsDto.builder()
                .targetId(targetId)
                .totalReviews(totalReviews)
                .averageRating(averageRating)
                .build();
    }

    /**
     * Mark review as helpful/not helpful
     */
    @Transactional
    @CacheEvict(value = "reviews", allEntries = true)
    public void markReviewHelpfulness(String reviewId, String userId, boolean helpful) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        // Check if user already marked this review
        if (helpfulnessRepository.existsByReviewIdAndUserId(reviewId, userId)) {
            throw new IllegalArgumentException("User has already marked this review");
        }

        ReviewHelpfulness helpfulness = ReviewHelpfulness.builder()
                .reviewId(reviewId)
                .userId(userId)
                .helpful(helpful)
                .build();

        helpfulnessRepository.save(helpfulness);

        // Update counts
        if (helpful) {
            review.setHelpfulCount(review.getHelpfulCount() + 1);
        } else {
            review.setUnhelpfulCount(review.getUnhelpfulCount() + 1);
        }

        reviewRepository.save(review);
    }

    /**
     * Approve review (moderation)
     */
    @Transactional
    @CacheEvict(value = {"reviews", "review-stats"}, allEntries = true)
    public void approveReview(String reviewId, String moderatorId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        review.setStatus(Review.ReviewStatus.APPROVED);
        review.setModeratedBy(moderatorId);
        review.setModeratedAt(java.time.Instant.now());

        reviewRepository.save(review);
        log.info("Review approved: {}", reviewId);

        // TODO: Publish REVIEW_APPROVED event to update product/restaurant ratings
    }

    /**
     * Reject review (moderation)
     */
    @Transactional
    @CacheEvict(value = "reviews", allEntries = true)
    public void rejectReview(String reviewId, String moderatorId, String notes) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        review.setStatus(Review.ReviewStatus.REJECTED);
        review.setModeratedBy(moderatorId);
        review.setModerationNotes(notes);
        review.setModeratedAt(java.time.Instant.now());

        reviewRepository.save(review);
        log.info("Review rejected: {}", reviewId);
    }
}

