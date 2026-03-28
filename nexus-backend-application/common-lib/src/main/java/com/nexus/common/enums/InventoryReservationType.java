package com.nexus.common.enums;

/**
 * Inventory Reservation Type
 */
public enum InventoryReservationType {
    SOFT_RESERVATION("Temporary hold in Redis, released if payment not confirmed within TTL"),
    HARD_RESERVATION("Committed reservation in PostgreSQL after payment confirmation");
    
    private final String description;
    
    InventoryReservationType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

