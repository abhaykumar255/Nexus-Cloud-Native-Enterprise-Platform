package com.nexus.order.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.common.enums.OrderStatus;
import com.nexus.order.dto.CreateOrderRequest;
import com.nexus.order.dto.OrderDto;
import com.nexus.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    
    private final OrderService orderService;
    
    /**
     * Create a new order
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CreateOrderRequest request) {
        
        log.info("Creating order for user: {}", userId);
        OrderDto order = orderService.createOrder(userId, request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", order));
    }
    
    /**
     * Get order by ID
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(
            @PathVariable String orderId) {
        
        log.info("Fetching order: {}", orderId);
        OrderDto order = orderService.getOrderById(orderId);
        
        return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", order));
    }
    
    /**
     * Get order by order number
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderByNumber(
            @PathVariable String orderNumber) {
        
        log.info("Fetching order by number: {}", orderNumber);
        OrderDto order = orderService.getOrderByNumber(orderNumber);
        
        return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", order));
    }
    
    /**
     * Get user orders
     */
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserOrders(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Fetching orders for user: {}, page: {}, size: {}", userId, page, size);
        Map<String, Object> result = orderService.getUserOrders(userId, page, size);
        
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", result));
    }
    
    /**
     * Update order status
     */
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam OrderStatus status) {
        
        log.info("Updating order {} to status: {}", orderId, status);
        OrderDto order = orderService.updateOrderStatus(orderId, status);
        
        return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", order));
    }
    
    /**
     * Cancel order
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderDto>> cancelOrder(
            @PathVariable String orderId,
            @RequestParam(required = false) String reason) {
        
        log.info("Cancelling order: {}, reason: {}", orderId, reason);
        OrderDto order = orderService.cancelOrder(orderId, reason);
        
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", order));
    }
}

