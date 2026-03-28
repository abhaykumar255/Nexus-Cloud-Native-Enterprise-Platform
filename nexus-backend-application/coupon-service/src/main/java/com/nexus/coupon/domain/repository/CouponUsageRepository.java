package com.nexus.coupon.domain.repository;

import com.nexus.coupon.domain.entity.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, String> {

    List<CouponUsage> findByCouponId(String couponId);

    List<CouponUsage> findByUserId(String userId);

    List<CouponUsage> findByCouponIdAndUserId(String couponId, String userId);

    long countByCouponIdAndUserId(String couponId, String userId);
}

