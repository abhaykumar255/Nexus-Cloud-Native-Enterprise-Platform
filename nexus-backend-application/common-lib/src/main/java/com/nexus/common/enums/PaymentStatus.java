package com.nexus.common.enums;

/**
 * Payment Status Enum
 */
public enum PaymentStatus {
    PENDING("Payment initiated, awaiting confirmation"),
    PROCESSING("Payment being processed by gateway"),
    COMPLETED("Payment successful"),
    FAILED("Payment failed"),
    CANCELLED("Payment cancelled"),
    REFUNDED("Payment refunded to customer"),
    PARTIALLY_REFUNDED("Partial refund processed");
    
    private final String description;
    
    PaymentStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isFinalState() {
        return this == COMPLETED || this == FAILED || this == CANCELLED || this == REFUNDED;
    }
}

