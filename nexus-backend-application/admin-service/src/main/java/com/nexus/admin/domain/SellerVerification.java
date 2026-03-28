package com.nexus.admin.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Seller Verification Entity
 * Manages seller approvals and verification process
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seller_verification", indexes = {
    @Index(name = "idx_seller_id", columnList = "seller_id"),
    @Index(name = "idx_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
public class SellerVerification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private Long sellerId;
    
    @Column(nullable = false, length = 200)
    private String businessName;
    
    @Column(nullable = false, length = 100)
    private String contactEmail;
    
    @Column(length = 20)
    private String contactPhone;
    
    @Column(length = 100)
    private String businessRegistrationNumber;
    
    @Column(length = 50)
    private String taxId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VerificationStatus status; // PENDING, UNDER_REVIEW, APPROVED, REJECTED, EXPIRED
    
    @Column(columnDefinition = "TEXT")
    private String documents; // JSON array of document URLs
    
    @Column(columnDefinition = "TEXT")
    private String rejectionReason;
    
    @Column(nullable = false)
    private Integer documentCount = 0;
    
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime expiryDate;
    
    private Long reviewerId;
    
    @Column(length = 100)
    private String reviewerName;
    
    @Column(columnDefinition = "TEXT")
    private String reviewNotes;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    public enum VerificationStatus {
        PENDING,
        UNDER_REVIEW,
        APPROVED,
        REJECTED,
        EXPIRED,
        SUSPENDED
    }
}

