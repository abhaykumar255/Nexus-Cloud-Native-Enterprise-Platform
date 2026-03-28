package com.nexus.common.exception;

/**
 * Exception thrown when inventory is insufficient for an order
 */
public class InsufficientStockException extends BusinessRuleException {
    
    private final String productId;
    private final int requestedQuantity;
    private final int availableStock;
    
    public InsufficientStockException(String productId, int requestedQuantity, int availableStock) {
        super(String.format("Insufficient stock for product %s. Requested: %d, Available: %d", 
                          productId, requestedQuantity, availableStock));
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
        this.availableStock = availableStock;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public int getRequestedQuantity() {
        return requestedQuantity;
    }
    
    public int getAvailableStock() {
        return availableStock;
    }
}

