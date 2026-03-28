package com.nexus.analytics.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.nexus.analytics.domain.ProductPerformance;
import com.nexus.analytics.domain.SalesMetrics;
import com.nexus.analytics.domain.UserBehavior;
import com.nexus.analytics.repository.ProductPerformanceRepository;
import com.nexus.analytics.repository.SalesMetricsRepository;
import com.nexus.analytics.repository.UserBehaviorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Analytics Aggregation Service
 * Processes events and aggregates data for dashboards and reports
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsAggregationService {
    
    private final SalesMetricsRepository salesMetricsRepository;
    private final ProductPerformanceRepository productPerformanceRepository;
    private final UserBehaviorRepository userBehaviorRepository;
    
    // Order Processing
    public void processOrderCreated(JsonNode orderData) {
        log.info("Processing order created event");
        LocalDate today = LocalDate.now();
        SalesMetrics metrics = getOrCreateSalesMetrics(today);
        
        metrics.setTotalOrders(metrics.getTotalOrders() + 1);
        metrics.setPendingOrders(metrics.getPendingOrders() + 1);
        metrics.setUpdatedAt(LocalDateTime.now());
        
        salesMetricsRepository.save(metrics);
    }
    
    public void processOrderCompleted(JsonNode orderData) {
        log.info("Processing order completed event");
        LocalDate today = LocalDate.now();
        SalesMetrics metrics = getOrCreateSalesMetrics(today);
        
        BigDecimal orderAmount = new BigDecimal(orderData.get("totalAmount").asText());
        
        metrics.setCompletedOrders(metrics.getCompletedOrders() + 1);
        metrics.setPendingOrders(Math.max(0, metrics.getPendingOrders() - 1));
        metrics.setCompletedRevenue(metrics.getCompletedRevenue().add(orderAmount));
        metrics.setTotalRevenue(metrics.getTotalRevenue().add(orderAmount));
        
        // Update average order value
        if (metrics.getCompletedOrders() > 0) {
            metrics.setAverageOrderValue(
                metrics.getCompletedRevenue().divide(
                    new BigDecimal(metrics.getCompletedOrders()), 2, BigDecimal.ROUND_HALF_UP
                )
            );
        }
        
        metrics.setUpdatedAt(LocalDateTime.now());
        salesMetricsRepository.save(metrics);
    }
    
    public void processOrderCancelled(JsonNode orderData) {
        log.info("Processing order cancelled event");
        LocalDate today = LocalDate.now();
        SalesMetrics metrics = getOrCreateSalesMetrics(today);
        
        metrics.setCancelledOrders(metrics.getCancelledOrders() + 1);
        metrics.setPendingOrders(Math.max(0, metrics.getPendingOrders() - 1));
        metrics.setUpdatedAt(LocalDateTime.now());
        
        salesMetricsRepository.save(metrics);
    }
    
    // Payment Processing
    public void processPaymentSuccess(JsonNode paymentData) {
        log.info("Processing payment success event");
        LocalDate today = LocalDate.now();
        SalesMetrics metrics = getOrCreateSalesMetrics(today);
        
        metrics.setSuccessfulPayments(metrics.getSuccessfulPayments() + 1);
        
        long totalPayments = metrics.getSuccessfulPayments() + metrics.getFailedPayments();
        if (totalPayments > 0) {
            metrics.setPaymentSuccessRate(
                (metrics.getSuccessfulPayments() * 100.0) / totalPayments
            );
        }
        
        metrics.setUpdatedAt(LocalDateTime.now());
        salesMetricsRepository.save(metrics);
    }
    
    public void processPaymentFailed(JsonNode paymentData) {
        log.info("Processing payment failed event");
        LocalDate today = LocalDate.now();
        SalesMetrics metrics = getOrCreateSalesMetrics(today);
        
        metrics.setFailedPayments(metrics.getFailedPayments() + 1);
        
        long totalPayments = metrics.getSuccessfulPayments() + metrics.getFailedPayments();
        if (totalPayments > 0) {
            metrics.setPaymentSuccessRate(
                (metrics.getSuccessfulPayments() * 100.0) / totalPayments
            );
        }
        
        metrics.setUpdatedAt(LocalDateTime.now());
        salesMetricsRepository.save(metrics);
    }
    
    // Product Processing
    public void processProductViewed(JsonNode productData) {
        log.debug("Processing product viewed event");
        
        Long productId = productData.get("productId").asLong();
        Long userId = productData.has("userId") ? productData.get("userId").asLong() : null;
        
        // Track user behavior
        trackUserBehavior(userId, "PRODUCT_VIEW", "PRODUCT", productId, productData);
        
        // Update product performance
        updateProductPerformance(productId, metrics -> {
            metrics.setTotalViews(metrics.getTotalViews() + 1);
        });
    }
    
    // User Processing
    public void processUserRegistered(JsonNode userData) {
        log.info("Processing user registered event");
        LocalDate today = LocalDate.now();
        SalesMetrics metrics = getOrCreateSalesMetrics(today);
        
        metrics.setNewCustomers(metrics.getNewCustomers() + 1);
        metrics.setTotalCustomers(metrics.getTotalCustomers() + 1);
        metrics.setUpdatedAt(LocalDateTime.now());
        
        salesMetricsRepository.save(metrics);
    }
    
    // Cart Processing
    public void processCartItemAdded(JsonNode cartData) {
        log.debug("Processing cart item added event");
        
        Long userId = cartData.has("userId") ? cartData.get("userId").asLong() : null;
        Long productId = cartData.get("productId").asLong();
        
        trackUserBehavior(userId, "ADD_TO_CART", "CART", productId, cartData);
        
        updateProductPerformance(productId, metrics -> {
            metrics.setAddedToCart(metrics.getAddedToCart() + 1);
        });
    }
    
    public void processCartAbandoned(JsonNode cartData) {
        log.info("Processing cart abandoned event");
        
        Long userId = cartData.has("userId") ? cartData.get("userId").asLong() : null;
        trackUserBehavior(userId, "CART_ABANDONED", "CART", null, cartData);
    }
    
    // Review Processing
    public void processReviewCreated(JsonNode reviewData) {
        log.info("Processing review created event");
        
        Long productId = reviewData.get("productId").asLong();
        
        updateProductPerformance(productId, metrics -> {
            metrics.setTotalReviews(metrics.getTotalReviews() + 1);
        });
    }
    
    // Helper Methods
    private SalesMetrics getOrCreateSalesMetrics(LocalDate date) {
        return salesMetricsRepository.findByDate(date)
            .orElseGet(() -> createNewSalesMetrics(date));
    }
    
    private SalesMetrics createNewSalesMetrics(LocalDate date) {
        return SalesMetrics.builder()
            .date(date)
            .totalOrders(0L)
            .completedOrders(0L)
            .cancelledOrders(0L)
            .pendingOrders(0L)
            .totalRevenue(BigDecimal.ZERO)
            .completedRevenue(BigDecimal.ZERO)
            .cancelledRevenue(BigDecimal.ZERO)
            .averageOrderValue(BigDecimal.ZERO)
            .totalProductsSold(0L)
            .uniqueProducts(0L)
            .totalCustomers(0L)
            .newCustomers(0L)
            .returningCustomers(0L)
            .totalSessions(0L)
            .conversions(0L)
            .conversionRate(0.0)
            .successfulPayments(0L)
            .failedPayments(0L)
            .paymentSuccessRate(0.0)
            .activeSellers(0L)
            .totalSellers(0L)
            .totalFoodOrders(0L)
            .foodRevenue(BigDecimal.ZERO)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }
    
    private void trackUserBehavior(Long userId, String eventType, String entityType, Long entityId, JsonNode eventData) {
        Map<String, Object> data = new HashMap<>();
        eventData.fields().forEachRemaining(entry -> 
            data.put(entry.getKey(), entry.getValue().asText())
        );
        
        UserBehavior behavior = UserBehavior.builder()
            .userId(userId)
            .eventType(eventType)
            .entityType(entityType)
            .entityId(entityId)
            .eventData(data)
            .timestamp(LocalDateTime.now())
            .build();
        
        userBehaviorRepository.save(behavior);
    }
    
    private void updateProductPerformance(Long productId, java.util.function.Consumer<ProductPerformance> updater) {
        LocalDate today = LocalDate.now();
        ProductPerformance performance = productPerformanceRepository
            .findByProductIdAndDate(productId, today)
            .orElseGet(() -> createNewProductPerformance(productId, today));
        
        updater.accept(performance);
        performance.setUpdatedAt(LocalDateTime.now());
        
        productPerformanceRepository.save(performance);
    }
    
    private ProductPerformance createNewProductPerformance(Long productId, LocalDate date) {
        return ProductPerformance.builder()
            .productId(productId)
            .date(date)
            .totalViews(0L)
            .uniqueViews(0L)
            .addedToCart(0L)
            .removedFromCart(0L)
            .unitsSold(0L)
            .revenue(BigDecimal.ZERO)
            .averagePrice(BigDecimal.ZERO)
            .viewToCartRate(0.0)
            .cartToPurchaseRate(0.0)
            .overallConversionRate(0.0)
            .totalReviews(0L)
            .averageRating(0.0)
            .stockLevel(0L)
            .stockOuts(0L)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }
}

