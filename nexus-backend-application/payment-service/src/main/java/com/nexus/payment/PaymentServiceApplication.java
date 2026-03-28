package com.nexus.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Payment Service Application
 * 
 * Handles payment processing, gateway integration, refunds, and wallet management
 * 
 * Key Features:
 * - Payment gateway integration (Stripe, Razorpay, PayPal simulation)
 * - Payment verification and reconciliation
 * - Refund processing
 * - Wallet and virtual currency management
 * - Transaction history and audit trail
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.nexus.payment", "com.nexus.common"})
public class PaymentServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}

