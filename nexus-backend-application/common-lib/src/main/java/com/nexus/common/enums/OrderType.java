package com.nexus.common.enums;

/**
 * Order Type Enum
 * Supports multiple business verticals in NEXUS Commerce
 */
public enum OrderType {
    PRODUCT_ORDER("Standard e-commerce product order (Amazon/Flipkart style)", 24, 72),
    FOOD_ORDER("Restaurant food delivery (Zomato/Swiggy style)", 30, 90),
    GROCERY_ORDER("Quick commerce grocery delivery", 15, 60),
    BULK_ORDER("B2B bulk purchase from seller", 72, 168);
    
    private final String description;
    private final int minEtaMinutes;
    private final int maxEtaMinutes;
    
    OrderType(String description, int minEtaMinutes, int maxEtaMinutes) {
        this.description = description;
        this.minEtaMinutes = minEtaMinutes;
        this.maxEtaMinutes = maxEtaMinutes;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getMinEtaMinutes() {
        return minEtaMinutes;
    }
    
    public int getMaxEtaMinutes() {
        return maxEtaMinutes;
    }
}

