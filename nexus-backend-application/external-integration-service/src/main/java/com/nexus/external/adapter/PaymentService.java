package com.nexus.external.adapter;

import com.nexus.external.dto.PaymentRequest;
import com.nexus.external.dto.PaymentResult;

/**
 * Payment Service Interface
 */
public interface PaymentService {
    
    /**
     * Process payment
     */
    PaymentResult processPayment(PaymentRequest request);
}

