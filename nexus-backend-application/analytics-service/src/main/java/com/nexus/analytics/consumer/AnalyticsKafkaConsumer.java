package com.nexus.analytics.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.analytics.service.AnalyticsAggregationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Kafka Consumer for Analytics Events
 * Consumes events from all services for real-time analytics
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsKafkaConsumer {
    
    private final AnalyticsAggregationService aggregationService;
    private final ObjectMapper objectMapper;
    
    // Order Events
    @KafkaListener(topics = "order.created", groupId = "analytics-service-group")
    public void consumeOrderCreated(@Payload String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Received event from topic {}: {}", topic, message);
        try {
            JsonNode orderData = objectMapper.readTree(message);
            aggregationService.processOrderCreated(orderData);
        } catch (Exception e) {
            log.error("Error processing order.created event: {}", e.getMessage(), e);
        }
    }
    
    @KafkaListener(topics = "order.completed", groupId = "analytics-service-group")
    public void consumeOrderCompleted(@Payload String message) {
        log.info("Received order.completed event");
        try {
            JsonNode orderData = objectMapper.readTree(message);
            aggregationService.processOrderCompleted(orderData);
        } catch (Exception e) {
            log.error("Error processing order.completed event: {}", e.getMessage(), e);
        }
    }
    
    @KafkaListener(topics = "order.cancelled", groupId = "analytics-service-group")
    public void consumeOrderCancelled(@Payload String message) {
        log.info("Received order.cancelled event");
        try {
            JsonNode orderData = objectMapper.readTree(message);
            aggregationService.processOrderCancelled(orderData);
        } catch (Exception e) {
            log.error("Error processing order.cancelled event: {}", e.getMessage(), e);
        }
    }
    
    // Payment Events
    @KafkaListener(topics = "payment.success", groupId = "analytics-service-group")
    public void consumePaymentSuccess(@Payload String message) {
        log.info("Received payment.success event");
        try {
            JsonNode paymentData = objectMapper.readTree(message);
            aggregationService.processPaymentSuccess(paymentData);
        } catch (Exception e) {
            log.error("Error processing payment.success event: {}", e.getMessage(), e);
        }
    }
    
    @KafkaListener(topics = "payment.failed", groupId = "analytics-service-group")
    public void consumePaymentFailed(@Payload String message) {
        log.info("Received payment.failed event");
        try {
            JsonNode paymentData = objectMapper.readTree(message);
            aggregationService.processPaymentFailed(paymentData);
        } catch (Exception e) {
            log.error("Error processing payment.failed event: {}", e.getMessage(), e);
        }
    }
    
    // Product Events
    @KafkaListener(topics = "product.viewed", groupId = "analytics-service-group")
    public void consumeProductViewed(@Payload String message) {
        log.debug("Received product.viewed event");
        try {
            JsonNode productData = objectMapper.readTree(message);
            aggregationService.processProductViewed(productData);
        } catch (Exception e) {
            log.error("Error processing product.viewed event: {}", e.getMessage(), e);
        }
    }
    
    // User Events
    @KafkaListener(topics = "user.registered", groupId = "analytics-service-group")
    public void consumeUserRegistered(@Payload String message) {
        log.info("Received user.registered event");
        try {
            JsonNode userData = objectMapper.readTree(message);
            aggregationService.processUserRegistered(userData);
        } catch (Exception e) {
            log.error("Error processing user.registered event: {}", e.getMessage(), e);
        }
    }
    
    // Cart Events
    @KafkaListener(topics = "cart.item.added", groupId = "analytics-service-group")
    public void consumeCartItemAdded(@Payload String message) {
        log.debug("Received cart.item.added event");
        try {
            JsonNode cartData = objectMapper.readTree(message);
            aggregationService.processCartItemAdded(cartData);
        } catch (Exception e) {
            log.error("Error processing cart.item.added event: {}", e.getMessage(), e);
        }
    }
    
    @KafkaListener(topics = "cart.abandoned", groupId = "analytics-service-group")
    public void consumeCartAbandoned(@Payload String message) {
        log.info("Received cart.abandoned event");
        try {
            JsonNode cartData = objectMapper.readTree(message);
            aggregationService.processCartAbandoned(cartData);
        } catch (Exception e) {
            log.error("Error processing cart.abandoned event: {}", e.getMessage(), e);
        }
    }
    
    // Review Events
    @KafkaListener(topics = "review.created", groupId = "analytics-service-group")
    public void consumeReviewCreated(@Payload String message) {
        log.info("Received review.created event");
        try {
            JsonNode reviewData = objectMapper.readTree(message);
            aggregationService.processReviewCreated(reviewData);
        } catch (Exception e) {
            log.error("Error processing review.created event: {}", e.getMessage(), e);
        }
    }
}

