package com.nexus.payment.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.payment.dto.InitiatePaymentRequest;
import com.nexus.payment.dto.PaymentDto;
import com.nexus.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    
    private final PaymentService paymentService;
    
    /**
     * Initiate a payment
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentDto>> initiatePayment(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody InitiatePaymentRequest request) {
        
        log.info("Initiating payment for user: {}, order: {}", userId, request.getOrderId());
        PaymentDto payment = paymentService.initiatePayment(userId, request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment initiated successfully", payment));
    }
    
    /**
     * Confirm payment (webhook simulation)
     */
    @PostMapping("/{paymentId}/confirm")
    public ResponseEntity<ApiResponse<PaymentDto>> confirmPayment(
            @PathVariable String paymentId) {
        
        log.info("Confirming payment: {}", paymentId);
        PaymentDto payment = paymentService.confirmPayment(paymentId);
        
        return ResponseEntity.ok(ApiResponse.success("Payment confirmed successfully", payment));
    }
    
    /**
     * Fail payment
     */
    @PostMapping("/{paymentId}/fail")
    public ResponseEntity<ApiResponse<PaymentDto>> failPayment(
            @PathVariable String paymentId,
            @RequestParam String reason) {
        
        log.info("Failing payment: {}", paymentId);
        PaymentDto payment = paymentService.failPayment(paymentId, reason);
        
        return ResponseEntity.ok(ApiResponse.success("Payment marked as failed", payment));
    }
    
    /**
     * Refund payment
     */
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<ApiResponse<PaymentDto>> refundPayment(
            @PathVariable String paymentId,
            @RequestParam BigDecimal amount,
            @RequestParam String reason) {
        
        log.info("Refunding payment: {}, amount: {}", paymentId, amount);
        PaymentDto payment = paymentService.refundPayment(paymentId, amount, reason);
        
        return ResponseEntity.ok(ApiResponse.success("Payment refunded successfully", payment));
    }
    
    /**
     * Get payment by ID
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentDto>> getPaymentById(
            @PathVariable String paymentId) {
        
        log.info("Fetching payment: {}", paymentId);
        PaymentDto payment = paymentService.getPaymentById(paymentId);
        
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", payment));
    }
    
    /**
     * Get payment by order ID
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<PaymentDto>> getPaymentByOrderId(
            @PathVariable String orderId) {
        
        log.info("Fetching payment for order: {}", orderId);
        PaymentDto payment = paymentService.getPaymentByOrderId(orderId);
        
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", payment));
    }
}

