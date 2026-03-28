package com.nexus.product.domain.entity;

import com.nexus.common.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Product entity - supports both standard products and food items
 */
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_seller_id", columnList = "seller_id"),
        @Index(name = "idx_category_id", columnList = "category_id"),
        @Index(name = "idx_sku", columnList = "sku"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_product_type", columnList = "product_type")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, length = 200)
    private String name;
    
    @Column(length = 100, unique = true)
    private String sku; // Stock Keeping Unit
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "seller_id", nullable = false)
    private String sellerId; // Reference to Seller Service
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal discountPrice;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal discountPercentage;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ProductStatus status = ProductStatus.DRAFT;
    
    // Product type: PRODUCT, FOOD, GROCERY
    @Column(name = "product_type", nullable = false, length = 20)
    @Builder.Default
    private String productType = "PRODUCT";
    
    @Column(length = 50)
    private String brand;
    
    @Column(length = 100)
    private String manufacturer;
    
    // Images stored as JSON array or separate table
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();
    
    // Tags for search (e.g., "organic", "vegan", "gluten-free")
    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    
    // Product attributes (size, color, weight, etc.)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductAttribute> attributes = new ArrayList<>();
    
    // Weight in grams
    @Column
    private Integer weight;
    
    // Dimensions in cm
    @Column
    private Integer length;
    
    @Column
    private Integer width;
    
    @Column
    private Integer height;
    
    // For food items
    @Column
    private Boolean isVegetarian;
    
    @Column
    private Boolean isVegan;
    
    @Column
    private Boolean containsEgg;
    
    @Column
    private Boolean containsDairy;
    
    // Rating and reviews
    @Column(precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;
    
    @Column
    @Builder.Default
    private Integer totalReviews = 0;
    
    @Column
    @Builder.Default
    private Integer totalSales = 0;
    
    @Column
    @Builder.Default
    private Integer viewCount = 0;
    
    @Column
    @Builder.Default
    private Boolean isFeatured = false;
    
    @Column
    @Builder.Default
    private Boolean isTrending = false;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}

