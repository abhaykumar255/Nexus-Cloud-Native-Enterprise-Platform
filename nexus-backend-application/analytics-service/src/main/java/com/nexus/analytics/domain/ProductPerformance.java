package com.nexus.analytics.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Product Performance Metrics Document
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product_performance")
public class ProductPerformance {
    
    @Id
    private String id;
    
    @Indexed
    private Long productId;
    private String productName;
    private String category;
    private Long sellerId;
    
    private LocalDate date;
    
    // View Metrics
    private Long totalViews;
    private Long uniqueViews;
    
    // Cart Metrics
    private Long addedToCart;
    private Long removedFromCart;
    
    // Purchase Metrics
    private Long unitsSold;
    private BigDecimal revenue;
    private BigDecimal averagePrice;
    
    // Conversion Metrics
    private Double viewToCartRate;
    private Double cartToPurchaseRate;
    private Double overallConversionRate;
    
    // Review Metrics
    private Long totalReviews;
    private Double averageRating;
    
    // Inventory Metrics
    private Long stockLevel;
    private Long stockOuts;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

