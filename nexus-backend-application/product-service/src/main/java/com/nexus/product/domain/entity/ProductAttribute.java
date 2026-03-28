package com.nexus.product.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Product attributes (e.g., Size: Medium, Color: Red)
 */
@Entity
@Table(name = "product_attributes", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(nullable = false, length = 50)
    private String attributeName; // e.g., "Size", "Color", "Material"
    
    @Column(nullable = false, length = 100)
    private String attributeValue; // e.g., "Large", "Red", "Cotton"
}

