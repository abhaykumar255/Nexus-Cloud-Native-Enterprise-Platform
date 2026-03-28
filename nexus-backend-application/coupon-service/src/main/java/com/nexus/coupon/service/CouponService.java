package com.nexus.coupon.service;

import com.nexus.coupon.domain.entity.Coupon;
import com.nexus.coupon.domain.entity.CouponUsage;
import com.nexus.coupon.domain.repository.CouponRepository;
import com.nexus.coupon.domain.repository.CouponUsageRepository;
import com.nexus.coupon.dto.CouponDto;
import com.nexus.coupon.dto.CouponValidationResponse;
import com.nexus.coupon.dto.ValidateCouponRequest;
import com.nexus.coupon.mapper.CouponMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final CouponMapper couponMapper;

    /**
     * Create a new coupon
     */
    @Transactional
    @CacheEvict(value = "coupons", allEntries = true)
    public CouponDto createCoupon(Coupon coupon) {
        if (couponRepository.existsByCode(coupon.getCode())) {
            throw new IllegalArgumentException("Coupon code already exists: " + coupon.getCode());
        }

        Coupon savedCoupon = couponRepository.save(coupon);
        log.info("Created coupon: {}", savedCoupon.getCode());

        return couponMapper.toDto(savedCoupon);
    }

    /**
     * Get all active coupons
     */
    @Cacheable(value = "coupons", key = "'active'")
    public List<CouponDto> getActiveCoupons() {
        return couponRepository.findByStatus(Coupon.CouponStatus.ACTIVE)
                .stream()
                .map(couponMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get coupon by code
     */
    @Cacheable(value = "coupons", key = "#code")
    public CouponDto getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found: " + code));
        return couponMapper.toDto(coupon);
    }

    /**
     * Validate coupon
     */
    public CouponValidationResponse validateCoupon(ValidateCouponRequest request) {
        log.info("Validating coupon: {} for user: {}", request.getCode(), request.getUserId());

        // Find coupon
        Coupon coupon = couponRepository.findByCode(request.getCode())
                .orElse(null);

        if (coupon == null) {
            return CouponValidationResponse.builder()
                    .valid(false)
                    .message("Invalid coupon code")
                    .build();
        }

        // Check if coupon is active and within validity period
        if (!coupon.isValid()) {
            return CouponValidationResponse.builder()
                    .valid(false)
                    .code(coupon.getCode())
                    .message("Coupon is not valid or has expired")
                    .build();
        }

        // Check minimum order amount
        if (coupon.getMinOrderAmount() != null && request.getOrderAmount().compareTo(coupon.getMinOrderAmount()) < 0) {
            return CouponValidationResponse.builder()
                    .valid(false)
                    .code(coupon.getCode())
                    .message(String.format("Minimum order amount is %s", coupon.getMinOrderAmount()))
                    .build();
        }

        // Check usage limit per user
        long userUsageCount = couponUsageRepository.countByCouponIdAndUserId(coupon.getId(), request.getUserId());
        if (userUsageCount >= coupon.getUsageLimitPerUser()) {
            return CouponValidationResponse.builder()
                    .valid(false)
                    .code(coupon.getCode())
                    .message("Coupon usage limit exceeded for this user")
                    .build();
        }

        // Check first order only
        if (Boolean.TRUE.equals(coupon.getFirstOrderOnly()) && !Boolean.TRUE.equals(request.getIsFirstOrder())) {
            return CouponValidationResponse.builder()
                    .valid(false)
                    .code(coupon.getCode())
                    .message("This coupon is only valid for first orders")
                    .build();
        }

        // Check category specific
        if (coupon.getApplicableCategory() != null && !coupon.getApplicableCategory().equals(request.getCategory())) {
            return CouponValidationResponse.builder()
                    .valid(false)
                    .code(coupon.getCode())
                    .message("Coupon not applicable for this category")
                    .build();
        }

        // Calculate discount
        BigDecimal discountAmount = calculateDiscount(coupon, request.getOrderAmount());

        return CouponValidationResponse.builder()
                .valid(true)
                .couponId(coupon.getId())
                .code(coupon.getCode())
                .discountAmount(discountAmount)
                .message("Coupon applied successfully")
                .build();
    }

    /**
     * Apply coupon (record usage)
     */
    @Transactional
    @CacheEvict(value = "coupons", allEntries = true)
    public void applyCoupon(String couponId, String userId, String orderId, BigDecimal orderAmount, BigDecimal discountAmount) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));

        // Record usage
        CouponUsage usage = CouponUsage.builder()
                .couponId(couponId)
                .userId(userId)
                .orderId(orderId)
                .orderAmount(orderAmount)
                .discountAmount(discountAmount)
                .build();

        couponUsageRepository.save(usage);

        // Increment usage count
        coupon.incrementUsage();
        couponRepository.save(coupon);

        log.info("Applied coupon {} for order {}", coupon.getCode(), orderId);
    }

    /**
     * Calculate discount amount
     */
    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal orderAmount) {
        BigDecimal discount;

        if (coupon.getDiscountType() == Coupon.DiscountType.PERCENTAGE) {
            discount = orderAmount.multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discount = coupon.getDiscountValue();
        }

        // Apply max discount limit
        if (coupon.getMaxDiscountAmount() != null && discount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
            discount = coupon.getMaxDiscountAmount();
        }

        return discount;
    }
}

