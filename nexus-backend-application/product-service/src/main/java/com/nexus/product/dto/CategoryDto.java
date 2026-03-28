package com.nexus.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private String parentId;
    private List<CategoryDto> subcategories;
    private Integer displayOrder;
    private Boolean isActive;
    private String categoryType;
    private Instant createdAt;
    private Instant updatedAt;
}

