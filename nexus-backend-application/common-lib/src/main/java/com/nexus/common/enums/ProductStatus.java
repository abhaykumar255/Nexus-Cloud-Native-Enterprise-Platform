package com.nexus.common.enums;

/**
 * Product Lifecycle Status Enum
 */
public enum ProductStatus {
    DRAFT("Product draft, not visible to customers"),
    PENDING_REVIEW("Submitted for admin review"),
    PUBLISHED("Published and visible to customers"),
    ARCHIVED("Archived, not visible to customers"),
    REJECTED("Rejected by admin"),
    OUT_OF_STOCK("Temporarily out of stock");
    
    private final String description;
    
    ProductStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isVisible() {
        return this == PUBLISHED;
    }
}

