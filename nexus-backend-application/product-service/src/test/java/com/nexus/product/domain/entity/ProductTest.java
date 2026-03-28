package com.nexus.product.domain.entity;

import com.nexus.common.enums.ProductStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void testProductBuilder() {
        // Given
        Category category = Category.builder()
                .id("cat-1")
                .name("Electronics")
                .build();

        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("image1.jpg");
        imageUrls.add("image2.jpg");

        List<String> tags = new ArrayList<>();
        tags.add("new");
        tags.add("sale");

        // When
        Product product = Product.builder()
                .id("prod-1")
                .name("Test Product")
                .sku("SKU-001")
                .description("Test Description")
                .sellerId("seller-1")
                .category(category)
                .price(new BigDecimal("99.99"))
                .discountPrice(new BigDecimal("79.99"))
                .discountPercentage(new BigDecimal("20.00"))
                .status(ProductStatus.PUBLISHED)
                .productType("PRODUCT")
                .brand("TestBrand")
                .manufacturer("TestManufacturer")
                .imageUrls(imageUrls)
                .tags(tags)
                .weight(1000)
                .length(10)
                .width(5)
                .height(3)
                .isVegetarian(true)
                .isVegan(false)
                .containsEgg(false)
                .containsDairy(true)
                .averageRating(new BigDecimal("4.50"))
                .totalReviews(100)
                .totalSales(500)
                .viewCount(1000)
                .isFeatured(true)
                .isTrending(false)
                .build();

        // Then
        assertThat(product.getId()).isEqualTo("prod-1");
        assertThat(product.getName()).isEqualTo("Test Product");
        assertThat(product.getSku()).isEqualTo("SKU-001");
        assertThat(product.getDescription()).isEqualTo("Test Description");
        assertThat(product.getSellerId()).isEqualTo("seller-1");
        assertThat(product.getCategory()).isEqualTo(category);
        assertThat(product.getPrice()).isEqualByComparingTo(new BigDecimal("99.99"));
        assertThat(product.getDiscountPrice()).isEqualByComparingTo(new BigDecimal("79.99"));
        assertThat(product.getDiscountPercentage()).isEqualByComparingTo(new BigDecimal("20.00"));
        assertThat(product.getStatus()).isEqualTo(ProductStatus.PUBLISHED);
        assertThat(product.getProductType()).isEqualTo("PRODUCT");
        assertThat(product.getBrand()).isEqualTo("TestBrand");
        assertThat(product.getManufacturer()).isEqualTo("TestManufacturer");
        assertThat(product.getImageUrls()).hasSize(2);
        assertThat(product.getTags()).hasSize(2);
        assertThat(product.getWeight()).isEqualTo(1000);
        assertThat(product.getLength()).isEqualTo(10);
        assertThat(product.getWidth()).isEqualTo(5);
        assertThat(product.getHeight()).isEqualTo(3);
        assertThat(product.getIsVegetarian()).isTrue();
        assertThat(product.getIsVegan()).isFalse();
        assertThat(product.getContainsEgg()).isFalse();
        assertThat(product.getContainsDairy()).isTrue();
        assertThat(product.getAverageRating()).isEqualByComparingTo(new BigDecimal("4.50"));
        assertThat(product.getTotalReviews()).isEqualTo(100);
        assertThat(product.getTotalSales()).isEqualTo(500);
        assertThat(product.getViewCount()).isEqualTo(1000);
        assertThat(product.getIsFeatured()).isTrue();
        assertThat(product.getIsTrending()).isFalse();
    }

    @Test
    void testProductDefaultValues() {
        // When
        Product product = Product.builder()
                .name("Test Product")
                .sellerId("seller-1")
                .price(new BigDecimal("99.99"))
                .build();

        // Then
        assertThat(product.getStatus()).isEqualTo(ProductStatus.DRAFT);
        assertThat(product.getProductType()).isEqualTo("PRODUCT");
        assertThat(product.getAverageRating()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(product.getTotalReviews()).isEqualTo(0);
        assertThat(product.getTotalSales()).isEqualTo(0);
        assertThat(product.getViewCount()).isEqualTo(0);
        assertThat(product.getIsFeatured()).isFalse();
        assertThat(product.getIsTrending()).isFalse();
        assertThat(product.getImageUrls()).isEmpty();
        assertThat(product.getTags()).isEmpty();
        assertThat(product.getAttributes()).isEmpty();
    }

    @Test
    void testProductWithAttributes() {
        // Given
        Product product = Product.builder()
                .name("Test Product")
                .sellerId("seller-1")
                .price(new BigDecimal("99.99"))
                .build();

        ProductAttribute attr1 = ProductAttribute.builder()
                .id("attr-1")
                .product(product)
                .attributeName("Color")
                .attributeValue("Red")
                .build();

        ProductAttribute attr2 = ProductAttribute.builder()
                .id("attr-2")
                .product(product)
                .attributeName("Size")
                .attributeValue("Large")
                .build();

        List<ProductAttribute> attributes = new ArrayList<>();
        attributes.add(attr1);
        attributes.add(attr2);
        product.setAttributes(attributes);

        // Then
        assertThat(product.getAttributes()).hasSize(2);
        assertThat(product.getAttributes().get(0).getAttributeName()).isEqualTo("Color");
        assertThat(product.getAttributes().get(1).getAttributeName()).isEqualTo("Size");
    }
}

