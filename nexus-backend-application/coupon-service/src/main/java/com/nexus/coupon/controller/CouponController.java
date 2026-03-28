package com.nexus.coupon.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.coupon.dto.CouponDto;
import com.nexus.coupon.dto.CouponValidationResponse;
import com.nexus.coupon.dto.ValidateCouponRequest;
import com.nexus.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Slf4j
public class CouponController {

    private final CouponService couponService;

    /**
     * Get all active coupons
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CouponDto>>> getActiveCoupons() {
        log.info("Fetching active coupons");
        List<CouponDto> coupons = couponService.getActiveCoupons();
        return ResponseEntity.ok(ApiResponse.success("Active coupons retrieved successfully", coupons));
    }

    /**
     * Get coupon by code
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<CouponDto>> getCouponByCode(@PathVariable String code) {
        log.info("Fetching coupon: {}", code);
        CouponDto coupon = couponService.getCouponByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Coupon retrieved successfully", coupon));
    }

    /**
     * Validate coupon
     */
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<CouponValidationResponse>> validateCoupon(
            @Valid @RequestBody ValidateCouponRequest request) {
        log.info("Validating coupon: {}", request.getCode());
        CouponValidationResponse response = couponService.validateCoupon(request);
        
        if (response.getValid()) {
            return ResponseEntity.ok(ApiResponse.success(response.getMessage(), response));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(response.getMessage()));
        }
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Coupon Service is running");
    }
}

