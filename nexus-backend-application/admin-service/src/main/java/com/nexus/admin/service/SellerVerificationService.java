package com.nexus.admin.service;

import com.nexus.admin.domain.SellerVerification;
import com.nexus.admin.domain.SellerVerification.VerificationStatus;
import com.nexus.admin.dto.SellerVerificationRequest;
import com.nexus.admin.repository.SellerVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Seller Verification Service
 * Manages seller approval and verification workflow
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SellerVerificationService {
    
    private final SellerVerificationRepository verificationRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Transactional
    public SellerVerification submitVerification(SellerVerificationRequest request) {
        log.info("Submitting seller verification for seller: {}", request.getSellerId());
        
        SellerVerification verification = SellerVerification.builder()
            .sellerId(request.getSellerId())
            .businessName(request.getBusinessName())
            .contactEmail(request.getContactEmail())
            .contactPhone(request.getContactPhone())
            .businessRegistrationNumber(request.getBusinessRegistrationNumber())
            .taxId(request.getTaxId())
            .status(VerificationStatus.PENDING)
            .documents(request.getDocuments())
            .documentCount(request.getDocumentCount())
            .submittedAt(LocalDateTime.now())
            .build();
        
        SellerVerification saved = verificationRepository.save(verification);
        publishVerificationEvent(saved, "submitted");
        
        return saved;
    }
    
    @Transactional
    public SellerVerification approveSeller(Long sellerId, Long reviewerId, String reviewerName, Integer expiryDays) {
        log.info("Approving seller: {}", sellerId);
        
        SellerVerification verification = verificationRepository.findBySellerId(sellerId)
            .orElseThrow(() -> new RuntimeException("Seller verification not found"));
        
        verification.setStatus(VerificationStatus.APPROVED);
        verification.setReviewerId(reviewerId);
        verification.setReviewerName(reviewerName);
        verification.setReviewedAt(LocalDateTime.now());
        verification.setApprovedAt(LocalDateTime.now());
        verification.setExpiryDate(LocalDateTime.now().plusDays(expiryDays != null ? expiryDays : 365));
        
        SellerVerification saved = verificationRepository.save(verification);
        publishVerificationEvent(saved, "approved");
        
        return saved;
    }
    
    @Transactional
    public SellerVerification rejectSeller(Long sellerId, String reason, Long reviewerId, String reviewerName) {
        log.info("Rejecting seller: {}", sellerId);
        
        SellerVerification verification = verificationRepository.findBySellerId(sellerId)
            .orElseThrow(() -> new RuntimeException("Seller verification not found"));
        
        verification.setStatus(VerificationStatus.REJECTED);
        verification.setRejectionReason(reason);
        verification.setReviewerId(reviewerId);
        verification.setReviewerName(reviewerName);
        verification.setReviewedAt(LocalDateTime.now());
        
        SellerVerification saved = verificationRepository.save(verification);
        publishVerificationEvent(saved, "rejected");
        
        return saved;
    }
    
    @Transactional
    public SellerVerification updateVerificationStatus(Long sellerId, VerificationStatus status, String notes) {
        log.info("Updating seller verification status: {} -> {}", sellerId, status);
        
        SellerVerification verification = verificationRepository.findBySellerId(sellerId)
            .orElseThrow(() -> new RuntimeException("Seller verification not found"));
        
        verification.setStatus(status);
        verification.setReviewNotes(notes);
        
        if (status == VerificationStatus.UNDER_REVIEW) {
            verification.setReviewedAt(LocalDateTime.now());
        }
        
        return verificationRepository.save(verification);
    }
    
    public List<SellerVerification> getVerificationsByStatus(VerificationStatus status) {
        return verificationRepository.findByStatus(status);
    }
    
    public SellerVerification getSellerVerification(Long sellerId) {
        return verificationRepository.findBySellerId(sellerId)
            .orElseThrow(() -> new RuntimeException("Seller verification not found"));
    }
    
    public List<SellerVerification> getPendingVerifications() {
        return verificationRepository.findByStatus(VerificationStatus.PENDING);
    }
    
    public List<SellerVerification> getExpiredVerifications() {
        return verificationRepository.findExpiredVerifications(LocalDateTime.now());
    }
    
    private void publishVerificationEvent(SellerVerification verification, String action) {
        try {
            kafkaTemplate.send("seller.verification." + action, 
                verification.getSellerId().toString(), verification);
            log.info("Published {} event for seller: {}", action, verification.getSellerId());
        } catch (Exception e) {
            log.error("Failed to publish verification event: {}", e.getMessage());
        }
    }
}

