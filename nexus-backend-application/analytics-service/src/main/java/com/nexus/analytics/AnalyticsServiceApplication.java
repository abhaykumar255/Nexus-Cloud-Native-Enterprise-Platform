package com.nexus.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Analytics Service Application
 * Port: 8096
 * Databases: MongoDB (analytics_db) + Elasticsearch (analytics indexes)
 * 
 * Key Features:
 * - Sales dashboards and KPI tracking
 * - User behavior analytics
 * - Revenue reports and forecasting
 * - Product performance metrics
 * - Conversion rate tracking
 * - Real-time business metrics
 * - Data aggregation from all services via Kafka
 * - Elasticsearch for log aggregation and analysis
 * 
 * Kafka Consumers:
 * - order.created, order.completed, order.cancelled
 * - payment.success, payment.failed, payment.refunded
 * - product.viewed, product.purchased
 * - user.registered, user.login
 * - cart.abandoned
 * - review.created
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
@EnableCaching
@EnableAsync
@EnableScheduling
@EnableMongoAuditing
public class AnalyticsServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AnalyticsServiceApplication.class, args);
    }
}

