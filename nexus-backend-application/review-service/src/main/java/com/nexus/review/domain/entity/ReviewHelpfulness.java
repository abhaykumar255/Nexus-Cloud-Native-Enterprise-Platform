package com.nexus.review.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "review_helpfulness", indexes = {
        @Index(name = "idx_helpfulness_review_user", columnList = "review_id, user_id", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewHelpfulness {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "review_id", nullable = false)
    private String reviewId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private Boolean helpful; // true = helpful, false = not helpful

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}

