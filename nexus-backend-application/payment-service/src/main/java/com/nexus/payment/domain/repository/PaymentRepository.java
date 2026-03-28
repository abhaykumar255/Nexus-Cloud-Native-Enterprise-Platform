package com.nexus.payment.domain.repository;

import com.nexus.common.enums.PaymentStatus;
import com.nexus.payment.domain.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    
    Optional<Payment> findByOrderId(String orderId);
    
    Optional<Payment> findByGatewayReferenceId(String gatewayReferenceId);
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    Page<Payment> findByUserId(String userId, Pageable pageable);
    
    List<Payment> findByUserIdAndStatus(String userId, PaymentStatus status);
    
    List<Payment> findByStatus(PaymentStatus status);
}

