package com.nexus.coupon.dto;

import com.nexus.coupon.domain.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {
    private String id;
    private String code;
    private String name;
    private String description;
    private Coupon.CouponType type;
    private Coupon.DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscountAmount;
    private BigDecimal minOrderAmount;
    private Integer usageLimit;
    private Integer usageLimitPerUser;
    private Integer usedCount;
    private Instant validFrom;
    private Instant validUntil;
    private String applicableCategory;
    private String applicableProductType;
    private String applicableSellerId;
    private Boolean firstOrderOnly;
    private Coupon.CouponStatus status;
    private Instant createdAt;
}

