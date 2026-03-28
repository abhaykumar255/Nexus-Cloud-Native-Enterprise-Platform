package com.nexus.review.dto;

import com.nexus.review.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private String id;
    private Review.TargetType targetType;
    private String targetId;
    private String userId;
    private String userName;
    private String orderId;
    private Integer rating;
    private String title;
    private String comment;
    private List<String> images;
    private Boolean verifiedPurchase;
    private Review.ReviewStatus status;
    private Integer helpfulCount;
    private Integer unhelpfulCount;
    private Instant createdAt;
}

