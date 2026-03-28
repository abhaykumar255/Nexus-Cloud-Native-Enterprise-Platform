package com.nexus.common.enums;

/**
 * Payment Method Enum
 */
public enum PaymentMethod {
    UPI("Unified Payments Interface"),
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    NET_BANKING("Net Banking"),
    WALLET("Digital Wallet"),
    COD("Cash on Delivery"),
    EMI("EMI Payment");
    
    private final String description;
    
    PaymentMethod(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

