package com.nexus.product.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Test
    void testCategoryBuilder() {
        // When
        Category category = Category.builder()
                .id("cat-1")
                .name("Electronics")
                .slug("electronics")
                .description("Electronic products")
                .imageUrl("category.jpg")
                .displayOrder(1)
                .isActive(true)
                .categoryType("PRODUCT")
                .build();

        // Then
        assertThat(category.getId()).isEqualTo("cat-1");
        assertThat(category.getName()).isEqualTo("Electronics");
        assertThat(category.getSlug()).isEqualTo("electronics");
        assertThat(category.getDescription()).isEqualTo("Electronic products");
        assertThat(category.getImageUrl()).isEqualTo("category.jpg");
        assertThat(category.getDisplayOrder()).isEqualTo(1);
        assertThat(category.getIsActive()).isTrue();
        assertThat(category.getCategoryType()).isEqualTo("PRODUCT");
    }

    @Test
    void testCategoryDefaultValues() {
        // When
        Category category = Category.builder()
                .name("Electronics")
                .build();

        // Then
        assertThat(category.getDisplayOrder()).isEqualTo(0);
        assertThat(category.getIsActive()).isTrue();
        assertThat(category.getCategoryType()).isEqualTo("PRODUCT");
        assertThat(category.getSubcategories()).isEmpty();
    }

    @Test
    void testCategoryHierarchy() {
        // Given - Parent category
        Category parent = Category.builder()
                .id("cat-parent")
                .name("Electronics")
                .slug("electronics")
                .build();

        // And - Child categories
        Category child1 = Category.builder()
                .id("cat-child-1")
                .name("Phones")
                .slug("phones")
                .parent(parent)
                .build();

        Category child2 = Category.builder()
                .id("cat-child-2")
                .name("Laptops")
                .slug("laptops")
                .parent(parent)
                .build();

        List<Category> subcategories = new ArrayList<>();
        subcategories.add(child1);
        subcategories.add(child2);
        parent.setSubcategories(subcategories);

        // Then
        assertThat(parent.getSubcategories()).hasSize(2);
        assertThat(child1.getParent()).isEqualTo(parent);
        assertThat(child2.getParent()).isEqualTo(parent);
        assertThat(parent.getSubcategories()).contains(child1, child2);
    }

    @Test
    void testCategoryTypes() {
        // Given
        Category productCategory = Category.builder()
                .name("Electronics")
                .categoryType("PRODUCT")
                .build();

        Category foodCategory = Category.builder()
                .name("Vegetables")
                .categoryType("FOOD")
                .build();

        Category groceryCategory = Category.builder()
                .name("Grains")
                .categoryType("GROCERY")
                .build();

        // Then
        assertThat(productCategory.getCategoryType()).isEqualTo("PRODUCT");
        assertThat(foodCategory.getCategoryType()).isEqualTo("FOOD");
        assertThat(groceryCategory.getCategoryType()).isEqualTo("GROCERY");
    }

    @Test
    void testCategoryActivation() {
        // Given
        Category category = Category.builder()
                .name("Electronics")
                .isActive(true)
                .build();

        // When
        category.setIsActive(false);

        // Then
        assertThat(category.getIsActive()).isFalse();
    }
}

