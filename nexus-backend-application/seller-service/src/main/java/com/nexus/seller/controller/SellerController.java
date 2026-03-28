package com.nexus.seller.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.common.enums.SellerStatus;
import com.nexus.seller.dto.CreateSellerRequest;
import com.nexus.seller.dto.SellerDto;
import com.nexus.seller.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
@Slf4j
public class SellerController {
    
    private final SellerService sellerService;
    
    /**
     * Register as a seller
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SellerDto>> registerSeller(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CreateSellerRequest request) {
        
        log.info("Registering seller for user: {}", userId);
        SellerDto seller = sellerService.registerSeller(userId, request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Seller registered successfully", seller));
    }
    
    /**
     * Get seller profile (by logged-in user)
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<SellerDto>> getMySellerProfile(
            @RequestHeader("X-User-Id") String userId) {
        
        log.info("Fetching seller profile for user: {}", userId);
        SellerDto seller = sellerService.getSellerByUserId(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Seller profile retrieved successfully", seller));
    }
    
    /**
     * Get seller by ID
     */
    @GetMapping("/{sellerId}")
    public ResponseEntity<ApiResponse<SellerDto>> getSellerById(
            @PathVariable String sellerId) {
        
        log.info("Fetching seller: {}", sellerId);
        SellerDto seller = sellerService.getSellerById(sellerId);
        
        return ResponseEntity.ok(ApiResponse.success("Seller retrieved successfully", seller));
    }
    
    /**
     * Get sellers by status (admin only)
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<SellerDto>>> getSellersByStatus(
            @PathVariable SellerStatus status) {
        
        log.info("Fetching sellers with status: {}", status);
        List<SellerDto> sellers = sellerService.getSellersByStatus(status);
        
        return ResponseEntity.ok(ApiResponse.success("Sellers retrieved successfully", sellers));
    }
    
    /**
     * Verify seller (admin only)
     */
    @PostMapping("/{sellerId}/verify")
    public ResponseEntity<ApiResponse<SellerDto>> verifySeller(
            @PathVariable String sellerId,
            @RequestParam(required = false) String notes) {
        
        log.info("Verifying seller: {}", sellerId);
        SellerDto seller = sellerService.verifySeller(sellerId, notes);
        
        return ResponseEntity.ok(ApiResponse.success("Seller verified successfully", seller));
    }
    
    /**
     * Reject seller (admin only)
     */
    @PostMapping("/{sellerId}/reject")
    public ResponseEntity<ApiResponse<SellerDto>> rejectSeller(
            @PathVariable String sellerId,
            @RequestParam String notes) {
        
        log.info("Rejecting seller: {}", sellerId);
        SellerDto seller = sellerService.rejectSeller(sellerId, notes);
        
        return ResponseEntity.ok(ApiResponse.success("Seller rejected", seller));
    }
}

