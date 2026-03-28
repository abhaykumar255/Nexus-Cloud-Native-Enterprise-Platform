package com.nexus.seller.dto;

import com.nexus.common.enums.SellerStatus;
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
public class SellerDto {
    
    private String id;
    private String userId;
    private String storeName;
    private String email;
    private String phoneNumber;
    private String businessName;
    private String businessType;
    private String gstNumber;
    private String panNumber;
    private String description;
    private String logoUrl;
    private String bannerUrl;
    private SellerStatus status;
    private BigDecimal commissionRate;
    private BigDecimal totalSales;
    private Long totalOrders;
    private BigDecimal rating;
    private Long totalReviews;
    private Instant verifiedAt;
    private Instant createdAt;
}

