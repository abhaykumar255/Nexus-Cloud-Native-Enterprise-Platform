package com.nexus.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payment Result DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResult {
    
    private boolean success;
    private String transactionId;
    private String status;
    private String errorMessage;
    
    public static PaymentResult failed(String errorMessage) {
        return PaymentResult.builder()
                .success(false)
                .status("FAILED")
                .errorMessage(errorMessage)
                .build();
    }
    
    public static PaymentResult succeeded(String transactionId) {
        return PaymentResult.builder()
                .success(true)
                .transactionId(transactionId)
                .status("SUCCESS")
                .build();
    }
}

