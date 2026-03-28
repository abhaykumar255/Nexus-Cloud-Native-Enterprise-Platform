package com.nexus.review.domain.repository;

import com.nexus.review.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    // Find by target
    Page<Review> findByTargetTypeAndTargetIdAndStatus(
            Review.TargetType targetType, 
            String targetId, 
            Review.ReviewStatus status, 
            Pageable pageable);

    // Find by user
    Page<Review> findByUserId(String userId, Pageable pageable);

    // Check if user already reviewed
    boolean existsByTargetTypeAndTargetIdAndUserId(
            Review.TargetType targetType, 
            String targetId, 
            String userId);

    // Get user's review for specific target
    Optional<Review> findByTargetTypeAndTargetIdAndUserId(
            Review.TargetType targetType, 
            String targetId, 
            String userId);

    // Count reviews for target
    long countByTargetTypeAndTargetIdAndStatus(
            Review.TargetType targetType, 
            String targetId, 
            Review.ReviewStatus status);

    // Calculate average rating
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.targetType = :targetType AND r.targetId = :targetId AND r.status = :status")
    Double calculateAverageRating(
            @Param("targetType") Review.TargetType targetType,
            @Param("targetId") String targetId,
            @Param("status") Review.ReviewStatus status);

    // Find pending reviews for moderation
    Page<Review> findByStatus(Review.ReviewStatus status, Pageable pageable);

    // Find flagged reviews
    Page<Review> findByStatusAndReportedCountGreaterThan(
            Review.ReviewStatus status, 
            Integer reportedCount, 
            Pageable pageable);
}

