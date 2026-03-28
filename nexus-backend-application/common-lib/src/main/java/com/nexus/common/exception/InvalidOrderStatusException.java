package com.nexus.common.exception;

import com.nexus.common.enums.OrderStatus;

/**
 * Exception thrown when an invalid order status transition is attempted
 */
public class InvalidOrderStatusException extends BusinessRuleException {
    
    private final OrderStatus currentStatus;
    private final OrderStatus attemptedStatus;
    
    public InvalidOrderStatusException(OrderStatus currentStatus, OrderStatus attemptedStatus) {
        super(String.format("Cannot transition order from %s to %s", currentStatus, attemptedStatus));
        this.currentStatus = currentStatus;
        this.attemptedStatus = attemptedStatus;
    }
    
    public OrderStatus getCurrentStatus() {
        return currentStatus;
    }
    
    public OrderStatus getAttemptedStatus() {
        return attemptedStatus;
    }
}

