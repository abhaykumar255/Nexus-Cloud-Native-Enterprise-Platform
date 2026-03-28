package com.nexus.inventory.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Inventory stock entity - tracks available stock for products
 */
@Entity
@Table(name = "inventory_stock", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_seller_id", columnList = "seller_id"),
        @Index(name = "idx_warehouse_location", columnList = "warehouse_location")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "product_id", nullable = false)
    private String productId;
    
    @Column(name = "seller_id", nullable = false)
    private String sellerId;
    
    @Column(name = "warehouse_location", length = 100)
    @Builder.Default
    private String warehouseLocation = "DEFAULT";
    
    @Column(nullable = false)
    @Builder.Default
    private Integer totalStock = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer availableStock = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer reservedStock = 0; // Hard reservations (confirmed orders)
    
    @Column(nullable = false)
    @Builder.Default
    private Integer softReservedStock = 0; // Soft reservations (carts)
    
    @Column(nullable = false)
    @Builder.Default
    private Integer reorderLevel = 10; // Alert when stock falls below this
    
    @Column(nullable = false)
    @Builder.Default
    private Integer maxStockLevel = 1000;
    
    @Column
    @Builder.Default
    private Boolean lowStockAlert = false;
    
    @Column
    @Builder.Default
    private Boolean outOfStock = false;
    
    @Version
    private Long version; // Optimistic locking for concurrent updates
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
    
    /**
     * Calculate available stock considering soft and hard reservations
     */
    @Transient
    public Integer getActualAvailableStock() {
        return totalStock - reservedStock - softReservedStock;
    }
    
    /**
     * Check if stock is below reorder level
     */
    @Transient
    public boolean isLowStock() {
        return getActualAvailableStock() <= reorderLevel;
    }
}

