package com.nexus.inventory.service;

import com.nexus.common.constants.CacheKeys;
import com.nexus.common.constants.KafkaTopics;
import com.nexus.common.enums.InventoryReservationType;
import com.nexus.common.exception.InsufficientStockException;
import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.inventory.domain.entity.InventoryStock;
import com.nexus.inventory.domain.entity.InventoryTransaction;
import com.nexus.inventory.domain.repository.InventoryStockRepository;
import com.nexus.inventory.domain.repository.InventoryTransactionRepository;
import com.nexus.inventory.dto.BulkStockCheckRequest;
import com.nexus.inventory.dto.InventoryStockDto;
import com.nexus.inventory.dto.ReservationRequest;
import com.nexus.inventory.dto.StockUpdateRequest;
import com.nexus.inventory.mapper.InventoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {
    
    private final InventoryStockRepository stockRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final InventoryMapper inventoryMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final int SOFT_RESERVATION_TTL_MINUTES = 15;
    
    /**
     * Get inventory by product ID
     */
    @Cacheable(value = "inventory-stock", key = "#productId")
    public InventoryStockDto getInventoryByProductId(String productId) {
        InventoryStock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + productId));
        return inventoryMapper.toDto(stock);
    }
    
    /**
     * Check if product has available stock
     */
    public boolean checkAvailability(String productId, Integer quantity) {
        // Check soft reservations in Redis first
        String reservationKey = CacheKeys.INVENTORY_SOFT_RESERVATION_PREFIX + productId;
        Integer softReserved = (Integer) redisTemplate.opsForValue().get(reservationKey);
        
        InventoryStock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + productId));
        
        int actualSoftReserved = softReserved != null ? softReserved : stock.getSoftReservedStock();
        int available = stock.getTotalStock() - stock.getReservedStock() - actualSoftReserved;
        
        return available >= quantity;
    }
    
    /**
     * Bulk stock availability check
     */
    public Map<String, Boolean> bulkCheckAvailability(BulkStockCheckRequest request) {
        Map<String, Boolean> availability = new HashMap<>();
        
        for (BulkStockCheckRequest.StockCheckItem item : request.getItems()) {
            boolean isAvailable = checkAvailability(item.getProductId(), item.getQuantity());
            availability.put(item.getProductId(), isAvailable);
        }
        
        return availability;
    }
    
    /**
     * Add stock to inventory
     */
    @Transactional
    @CacheEvict(value = "inventory-stock", key = "#request.productId")
    public InventoryStockDto addStock(StockUpdateRequest request, String sellerId) {
        InventoryStock stock = stockRepository.findByProductIdWithLock(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + request.getProductId()));
        
        int previousStock = stock.getTotalStock();
        stock.setTotalStock(stock.getTotalStock() + request.getQuantity());
        stock.setAvailableStock(stock.getAvailableStock() + request.getQuantity());
        stock.setOutOfStock(false);
        stock.setLowStockAlert(stock.isLowStock());
        
        InventoryStock updated = stockRepository.save(stock);
        
        // Record transaction
        recordTransaction(stock, InventoryTransaction.TransactionType.STOCK_IN, 
                request.getQuantity(), previousStock, stock.getTotalStock(), request.getNotes(), null, null);
        
        // Publish event
        publishInventoryEvent("STOCK_UPDATED", updated);
        
        return inventoryMapper.toDto(updated);
    }
    
    /**
     * Reduce stock from inventory
     */
    @Transactional
    @CacheEvict(value = "inventory-stock", key = "#request.productId")
    public InventoryStockDto reduceStock(StockUpdateRequest request, String sellerId) {
        InventoryStock stock = stockRepository.findByProductIdWithLock(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + request.getProductId()));
        
        if (stock.getAvailableStock() < request.getQuantity()) {
            throw new InsufficientStockException(request.getProductId(), request.getQuantity(), stock.getAvailableStock());
        }
        
        int previousStock = stock.getTotalStock();
        stock.setTotalStock(stock.getTotalStock() - request.getQuantity());
        stock.setAvailableStock(stock.getAvailableStock() - request.getQuantity());
        stock.setOutOfStock(stock.getTotalStock() <= 0);
        stock.setLowStockAlert(stock.isLowStock());
        
        InventoryStock updated = stockRepository.save(stock);
        
        // Record transaction
        recordTransaction(stock, InventoryTransaction.TransactionType.STOCK_OUT, 
                request.getQuantity(), previousStock, stock.getTotalStock(), request.getNotes(), null, null);
        
        // Publish event
        publishInventoryEvent("STOCK_UPDATED", updated);
        
        if (updated.getLowStockAlert()) {
            publishInventoryEvent("LOW_STOCK_ALERT", updated);
        }

        return inventoryMapper.toDto(updated);
    }

    /**
     * Create soft reservation (for cart items)
     * Stored in Redis with TTL
     */
    @Transactional
    public void createSoftReservation(ReservationRequest request) {
        log.info("Creating soft reservation for product: {}, quantity: {}", request.getProductId(), request.getQuantity());

        InventoryStock stock = stockRepository.findByProductIdWithLock(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + request.getProductId()));

        if (!checkAvailability(request.getProductId(), request.getQuantity())) {
            throw new InsufficientStockException(request.getProductId(), request.getQuantity(), stock.getActualAvailableStock());
        }

        // Store in Redis
        String reservationKey = CacheKeys.INVENTORY_SOFT_RESERVATION_PREFIX + request.getUserId() + ":" + request.getProductId();
        int ttl = request.getTtlMinutes() != null ? request.getTtlMinutes() : SOFT_RESERVATION_TTL_MINUTES;

        redisTemplate.opsForValue().set(reservationKey, request.getQuantity(), ttl, TimeUnit.MINUTES);

        // Update soft reserved count in DB
        stock.setSoftReservedStock(stock.getSoftReservedStock() + request.getQuantity());
        stockRepository.save(stock);

        // Record transaction
        recordTransaction(stock, InventoryTransaction.TransactionType.SOFT_RESERVE,
                request.getQuantity(), null, null, "Soft reservation", request.getUserId(), request.getOrderId());

        log.info("Soft reservation created successfully for product: {}", request.getProductId());
    }

    /**
     * Release soft reservation
     */
    @Transactional
    public void releaseSoftReservation(String userId, String productId, Integer quantity) {
        log.info("Releasing soft reservation for product: {}, quantity: {}", productId, quantity);

        InventoryStock stock = stockRepository.findByProductIdWithLock(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + productId));

        // Remove from Redis
        String reservationKey = CacheKeys.INVENTORY_SOFT_RESERVATION_PREFIX + userId + ":" + productId;
        redisTemplate.delete(reservationKey);

        // Update soft reserved count in DB
        stock.setSoftReservedStock(Math.max(0, stock.getSoftReservedStock() - quantity));
        stockRepository.save(stock);

        // Record transaction
        recordTransaction(stock, InventoryTransaction.TransactionType.SOFT_RELEASE,
                quantity, null, null, "Soft reservation released", userId, null);
    }

    /**
     * Create hard reservation (for confirmed orders)
     * Stored in PostgreSQL
     */
    @Transactional
    @CacheEvict(value = "inventory-stock", key = "#request.productId")
    public void createHardReservation(ReservationRequest request) {
        log.info("Creating hard reservation for product: {}, order: {}", request.getProductId(), request.getOrderId());

        InventoryStock stock = stockRepository.findByProductIdWithLock(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + request.getProductId()));

        // Check if sufficient stock is available
        if (stock.getActualAvailableStock() < request.getQuantity()) {
            throw new InsufficientStockException(request.getProductId(), request.getQuantity(), stock.getActualAvailableStock());
        }

        // Move from soft to hard reservation
        if (request.getUserId() != null) {
            releaseSoftReservation(request.getUserId(), request.getProductId(), request.getQuantity());
        }

        stock.setReservedStock(stock.getReservedStock() + request.getQuantity());
        stock.setAvailableStock(stock.getAvailableStock() - request.getQuantity());
        stock.setOutOfStock(stock.getActualAvailableStock() <= 0);
        stock.setLowStockAlert(stock.isLowStock());

        stockRepository.save(stock);

        // Record transaction
        recordTransaction(stock, InventoryTransaction.TransactionType.HARD_RESERVE,
                request.getQuantity(), null, null, "Hard reservation for order", request.getUserId(), request.getOrderId());

        publishInventoryEvent("STOCK_RESERVED", stock);

        log.info("Hard reservation created successfully for product: {}", request.getProductId());
    }

    /**
     * Release hard reservation (order cancelled/failed)
     */
    @Transactional
    @CacheEvict(value = "inventory-stock", key = "#productId")
    public void releaseHardReservation(String productId, String orderId, Integer quantity) {
        log.info("Releasing hard reservation for product: {}, order: {}", productId, orderId);

        InventoryStock stock = stockRepository.findByProductIdWithLock(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + productId));

        stock.setReservedStock(Math.max(0, stock.getReservedStock() - quantity));
        stock.setAvailableStock(stock.getAvailableStock() + quantity);
        stock.setOutOfStock(false);
        stock.setLowStockAlert(stock.isLowStock());

        stockRepository.save(stock);

        // Record transaction
        recordTransaction(stock, InventoryTransaction.TransactionType.HARD_RELEASE,
                quantity, null, null, "Hard reservation released", null, orderId);

        publishInventoryEvent("RESERVATION_RELEASED", stock);
    }

    /**
     * Get inventory for seller
     */
    public Page<InventoryStockDto> getSellerInventory(String sellerId, Pageable pageable) {
        return stockRepository.findBySellerId(sellerId, pageable)
                .map(inventoryMapper::toDto);
    }

    /**
     * Get low stock items
     */
    public List<InventoryStockDto> getLowStockItems(String sellerId) {
        List<InventoryStock> lowStockItems = sellerId != null
                ? stockRepository.findLowStockItemsBySeller(sellerId)
                : stockRepository.findLowStockItems();

        return lowStockItems.stream()
                .map(inventoryMapper::toDto)
                .toList();
    }

    /**
     * Initialize inventory for new product
     */
    @Transactional
    public InventoryStockDto initializeInventory(String productId, String sellerId, Integer initialStock) {
        InventoryStock stock = InventoryStock.builder()
                .productId(productId)
                .sellerId(sellerId)
                .totalStock(initialStock != null ? initialStock : 0)
                .availableStock(initialStock != null ? initialStock : 0)
                .reservedStock(0)
                .softReservedStock(0)
                .reorderLevel(10)
                .maxStockLevel(1000)
                .warehouseLocation("DEFAULT")
                .outOfStock(initialStock == null || initialStock == 0)
                .lowStockAlert(initialStock != null && initialStock <= 10)
                .build();

        InventoryStock saved = stockRepository.save(stock);

        log.info("Inventory initialized for product: {} with stock: {}", productId, initialStock);

        return inventoryMapper.toDto(saved);
    }

    /**
     * Record inventory transaction
     */
    private void recordTransaction(InventoryStock stock, InventoryTransaction.TransactionType type,
                                   Integer quantity, Integer previousStock, Integer newStock,
                                   String notes, String userId, String orderId) {
        InventoryTransaction transaction = InventoryTransaction.builder()
                .productId(stock.getProductId())
                .sellerId(stock.getSellerId())
                .transactionType(type)
                .quantity(quantity)
                .previousStock(previousStock)
                .newStock(newStock)
                .warehouseLocation(stock.getWarehouseLocation())
                .notes(notes)
                .userId(userId)
                .orderId(orderId)
                .build();

        transactionRepository.save(transaction);
    }

    /**
     * Publish inventory event to Kafka
     */
    private void publishInventoryEvent(String eventType, InventoryStock stock) {
        Map<String, Object> event = Map.of(
                "eventType", eventType,
                "productId", stock.getProductId(),
                "sellerId", stock.getSellerId(),
                "totalStock", stock.getTotalStock(),
                "availableStock", stock.getActualAvailableStock(),
                "lowStockAlert", stock.getLowStockAlert(),
                "outOfStock", stock.getOutOfStock(),
                "timestamp", System.currentTimeMillis()
        );

        kafkaTemplate.send(KafkaTopics.INVENTORY_EVENTS, stock.getProductId(), event);
    }
}
