package com.nexus.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when payment processing fails
 */
public class PaymentException extends NexusException {

    private final String transactionId;
    private final String gatewayErrorCode;

    public PaymentException(String message) {
        super(message, "PAYMENT_ERROR", HttpStatus.PAYMENT_REQUIRED);
        this.transactionId = null;
        this.gatewayErrorCode = null;
    }

    public PaymentException(String message, String transactionId, String gatewayErrorCode) {
        super(String.format("Payment failed: %s (Transaction: %s, Gateway Error: %s)",
                          message, transactionId, gatewayErrorCode),
              "PAYMENT_GATEWAY_ERROR",
              HttpStatus.PAYMENT_REQUIRED);
        this.transactionId = transactionId;
        this.gatewayErrorCode = gatewayErrorCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getGatewayErrorCode() {
        return gatewayErrorCode;
    }
}

