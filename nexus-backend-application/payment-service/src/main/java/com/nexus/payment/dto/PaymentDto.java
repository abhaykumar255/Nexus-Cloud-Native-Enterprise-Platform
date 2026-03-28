package com.nexus.payment.dto;

import com.nexus.common.enums.PaymentMethod;
import com.nexus.common.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    
    private String id;
    private String orderId;
    private String userId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String gatewayProvider;
    private String gatewayReferenceId;
    private String transactionId;
    private String failureReason;
    private String failureCode;
    private BigDecimal refundAmount;
    private String refundReason;
    private Instant refundedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant completedAt;
}

