package com.nexus.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPerformanceDto {
    
    private Long productId;
    private String productName;
    private String category;
    private Long unitsSold;
    private BigDecimal revenue;
    private Double averageRating;
}

