package com.nexus.review.dto;

import com.nexus.review.domain.entity.Review;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {

    @NotNull(message = "Target type is required")
    private Review.TargetType targetType;

    @NotBlank(message = "Target ID is required")
    private String targetId;

    @NotBlank(message = "User ID is required")
    private String userId;

    private String userName;

    private String orderId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Comment is required")
    @Size(min = 10, max = 2000, message = "Comment must be between 10 and 2000 characters")
    private String comment;

    private List<String> images;
}

