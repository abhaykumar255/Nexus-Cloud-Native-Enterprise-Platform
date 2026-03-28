package com.nexus.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStockDto {
    private String id;
    private String productId;
    private String sellerId;
    private String warehouseLocation;
    private Integer totalStock;
    private Integer availableStock;
    private Integer reservedStock;
    private Integer softReservedStock;
    private Integer actualAvailableStock;
    private Integer reorderLevel;
    private Integer maxStockLevel;
    private Boolean lowStockAlert;
    private Boolean outOfStock;
    private Instant createdAt;
    private Instant updatedAt;
}

