package com.nexus.coupon.mapper;

import com.nexus.coupon.domain.entity.Coupon;
import com.nexus.coupon.dto.CouponDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CouponMapper {
    
    CouponDto toDto(Coupon coupon);
    
    Coupon toEntity(CouponDto couponDto);
}

