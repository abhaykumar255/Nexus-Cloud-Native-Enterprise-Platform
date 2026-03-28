package com.nexus.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Payment Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    private BigDecimal amount;
    private String currency;
    private String customerId;
    private String paymentMethodId;
    private String description;
}

