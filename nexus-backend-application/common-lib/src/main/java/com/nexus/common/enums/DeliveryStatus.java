package com.nexus.common.enums;

/**
 * Delivery Status Enum
 */
public enum DeliveryStatus {
    PENDING_ASSIGNMENT("Awaiting delivery partner assignment"),
    ASSIGNED("Delivery partner assigned"),
    PARTNER_ACCEPTED("Delivery partner accepted"),
    PARTNER_REJECTED("Delivery partner rejected"),
    PICKED_UP("Order picked up from seller/warehouse"),
    IN_TRANSIT("Order in transit to customer"),
    NEAR_DESTINATION("Delivery partner near customer location"),
    DELIVERED("Successfully delivered"),
    FAILED("Delivery failed"),
    RETURNED_TO_ORIGIN("Returned to seller/warehouse"),
    CANCELLED("Delivery cancelled");
    
    private final String description;
    
    DeliveryStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isFinalState() {
        return this == DELIVERED || this == FAILED || this == RETURNED_TO_ORIGIN || this == CANCELLED;
    }
}

