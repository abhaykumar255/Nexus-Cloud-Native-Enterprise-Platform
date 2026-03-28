package com.nexus.inventory.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.common.dto.PaginationInfo;
import com.nexus.inventory.dto.BulkStockCheckRequest;
import com.nexus.inventory.dto.InventoryStockDto;
import com.nexus.inventory.dto.ReservationRequest;
import com.nexus.inventory.dto.StockUpdateRequest;
import com.nexus.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Inventory Controller
 * Manages inventory stock, reservations, and availability
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    /**
     * Get inventory by product ID
     * GET /api/v1/inventory/product/{productId}
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<InventoryStockDto>> getInventoryByProductId(
            @PathVariable String productId) {
        log.info("Get inventory for product: {}", productId);
        
        InventoryStockDto inventory = inventoryService.getInventoryByProductId(productId);
        
        return ResponseEntity.ok(ApiResponse.success(inventory));
    }
    
    /**
     * Check stock availability
     * GET /api/v1/inventory/check-availability
     */
    @GetMapping("/check-availability")
    public ResponseEntity<ApiResponse<Boolean>> checkAvailability(
            @RequestParam String productId,
            @RequestParam Integer quantity) {
        log.info("Check availability for product: {}, quantity: {}", productId, quantity);
        
        boolean available = inventoryService.checkAvailability(productId, quantity);
        
        return ResponseEntity.ok(ApiResponse.success(available));
    }
    
    /**
     * Bulk stock availability check
     * POST /api/v1/inventory/bulk-check
     */
    @PostMapping("/bulk-check")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> bulkCheckAvailability(
            @Valid @RequestBody BulkStockCheckRequest request) {
        log.info("Bulk check availability for {} products", request.getItems().size());
        
        Map<String, Boolean> availability = inventoryService.bulkCheckAvailability(request);
        
        return ResponseEntity.ok(ApiResponse.success(availability));
    }
    
    /**
     * Add stock
     * POST /api/v1/inventory/add-stock
     */
    @PostMapping("/add-stock")
    public ResponseEntity<ApiResponse<InventoryStockDto>> addStock(
            @Valid @RequestBody StockUpdateRequest request,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Seller-Id") String sellerId) {
        log.info("Add stock for product: {}, quantity: {}", request.getProductId(), request.getQuantity());
        
        InventoryStockDto updated = inventoryService.addStock(request, sellerId);
        
        return ResponseEntity.ok(ApiResponse.success("Stock added successfully", updated));
    }
    
    /**
     * Reduce stock
     * POST /api/v1/inventory/reduce-stock
     */
    @PostMapping("/reduce-stock")
    public ResponseEntity<ApiResponse<InventoryStockDto>> reduceStock(
            @Valid @RequestBody StockUpdateRequest request,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Seller-Id") String sellerId) {
        log.info("Reduce stock for product: {}, quantity: {}", request.getProductId(), request.getQuantity());
        
        InventoryStockDto updated = inventoryService.reduceStock(request, sellerId);
        
        return ResponseEntity.ok(ApiResponse.success("Stock reduced successfully", updated));
    }
    
    /**
     * Create soft reservation (cart)
     * POST /api/v1/inventory/reserve/soft
     */
    @PostMapping("/reserve/soft")
    public ResponseEntity<ApiResponse<String>> createSoftReservation(
            @Valid @RequestBody ReservationRequest request,
            @RequestHeader("X-User-Id") String userId) {
        log.info("Create soft reservation for product: {}, user: {}", request.getProductId(), userId);
        
        request.setUserId(userId);
        inventoryService.createSoftReservation(request);
        
        return ResponseEntity.ok(ApiResponse.success("Soft reservation created successfully", "OK"));
    }
    
    /**
     * Release soft reservation
     * POST /api/v1/inventory/release/soft
     */
    @PostMapping("/release/soft")
    public ResponseEntity<ApiResponse<String>> releaseSoftReservation(
            @RequestParam String productId,
            @RequestParam Integer quantity,
            @RequestHeader("X-User-Id") String userId) {
        log.info("Release soft reservation for product: {}, user: {}", productId, userId);
        
        inventoryService.releaseSoftReservation(userId, productId, quantity);
        
        return ResponseEntity.ok(ApiResponse.success("Soft reservation released successfully", "OK"));
    }
    
    /**
     * Create hard reservation (order confirmed)
     * POST /api/v1/inventory/reserve/hard
     */
    @PostMapping("/reserve/hard")
    public ResponseEntity<ApiResponse<String>> createHardReservation(
            @Valid @RequestBody ReservationRequest request) {
        log.info("Create hard reservation for product: {}, order: {}", request.getProductId(), request.getOrderId());
        
        inventoryService.createHardReservation(request);

        return ResponseEntity.ok(ApiResponse.success("Hard reservation created successfully", "OK"));
    }

    /**
     * Release hard reservation (order cancelled)
     * POST /api/v1/inventory/release/hard
     */
    @PostMapping("/release/hard")
    public ResponseEntity<ApiResponse<String>> releaseHardReservation(
            @RequestParam String productId,
            @RequestParam String orderId,
            @RequestParam Integer quantity) {
        log.info("Release hard reservation for product: {}, order: {}", productId, orderId);

        inventoryService.releaseHardReservation(productId, orderId, quantity);

        return ResponseEntity.ok(ApiResponse.success("Hard reservation released successfully", "OK"));
    }

    /**
     * Get seller inventory
     * GET /api/v1/inventory/seller/{sellerId}
     */
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<List<InventoryStockDto>>> getSellerInventory(
            @PathVariable String sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Get inventory for seller: {}", sellerId);

        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryStockDto> inventory = inventoryService.getSellerInventory(sellerId, pageable);

        return ResponseEntity.ok(ApiResponse.paginated(inventory.getContent(), PaginationInfo.from(inventory)));
    }

    /**
     * Get low stock items
     * GET /api/v1/inventory/low-stock
     */
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<InventoryStockDto>>> getLowStockItems(
            @RequestParam(required = false) String sellerId) {
        log.info("Get low stock items for seller: {}", sellerId);

        List<InventoryStockDto> lowStockItems = inventoryService.getLowStockItems(sellerId);

        return ResponseEntity.ok(ApiResponse.success(lowStockItems));
    }

    /**
     * Initialize inventory for new product
     * POST /api/v1/inventory/initialize
     */
    @PostMapping("/initialize")
    public ResponseEntity<ApiResponse<InventoryStockDto>> initializeInventory(
            @RequestParam String productId,
            @RequestParam String sellerId,
            @RequestParam(required = false) Integer initialStock) {
        log.info("Initialize inventory for product: {}, seller: {}", productId, sellerId);

        InventoryStockDto inventory = inventoryService.initializeInventory(productId, sellerId, initialStock);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inventory initialized successfully", inventory));
    }
}
