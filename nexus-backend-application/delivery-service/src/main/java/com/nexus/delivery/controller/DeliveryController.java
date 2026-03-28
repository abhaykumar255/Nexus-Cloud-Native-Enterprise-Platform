package com.nexus.delivery.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.common.enums.DeliveryStatus;
import com.nexus.delivery.dto.DeliveryDto;
import com.nexus.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {
    
    private final DeliveryService deliveryService;
    
    /**
     * Get delivery by order ID
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<DeliveryDto>> getDeliveryByOrderId(
            @PathVariable String orderId) {
        
        log.info("Fetching delivery for order: {}", orderId);
        DeliveryDto delivery = deliveryService.getDeliveryByOrderId(orderId);
        
        return ResponseEntity.ok(ApiResponse.success("Delivery retrieved successfully", delivery));
    }
    
    /**
     * Update delivery status
     */
    @PatchMapping("/{deliveryId}/status")
    public ResponseEntity<ApiResponse<DeliveryDto>> updateStatus(
            @PathVariable String deliveryId,
            @RequestParam DeliveryStatus status) {
        
        log.info("Updating delivery {} to status: {}", deliveryId, status);
        DeliveryDto delivery = deliveryService.updateStatus(deliveryId, status);
        
        return ResponseEntity.ok(ApiResponse.success("Delivery status updated successfully", delivery));
    }
}

