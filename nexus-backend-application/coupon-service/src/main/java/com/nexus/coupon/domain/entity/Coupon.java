package com.nexus.coupon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "coupons", indexes = {
        @Index(name = "idx_coupon_code", columnList = "code", unique = true),
        @Index(name = "idx_coupon_type", columnList = "type"),
        @Index(name = "idx_coupon_status", columnList = "status"),
        @Index(name = "idx_coupon_valid_dates", columnList = "validFrom, validUntil")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CouponType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private DiscountType discountType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(precision = 10, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal minOrderAmount;

    @Column(nullable = false)
    @Builder.Default
    private Integer usageLimit = 0; // 0 means unlimited

    @Column(nullable = false)
    @Builder.Default
    private Integer usageLimitPerUser = 1;

    @Column(nullable = false)
    @Builder.Default
    private Integer usedCount = 0;

    @Column(nullable = false)
    private Instant validFrom;

    @Column(nullable = false)
    private Instant validUntil;

    @Column(length = 100)
    private String applicableCategory;

    @Column(length = 100)
    private String applicableProductType;

    @Column(name = "applicable_seller_id")
    private String applicableSellerId;

    @Column(name = "first_order_only")
    @Builder.Default
    private Boolean firstOrderOnly = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private CouponStatus status = CouponStatus.ACTIVE;

    @Column(name = "created_by")
    private String createdBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Enums
    public enum CouponType {
        GENERAL,          // Available to all
        USER_SPECIFIC,    // Targeted to specific users
        FIRST_ORDER,      // New user discount
        CATEGORY_SPECIFIC,// Specific category discount
        SELLER_SPECIFIC,  // Seller promotion
        CASHBACK         // Cashback offer
    }

    public enum DiscountType {
        PERCENTAGE,  // % discount
        FIXED_AMOUNT // Fixed amount discount
    }

    public enum CouponStatus {
        ACTIVE,
        INACTIVE,
        EXPIRED,
        EXHAUSTED
    }

    /**
     * Check if coupon is currently valid
     */
    public boolean isValid() {
        Instant now = Instant.now();
        return status == CouponStatus.ACTIVE
                && now.isAfter(validFrom)
                && now.isBefore(validUntil)
                && (usageLimit == 0 || usedCount < usageLimit);
    }

    /**
     * Increment usage count
     */
    public void incrementUsage() {
        this.usedCount++;
        if (usageLimit > 0 && usedCount >= usageLimit) {
            this.status = CouponStatus.EXHAUSTED;
        }
    }
}

