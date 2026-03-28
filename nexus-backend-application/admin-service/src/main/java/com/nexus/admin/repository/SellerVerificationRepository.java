package com.nexus.admin.repository;

import com.nexus.admin.domain.SellerVerification;
import com.nexus.admin.domain.SellerVerification.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SellerVerificationRepository extends JpaRepository<SellerVerification, Long> {
    
    Optional<SellerVerification> findBySellerId(Long sellerId);
    
    List<SellerVerification> findByStatus(VerificationStatus status);
    
    List<SellerVerification> findByStatusIn(List<VerificationStatus> statuses);
    
    @Query("SELECT sv FROM SellerVerification sv WHERE sv.expiryDate < :date AND sv.status = 'APPROVED'")
    List<SellerVerification> findExpiredVerifications(LocalDateTime date);
    
    @Query("SELECT COUNT(sv) FROM SellerVerification sv WHERE sv.status = :status")
    Long countByStatus(VerificationStatus status);
}

