package com.nexus.common.enums;

/**
 * User Role Enum for NEXUS Commerce
 * Enhanced for e-commerce platform
 */
public enum UserRole {
    CUSTOMER("Regular customer", 1),
    SELLER("Seller/Vendor", 2),
    DELIVERY_PARTNER("Delivery partner", 3),
    RESTAURANT_OWNER("Restaurant/Store owner", 4),
    ADMIN("Platform administrator", 5),
    SUPER_ADMIN("Super administrator", 6);
    
    private final String description;
    private final int level;
    
    UserRole(String description, int level) {
        this.description = description;
        this.level = level;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getLevel() {
        return level;
    }
    
    public boolean hasPermission(UserRole requiredRole) {
        return this.level >= requiredRole.level;
    }
}

