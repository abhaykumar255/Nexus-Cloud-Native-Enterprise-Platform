package com.nexus.analytics.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Sales Metrics Document - Aggregated daily sales data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sales_metrics")
@CompoundIndex(name = "date_idx", def = "{'date': 1}")
public class SalesMetrics {
    
    @Id
    private String id;
    
    private LocalDate date;
    
    // Order Metrics
    private Long totalOrders;
    private Long completedOrders;
    private Long cancelledOrders;
    private Long pendingOrders;
    
    // Revenue Metrics
    private BigDecimal totalRevenue;
    private BigDecimal completedRevenue;
    private BigDecimal cancelledRevenue;
    private BigDecimal averageOrderValue;
    
    // Product Metrics
    private Long totalProductsSold;
    private Long uniqueProducts;
    
    // User Metrics
    private Long totalCustomers;
    private Long newCustomers;
    private Long returningCustomers;
    
    // Conversion Metrics
    private Long totalSessions;
    private Long conversions;
    private Double conversionRate;
    
    // Payment Metrics
    private Long successfulPayments;
    private Long failedPayments;
    private Double paymentSuccessRate;
    
    // Seller Metrics
    private Long activeSellers;
    private Long totalSellers;
    
    // Restaurant Metrics
    private Long totalFoodOrders;
    private BigDecimal foodRevenue;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

