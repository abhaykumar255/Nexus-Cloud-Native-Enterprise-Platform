package com.nexus.review.domain.repository;

import com.nexus.review.domain.entity.ReviewHelpfulness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewHelpfulnessRepository extends JpaRepository<ReviewHelpfulness, String> {

    Optional<ReviewHelpfulness> findByReviewIdAndUserId(String reviewId, String userId);

    boolean existsByReviewIdAndUserId(String reviewId, String userId);

    long countByReviewIdAndHelpful(String reviewId, Boolean helpful);
}

