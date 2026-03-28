package com.nexus.product.dto;

import com.nexus.common.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    
    private String id;
    private String name;
    private String sku;
    private String description;
    private String sellerId;
    private String categoryId;
    private String categoryName;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private BigDecimal discountPercentage;
    private ProductStatus status;
    private String productType;
    private String brand;
    private String manufacturer;
    private List<String> imageUrls;
    private List<String> tags;
    private List<ProductAttributeDto> attributes;
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;
    private Boolean isVegetarian;
    private Boolean isVegan;
    private Boolean containsEgg;
    private Boolean containsDairy;
    private BigDecimal averageRating;
    private Integer totalReviews;
    private Integer totalSales;
    private Integer viewCount;
    private Boolean isFeatured;
    private Boolean isTrending;
    private Instant createdAt;
    private Instant updatedAt;
}

