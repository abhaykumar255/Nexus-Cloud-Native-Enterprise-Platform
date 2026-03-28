package com.nexus.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponValidationResponse {
    private Boolean valid;
    private String couponId;
    private String code;
    private BigDecimal discountAmount;
    private String message;
}

