package com.nexus.coupon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "coupon_usage", indexes = {
        @Index(name = "idx_usage_coupon_id", columnList = "coupon_id"),
        @Index(name = "idx_usage_user_id", columnList = "user_id"),
        @Index(name = "idx_usage_order_id", columnList = "order_id"),
        @Index(name = "idx_usage_coupon_user", columnList = "coupon_id, user_id")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "coupon_id", nullable = false)
    private String couponId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "order_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderAmount;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant usedAt;
}

