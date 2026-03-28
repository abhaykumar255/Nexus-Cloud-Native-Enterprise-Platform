package com.nexus.analytics.controller;

import com.nexus.analytics.dto.DashboardResponse;
import com.nexus.analytics.dto.ProductPerformanceDto;
import com.nexus.analytics.dto.RevenueReportDto;
import com.nexus.analytics.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Analytics REST Controller
 * Provides endpoints for dashboards, reports, and business intelligence
 */
@Slf4j
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    
    private final DashboardService dashboardService;
    
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(
            @RequestParam(defaultValue = "30") int days
    ) {
        log.info("GET /api/analytics/dashboard - days: {}", days);
        DashboardResponse response = dashboardService.getDashboardData(days);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/revenue/report")
    public ResponseEntity<RevenueReportDto> getRevenueReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        log.info("GET /api/analytics/revenue/report - startDate: {}, endDate: {}", startDate, endDate);
        RevenueReportDto report = dashboardService.getRevenueReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/products/top")
    public ResponseEntity<List<ProductPerformanceDto>> getTopProducts(
            @RequestParam(defaultValue = "10") int limit
    ) {
        log.info("GET /api/analytics/products/top - limit: {}", limit);
        List<ProductPerformanceDto> topProducts = dashboardService.getTopProducts(limit);
        return ResponseEntity.ok(topProducts);
    }
    
    @GetMapping("/conversion/funnel")
    public ResponseEntity<DashboardResponse.ConversionFunnel> getConversionFunnel(
            @RequestParam(defaultValue = "30") int days
    ) {
        log.info("GET /api/analytics/conversion/funnel - days: {}", days);
        DashboardResponse.ConversionFunnel funnel = dashboardService.getConversionFunnel(days);
        return ResponseEntity.ok(funnel);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Analytics Service is running");
    }
}

