package com.nexus.search.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Product document for Elasticsearch
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {
    
    private String id;
    private String name;
    private String description;
    private String shortDescription;
    private String brand;
    private String category;
    private String subCategory;
    private String sellerId;
    private String sellerName;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private Integer discountPercentage;
    private Integer stockQuantity;
    private Boolean inStock;
    private String sku;
    private List<String> tags;
    private List<String> images;
    private BigDecimal rating;
    private Integer reviewCount;
    private String status;
    private Instant createdAt;
}

