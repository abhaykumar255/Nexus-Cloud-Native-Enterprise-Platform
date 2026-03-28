package com.nexus.common.enums;

/**
 * Seller Verification Status Enum
 */
public enum SellerStatus {
    PENDING("Registration pending, initial state"),
    UNDER_REVIEW("KYC documents under review"),
    VERIFIED("Verified and active"),
    SUSPENDED("Temporarily suspended"),
    BLOCKED("Permanently blocked"),
    INACTIVE("Inactive by seller choice");
    
    private final String description;
    
    SellerStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean canSell() {
        return this == VERIFIED;
    }
}

