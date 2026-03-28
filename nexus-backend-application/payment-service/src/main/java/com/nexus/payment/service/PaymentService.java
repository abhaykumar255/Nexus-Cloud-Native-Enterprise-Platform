package com.nexus.payment.service;

import com.nexus.common.enums.PaymentMethod;
import com.nexus.common.enums.PaymentStatus;
import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.payment.domain.entity.Payment;
import com.nexus.payment.domain.repository.PaymentRepository;
import com.nexus.payment.dto.InitiatePaymentRequest;
import com.nexus.payment.dto.PaymentDto;
import com.nexus.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    
    /**
     * Initiate a new payment
     */
    @Transactional
    public PaymentDto initiatePayment(String userId, InitiatePaymentRequest request) {
        log.info("Initiating payment for order: {}, userId: {}", request.getOrderId(), userId);
        
        // Determine gateway provider
        String gatewayProvider = determineGatewayProvider(request.getPaymentMethod());
        
        // Create payment record
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .userId(userId)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.PENDING)
                .gatewayProvider(gatewayProvider)
                .transactionId(generateTransactionId())
                .build();
        
        // TODO: Call payment gateway API
        // For now, simulate gateway response
        payment.setGatewayReferenceId(UUID.randomUUID().toString());
        
        payment = paymentRepository.save(payment);
        log.info("Payment initiated: {}", payment.getId());
        
        // TODO: Publish PAYMENT_INITIATED event
        
        return paymentMapper.toDto(payment);
    }
    
    /**
     * Confirm payment (simulated webhook)
     */
    @Transactional
    public PaymentDto confirmPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setCompletedAt(Instant.now());
        
        payment = paymentRepository.save(payment);
        log.info("Payment confirmed: {}", paymentId);
        
        // TODO: Publish PAYMENT_COMPLETED event
        
        return paymentMapper.toDto(payment);
    }
    
    /**
     * Mark payment as failed
     */
    @Transactional
    public PaymentDto failPayment(String paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        
        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailureReason(reason);
        
        payment = paymentRepository.save(payment);
        log.info("Payment failed: {}, reason: {}", paymentId, reason);
        
        // TODO: Publish PAYMENT_FAILED event
        
        return paymentMapper.toDto(payment);
    }
    
    /**
     * Process refund
     */
    @Transactional
    public PaymentDto refundPayment(String paymentId, BigDecimal amount, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot refund payment in " + payment.getStatus() + " status");
        }
        
        if (amount.compareTo(payment.getAmount()) > 0) {
            throw new IllegalArgumentException("Refund amount cannot exceed payment amount");
        }
        
        payment.setRefundAmount(amount);
        payment.setRefundReason(reason);
        payment.setRefundedAt(Instant.now());
        payment.setStatus(PaymentStatus.REFUNDED);
        
        payment = paymentRepository.save(payment);
        log.info("Payment refunded: {}, amount: {}", paymentId, amount);
        
        // TODO: Publish PAYMENT_REFUNDED event
        
        return paymentMapper.toDto(payment);
    }
    
    /**
     * Get payment by ID
     */
    public PaymentDto getPaymentById(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        return paymentMapper.toDto(payment);
    }
    
    /**
     * Get payment by order ID
     */
    public PaymentDto getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order: " + orderId));
        return paymentMapper.toDto(payment);
    }
    
    // Helper methods
    
    private String determineGatewayProvider(PaymentMethod method) {
        return switch (method) {
            case CREDIT_CARD, DEBIT_CARD -> "STRIPE";
            case UPI, NET_BANKING -> "RAZORPAY";
            case WALLET, COD -> "INTERNAL";
            case EMI -> "STRIPE";
        };
    }
    
    private String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

