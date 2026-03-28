package com.nexus.product.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Product category entity with hierarchical support
 */
@Entity
@Table(name = "categories", indexes = {
        @Index(name = "idx_parent_id", columnList = "parent_id"),
        @Index(name = "idx_slug", columnList = "slug")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(unique = true, length = 150)
    private String slug; // URL-friendly name
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(length = 255)
    private String imageUrl;
    
    // Hierarchical categories
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Category> subcategories = new ArrayList<>();
    
    @Column
    @Builder.Default
    private Integer displayOrder = 0;
    
    @Column
    @Builder.Default
    private Boolean isActive = true;
    
    // Category type: PRODUCT, FOOD, GROCERY
    @Column(name = "category_type", length = 20)
    @Builder.Default
    private String categoryType = "PRODUCT";
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}

