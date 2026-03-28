package com.nexus.inventory.domain.entity;

import com.nexus.common.enums.InventoryReservationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Inventory transaction entity - audit trail for all inventory movements
 */
@Entity
@Table(name = "inventory_transactions", indexes = {
        @Index(name = "idx_product_id_tx", columnList = "product_id"),
        @Index(name = "idx_order_id_tx", columnList = "order_id"),
        @Index(name = "idx_transaction_type", columnList = "transaction_type"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "product_id", nullable = false)
    private String productId;
    
    @Column(name = "seller_id", nullable = false)
    private String sellerId;
    
    @Column(name = "order_id")
    private String orderId;
    
    @Column(name = "user_id")
    private String userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 30)
    private TransactionType transactionType;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "previous_stock")
    private Integer previousStock;
    
    @Column(name = "new_stock")
    private Integer newStock;
    
    @Column(name = "warehouse_location", length = 100)
    private String warehouseLocation;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_type", length = 20)
    private InventoryReservationType reservationType;
    
    @Column(length = 500)
    private String notes;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    public enum TransactionType {
        STOCK_IN,           // Add inventory
        STOCK_OUT,          // Remove inventory
        SOFT_RESERVE,       // Soft reservation (cart)
        SOFT_RELEASE,       // Release soft reservation
        HARD_RESERVE,       // Hard reservation (order confirmed)
        HARD_RELEASE,       // Release hard reservation
        ADJUSTMENT,         // Manual adjustment
        DAMAGE,             // Damaged goods
        RETURN,             // Customer return
        TRANSFER            // Warehouse transfer
    }
}

