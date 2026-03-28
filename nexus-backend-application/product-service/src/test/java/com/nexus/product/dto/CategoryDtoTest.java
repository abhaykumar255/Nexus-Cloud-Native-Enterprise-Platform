package com.nexus.product.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryDtoTest {

    @Test
    void testCategoryDtoBuilder() {
        // When
        CategoryDto dto = CategoryDto.builder()
                .id("cat-1")
                .name("Electronics")
                .slug("electronics")
                .description("Electronic products")
                .imageUrl("category.jpg")
                .parentId("parent-1")
                .displayOrder(1)
                .isActive(true)
                .categoryType("PRODUCT")
                .build();

        // Then
        assertThat(dto.getId()).isEqualTo("cat-1");
        assertThat(dto.getName()).isEqualTo("Electronics");
        assertThat(dto.getSlug()).isEqualTo("electronics");
        assertThat(dto.getDescription()).isEqualTo("Electronic products");
        assertThat(dto.getImageUrl()).isEqualTo("category.jpg");
        assertThat(dto.getParentId()).isEqualTo("parent-1");
        assertThat(dto.getDisplayOrder()).isEqualTo(1);
        assertThat(dto.getIsActive()).isTrue();
        assertThat(dto.getCategoryType()).isEqualTo("PRODUCT");
    }

    @Test
    void testCategoryDtoSettersAndGetters() {
        // Given
        CategoryDto dto = new CategoryDto();

        // When
        dto.setId("cat-1");
        dto.setName("Food");
        dto.setSlug("food");
        dto.setIsActive(true);

        // Then
        assertThat(dto.getId()).isEqualTo("cat-1");
        assertThat(dto.getName()).isEqualTo("Food");
        assertThat(dto.getSlug()).isEqualTo("food");
        assertThat(dto.getIsActive()).isTrue();
    }
}

