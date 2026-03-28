package com.nexus.product.dto;

import com.nexus.common.enums.ProductStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductDtoTest {

    @Test
    void testProductDtoBuilder() {
        // Given
        List<String> imageUrls = Arrays.asList("image1.jpg", "image2.jpg");
        List<String> tags = Arrays.asList("new", "sale");
        
        ProductAttributeDto attr1 = ProductAttributeDto.builder()
                .attributeName("Color")
                .attributeValue("Red")
                .build();
        
        List<ProductAttributeDto> attributes = Arrays.asList(attr1);

        // When
        ProductDto dto = ProductDto.builder()
                .id("prod-1")
                .name("Test Product")
                .sku("SKU-001")
                .description("Test Description")
                .sellerId("seller-1")
                .categoryId("cat-1")
                .categoryName("Electronics")
                .price(new BigDecimal("99.99"))
                .discountPrice(new BigDecimal("79.99"))
                .discountPercentage(new BigDecimal("20.00"))
                .status(ProductStatus.PUBLISHED)
                .productType("PRODUCT")
                .brand("TestBrand")
                .manufacturer("TestManufacturer")
                .imageUrls(imageUrls)
                .tags(tags)
                .attributes(attributes)
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
        assertThat(dto.getId()).isEqualTo("prod-1");
        assertThat(dto.getName()).isEqualTo("Test Product");
        assertThat(dto.getSku()).isEqualTo("SKU-001");
        assertThat(dto.getSellerId()).isEqualTo("seller-1");
        assertThat(dto.getCategoryId()).isEqualTo("cat-1");
        assertThat(dto.getCategoryName()).isEqualTo("Electronics");
        assertThat(dto.getPrice()).isEqualByComparingTo(new BigDecimal("99.99"));
        assertThat(dto.getStatus()).isEqualTo(ProductStatus.PUBLISHED);
        assertThat(dto.getProductType()).isEqualTo("PRODUCT");
        assertThat(dto.getImageUrls()).hasSize(2);
        assertThat(dto.getTags()).hasSize(2);
        assertThat(dto.getAttributes()).hasSize(1);
    }

    @Test
    void testProductDtoSettersAndGetters() {
        // Given
        ProductDto dto = new ProductDto();

        // When
        dto.setId("prod-1");
        dto.setName("Test Product");
        dto.setPrice(new BigDecimal("99.99"));
        dto.setStatus(ProductStatus.DRAFT);

        // Then
        assertThat(dto.getId()).isEqualTo("prod-1");
        assertThat(dto.getName()).isEqualTo("Test Product");
        assertThat(dto.getPrice()).isEqualByComparingTo(new BigDecimal("99.99"));
        assertThat(dto.getStatus()).isEqualTo(ProductStatus.DRAFT);
    }
}

