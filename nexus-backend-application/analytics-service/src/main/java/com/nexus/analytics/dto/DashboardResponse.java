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
public class DashboardResponse {
    
    private Long totalOrders;
    private Long completedOrders;
    private BigDecimal totalRevenue;
    private Long totalCustomers;
    private Long newCustomers;
    private Double averageConversionRate;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversionFunnel {
        private Long totalSessions;
        private Long conversions;
        private Double conversionRate;
    }
}

