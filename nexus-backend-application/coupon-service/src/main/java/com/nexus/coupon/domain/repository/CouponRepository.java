package com.nexus.coupon.domain.repository;

import com.nexus.coupon.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String> {

    Optional<Coupon> findByCode(String code);

    List<Coupon> findByStatus(Coupon.CouponStatus status);

    List<Coupon> findByType(Coupon.CouponType type);

    boolean existsByCode(String code);
}

