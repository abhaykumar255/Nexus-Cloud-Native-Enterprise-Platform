package com.nexus.order.service;

import com.nexus.common.dto.PaginationInfo;
import com.nexus.common.enums.OrderStatus;
import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.order.domain.entity.Order;
import com.nexus.order.domain.entity.OrderItem;
import com.nexus.order.domain.repository.OrderRepository;
import com.nexus.order.dto.*;
import com.nexus.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    
    private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // 10% tax
    private static final BigDecimal DELIVERY_FEE = new BigDecimal("5.00");
    
    /**
     * Create a new order
     */
    @Transactional
    public OrderDto createOrder(String userId, CreateOrderRequest request) {
        log.info("Creating order for user: {}", userId);
        
        // Generate order number
        String orderNumber = generateOrderNumber();
        
        // Calculate totals
        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        // Create order entity
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .userId(userId)
                .type(request.getType())
                .status(OrderStatus.PENDING_PAYMENT)
                .paymentMethod(request.getPaymentMethod())
                .deliveryAddress(request.getDeliveryAddress().getAddress())
                .deliveryCity(request.getDeliveryAddress().getCity())
                .deliveryState(request.getDeliveryAddress().getState())
                .deliveryPostalCode(request.getDeliveryAddress().getPostalCode())
                .deliveryCountry(request.getDeliveryAddress().getCountry())
                .deliveryPhone(request.getDeliveryAddress().getPhone())
                .deliveryLatitude(request.getDeliveryAddress().getLatitude())
                .deliveryLongitude(request.getDeliveryAddress().getLongitude())
                .deliveryInstructions(request.getDeliveryAddress().getInstructions())
                .couponCode(request.getCouponCode())
                .notes(request.getNotes())
                .deliveryFee(DELIVERY_FEE)
                .build();
        
        // Process order items
        for (OrderItemRequest itemReq : request.getItems()) {
            // TODO: Fetch product details from Product Service
            Map<String, Object> productDetails = fetchProductDetails(itemReq.getProductId());
            
            BigDecimal unitPrice = new BigDecimal(productDetails.get("price").toString());
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productId(itemReq.getProductId())
                    .productName(productDetails.get("name").toString())
                    .productImage(productDetails.getOrDefault("primaryImage", "").toString())
                    .sellerId(productDetails.getOrDefault("sellerId", "").toString())
                    .sellerName(productDetails.getOrDefault("sellerName", "").toString())
                    .quantity(itemReq.getQuantity())
                    .unitPrice(unitPrice)
                    .totalPrice(itemTotal)
                    .variantId(itemReq.getVariantId())
                    .build();
            
            orderItems.add(orderItem);
            subtotal = subtotal.add(itemTotal);
        }
        
        order.setItems(orderItems);
        order.setSubtotal(subtotal);
        
        // Calculate tax and total
        BigDecimal tax = subtotal.multiply(TAX_RATE);
        order.setTax(tax);
        
        BigDecimal total = subtotal.add(tax).add(DELIVERY_FEE).subtract(order.getDiscount());
        order.setTotalAmount(total);
        
        // Save order
        order = orderRepository.save(order);
        log.info("Order created: {}", orderNumber);
        
        // TODO: Publish ORDER_CREATED event to Kafka
        
        return orderMapper.toDto(order);
    }
    
    /**
     * Get order by ID
     */
    public OrderDto getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        return orderMapper.toDto(order);
    }
    
    /**
     * Get order by order number
     */
    public OrderDto getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderNumber));
        return orderMapper.toDto(order);
    }
    
    /**
     * Get user orders with pagination
     */
    public Map<String, Object> getUserOrders(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);

        return Map.of(
                "orders", orderMapper.toDtoList(orderPage.getContent()),
                "pagination", PaginationInfo.builder()
                        .page(page)
                        .size(size)
                        .totalElements(orderPage.getTotalElements())
                        .totalPages(orderPage.getTotalPages())
                        .hasNext(orderPage.hasNext())
                        .hasPrevious(orderPage.hasPrevious())
                        .build()
        );
    }

    /**
     * Update order status
     */
    @Transactional
    public OrderDto updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);

        // Update timestamps based on status
        if (newStatus == OrderStatus.CONFIRMED) {
            order.setConfirmedAt(Instant.now());
        } else if (newStatus == OrderStatus.DISPATCHED) {
            order.setDispatchedAt(Instant.now());
        } else if (newStatus == OrderStatus.DELIVERED) {
            order.setActualDeliveryTime(Instant.now());
        }

        order = orderRepository.save(order);
        log.info("Order {} status updated from {} to {}", orderId, oldStatus, newStatus);

        // TODO: Publish ORDER_STATUS_UPDATED event

        return orderMapper.toDto(order);
    }

    /**
     * Cancel order
     */
    @Transactional
    public OrderDto cancelOrder(String orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel order in " + order.getStatus() + " status");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(reason);

        order = orderRepository.save(order);
        log.info("Order {} cancelled. Reason: {}", orderId, reason);

        // TODO: Publish ORDER_CANCELLED event to trigger inventory release

        return orderMapper.toDto(order);
    }

    // Helper methods

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Map<String, Object> fetchProductDetails(String productId) {
        // TODO: Call Product Service via WebClient
        // Mock data for now
        return Map.of(
                "name", "Sample Product",
                "price", 99.99,
                "primaryImage", "https://example.com/image.jpg",
                "sellerId", "seller123",
                "sellerName", "Sample Seller"
        );
    }
}

