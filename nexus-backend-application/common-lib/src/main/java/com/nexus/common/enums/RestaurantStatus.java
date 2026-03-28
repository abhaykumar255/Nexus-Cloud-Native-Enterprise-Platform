package com.nexus.common.enums;

/**
 * Restaurant/Store Status Enum
 */
public enum RestaurantStatus {
    OPEN("Open for orders"),
    CLOSED("Closed, not accepting orders"),
    TEMPORARILY_CLOSED("Temporarily closed due to high demand or other issues"),
    PERMANENTLY_CLOSED("Permanently closed");
    
    private final String description;
    
    RestaurantStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean canAcceptOrders() {
        return this == OPEN;
    }
}

