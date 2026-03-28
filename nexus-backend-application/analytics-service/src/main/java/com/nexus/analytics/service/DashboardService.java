package com.nexus.analytics.service;

import com.nexus.analytics.domain.ProductPerformance;
import com.nexus.analytics.domain.SalesMetrics;
import com.nexus.analytics.dto.DashboardResponse;
import com.nexus.analytics.dto.ProductPerformanceDto;
import com.nexus.analytics.dto.RevenueReportDto;
import com.nexus.analytics.repository.ProductPerformanceRepository;
import com.nexus.analytics.repository.SalesMetricsRepository;
import com.nexus.analytics.repository.UserBehaviorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dashboard Service
 * Provides aggregated data for analytics dashboards and reports
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final SalesMetricsRepository salesMetricsRepository;
    private final ProductPerformanceRepository productPerformanceRepository;
    private final UserBehaviorRepository userBehaviorRepository;
    
    @Cacheable(value = "dashboard", key = "#days")
    public DashboardResponse getDashboardData(int days) {
        log.info("Generating dashboard data for last {} days", days);
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        
        List<SalesMetrics> metrics = salesMetricsRepository.findByDateBetweenOrderByDateDesc(startDate, endDate);
        
        // Calculate totals
        long totalOrders = metrics.stream().mapToLong(SalesMetrics::getTotalOrders).sum();
        long completedOrders = metrics.stream().mapToLong(SalesMetrics::getCompletedOrders).sum();
        BigDecimal totalRevenue = metrics.stream()
            .map(SalesMetrics::getTotalRevenue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long totalCustomers = metrics.stream().mapToLong(SalesMetrics::getTotalCustomers).sum();
        long newCustomers = metrics.stream().mapToLong(SalesMetrics::getNewCustomers).sum();
        
        double averageConversionRate = metrics.stream()
            .mapToDouble(SalesMetrics::getConversionRate)
            .average()
            .orElse(0.0);
        
        return DashboardResponse.builder()
            .totalOrders(totalOrders)
            .completedOrders(completedOrders)
            .totalRevenue(totalRevenue)
            .totalCustomers(totalCustomers)
            .newCustomers(newCustomers)
            .averageConversionRate(averageConversionRate)
            .build();
    }
    
    @Cacheable(value = "revenue-report", key = "#startDate + '-' + #endDate")
    public RevenueReportDto getRevenueReport(LocalDate startDate, LocalDate endDate) {
        log.info("Generating revenue report from {} to {}", startDate, endDate);
        
        List<SalesMetrics> metrics = salesMetricsRepository.findByDateBetweenOrderByDateDesc(startDate, endDate);
        
        BigDecimal totalRevenue = metrics.stream()
            .map(SalesMetrics::getCompletedRevenue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal averageOrderValue = metrics.stream()
            .map(SalesMetrics::getAverageOrderValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(new BigDecimal(metrics.size()), 2, BigDecimal.ROUND_HALF_UP);
        
        return RevenueReportDto.builder()
            .startDate(startDate)
            .endDate(endDate)
            .totalRevenue(totalRevenue)
            .averageOrderValue(averageOrderValue)
            .dailyMetrics(metrics)
            .build();
    }
    
    @Cacheable(value = "top-products", key = "#limit")
    public List<ProductPerformanceDto> getTopProducts(int limit) {
        log.info("Fetching top {} products", limit);
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        
        List<ProductPerformance> topProducts = productPerformanceRepository
            .findTopRevenueProducts(startDate, endDate);
        
        return topProducts.stream()
            .limit(limit)
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    @Cacheable(value = "conversion-funnel")
    public DashboardResponse.ConversionFunnel getConversionFunnel(int days) {
        log.info("Calculating conversion funnel for last {} days", days);
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        
        List<SalesMetrics> metrics = salesMetricsRepository.findByDateBetweenOrderByDateDesc(startDate, endDate);
        
        long totalSessions = metrics.stream().mapToLong(SalesMetrics::getTotalSessions).sum();
        long conversions = metrics.stream().mapToLong(SalesMetrics::getConversions).sum();
        
        double conversionRate = totalSessions > 0 ? (conversions * 100.0) / totalSessions : 0.0;
        
        return DashboardResponse.ConversionFunnel.builder()
            .totalSessions(totalSessions)
            .conversions(conversions)
            .conversionRate(conversionRate)
            .build();
    }
    
    private ProductPerformanceDto convertToDto(ProductPerformance performance) {
        return ProductPerformanceDto.builder()
            .productId(performance.getProductId())
            .productName(performance.getProductName())
            .category(performance.getCategory())
            .unitsSold(performance.getUnitsSold())
            .revenue(performance.getRevenue())
            .averageRating(performance.getAverageRating())
            .build();
    }
}

