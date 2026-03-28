package com.nexus.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * AI Service - Recommendations, Anomaly Detection, Predictive Analytics, Chatbot Scaffold
 * Port: 8089
 * Databases: PostgreSQL (ai_db) + MongoDB (ai_analytics)
 * Features:
 * - Task recommendations based on user history and collaborative filtering
 * - Anomaly detection for service metrics
 * - Predictive task completion time estimation
 * - Chatbot scaffold for future NLP integration
 * - Kafka event consumer for real-time model updates
 */
@SpringBootApplication
@EnableFeignClients
@EnableKafka
@EnableCaching
@EnableAsync
@EnableScheduling
@EnableJpaAuditing
public class AiServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiServiceApplication.class, args);
    }
}

