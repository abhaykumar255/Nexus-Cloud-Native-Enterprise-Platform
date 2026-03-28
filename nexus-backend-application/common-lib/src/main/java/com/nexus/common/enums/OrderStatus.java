package com.nexus.common.enums;

/**
 * Order Status Enum - Complete Order Lifecycle
 * Aligned with NEXUS Commerce specification
 */
public enum OrderStatus {
    // Initial state
    PENDING_PAYMENT("Order created, awaiting payment"),
    
    // Payment completed
    PAYMENT_CONFIRMED("Payment successful, awaiting seller confirmation"),
    
    // Seller actions
    CONFIRMED("Seller accepted order"),
    REJECTED("Seller rejected order"),
    
    // Fulfillment stages
    PACKED("Order packed and ready for dispatch"),
    DISPATCHED("Order dispatched from warehouse/store"),
    OUT_FOR_DELIVERY("Delivery partner picked up, in transit"),
    
    // Completion states
    DELIVERED("Successfully delivered to customer"),
    
    // Return flow
    RETURN_REQUESTED("Customer requested return"),
    RETURNED("Product returned and processed"),
    
    // Cancellation
    CANCELLED("Order cancelled"),
    
    // Failure
    FAILED("Delivery failed or other critical error"),
    
    // On-hold
    ON_HOLD("Order temporarily on hold");
    
    private final String description;
    
    OrderStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isFinalState() {
        return this == DELIVERED || this == CANCELLED || this == RETURNED || this == FAILED;
    }
    
    public boolean canTransitionTo(OrderStatus nextStatus) {
        return switch (this) {
            case PENDING_PAYMENT -> nextStatus == PAYMENT_CONFIRMED || nextStatus == CANCELLED;
            case PAYMENT_CONFIRMED -> nextStatus == CONFIRMED || nextStatus == CANCELLED || nextStatus == REJECTED;
            case CONFIRMED -> nextStatus == PACKED || nextStatus == CANCELLED;
            case PACKED -> nextStatus == DISPATCHED || nextStatus == CANCELLED;
            case DISPATCHED -> nextStatus == OUT_FOR_DELIVERY || nextStatus == CANCELLED;
            case OUT_FOR_DELIVERY -> nextStatus == DELIVERED || nextStatus == FAILED;
            case DELIVERED -> nextStatus == RETURN_REQUESTED;
            case RETURN_REQUESTED -> nextStatus == RETURNED;
            case CANCELLED, RETURNED, FAILED, REJECTED -> false; // Final states
            case ON_HOLD -> nextStatus == CONFIRMED || nextStatus == CANCELLED;
        };
    }
}

