package com.nexus.seller.service;

import com.nexus.common.enums.SellerStatus;
import com.nexus.common.exception.DuplicateResourceException;
import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.seller.domain.entity.Seller;
import com.nexus.seller.domain.repository.SellerRepository;
import com.nexus.seller.dto.CreateSellerRequest;
import com.nexus.seller.dto.SellerDto;
import com.nexus.seller.mapper.SellerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerService {
    
    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;
    
    /**
     * Register a new seller
     */
    @Transactional
    public SellerDto registerSeller(String userId, CreateSellerRequest request) {
        log.info("Registering new seller for user: {}", userId);
        
        // Check if seller already exists
        if (sellerRepository.existsByUserId(userId)) {
            throw new DuplicateResourceException("Seller already registered for user: " + userId);
        }
        
        if (sellerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }
        
        Seller seller = Seller.builder()
                .userId(userId)
                .storeName(request.getStoreName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .businessName(request.getBusinessName())
                .businessType(request.getBusinessType())
                .gstNumber(request.getGstNumber())
                .panNumber(request.getPanNumber())
                .description(request.getDescription())
                .status(SellerStatus.PENDING)
                .build();
        
        seller = sellerRepository.save(seller);
        log.info("Seller registered successfully: {}", seller.getId());
        
        return sellerMapper.toDto(seller);
    }
    
    /**
     * Verify seller
     */
    @Transactional
    public SellerDto verifySeller(String sellerId, String notes) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found: " + sellerId));
        
        seller.setStatus(SellerStatus.VERIFIED);
        seller.setVerifiedAt(Instant.now());
        seller.setVerificationNotes(notes);
        
        seller = sellerRepository.save(seller);
        log.info("Seller verified: {}", sellerId);
        
        return sellerMapper.toDto(seller);
    }
    
    /**
     * Reject seller
     */
    @Transactional
    public SellerDto rejectSeller(String sellerId, String notes) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found: " + sellerId));

        seller.setStatus(SellerStatus.BLOCKED);
        seller.setVerificationNotes(notes);

        seller = sellerRepository.save(seller);
        log.info("Seller rejected: {}", sellerId);

        return sellerMapper.toDto(seller);
    }
    
    /**
     * Get seller by ID
     */
    public SellerDto getSellerById(String sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found: " + sellerId));
        return sellerMapper.toDto(seller);
    }
    
    /**
     * Get seller by user ID
     */
    public SellerDto getSellerByUserId(String userId) {
        Seller seller = sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found for user: " + userId));
        return sellerMapper.toDto(seller);
    }
    
    /**
     * Get sellers by status
     */
    public List<SellerDto> getSellersByStatus(SellerStatus status) {
        List<Seller> sellers = sellerRepository.findByStatus(status);
        return sellerMapper.toDtoList(sellers);
    }
}

