package com.nexus.search.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Seller document for Elasticsearch
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerDocument {
    
    private String id;
    private String businessName;
    private String businessType;
    private String businessEmail;
    private String businessPhone;
    private String businessAddress;
    private String status;
    private String verificationStatus;
    private Boolean kycVerified;
    private BigDecimal rating;
    private Integer totalReviews;
    private Integer totalProducts;
    private Integer totalOrders;
    private BigDecimal totalRevenue;
    private String description;
    private Instant createdAt;
}

