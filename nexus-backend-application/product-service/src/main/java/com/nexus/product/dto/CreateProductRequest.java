package com.nexus.product.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    
    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must be less than 200 characters")
    private String name;
    
    @Size(max = 100, message = "SKU must be less than 100 characters")
    private String sku;
    
    @Size(max = 5000, message = "Description must be less than 5000 characters")
    private String description;
    
    @NotBlank(message = "Seller ID is required")
    private String sellerId;
    
    private String categoryId;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    
    @DecimalMin(value = "0.0", message = "Discount price must be 0 or greater")
    private BigDecimal discountPrice;
    
    @DecimalMin(value = "0.0", message = "Discount percentage must be 0 or greater")
    @DecimalMax(value = "100.0", message = "Discount percentage must be 100 or less")
    private BigDecimal discountPercentage;
    
    @NotBlank(message = "Product type is required")
    @Pattern(regexp = "PRODUCT|FOOD|GROCERY", message = "Product type must be PRODUCT, FOOD, or GROCERY")
    private String productType;
    
    @Size(max = 50, message = "Brand must be less than 50 characters")
    private String brand;
    
    @Size(max = 100, message = "Manufacturer must be less than 100 characters")
    private String manufacturer;
    
    private List<String> imageUrls;
    
    private List<String> tags;
    
    private List<ProductAttributeDto> attributes;
    
    @Min(value = 0, message = "Weight must be 0 or greater")
    private Integer weight;
    
    @Min(value = 0, message = "Length must be 0 or greater")
    private Integer length;
    
    @Min(value = 0, message = "Width must be 0 or greater")
    private Integer width;
    
    @Min(value = 0, message = "Height must be 0 or greater")
    private Integer height;
    
    private Boolean isVegetarian;
    private Boolean isVegan;
    private Boolean containsEgg;
    private Boolean containsDairy;
    private Boolean isFeatured;
    private Boolean isTrending;
}

