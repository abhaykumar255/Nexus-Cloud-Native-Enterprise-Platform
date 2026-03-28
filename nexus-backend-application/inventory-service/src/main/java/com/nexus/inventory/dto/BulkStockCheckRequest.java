package com.nexus.inventory.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkStockCheckRequest {
    
    @NotEmpty(message = "Product items cannot be empty")
    private List<StockCheckItem> items;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockCheckItem {
        private String productId;
        private Integer quantity;
    }
}

