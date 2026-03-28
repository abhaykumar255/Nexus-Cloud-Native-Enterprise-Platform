package com.nexus.product.dto;

import com.nexus.common.enums.ProductStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateProductRequestTest {

    @Test
    void testUpdateProductRequestBuilder() {
        // Given
        List<String> imageUrls = Arrays.asList("image1.jpg", "image2.jpg");
        List<String> tags = Arrays.asList("updated", "sale");
        
        ProductAttributeDto attr = ProductAttributeDto.builder()
                .attributeName("Color")
                .attributeValue("Blue")
                .build();
        
        List<ProductAttributeDto> attributes = Arrays.asList(attr);

        // When
        UpdateProductRequest request = UpdateProductRequest.builder()
                .name("Updated Product")
                .description("Updated Description")
                .categoryId("cat-2")
                .price(new BigDecimal("149.99"))
                .discountPrice(new BigDecimal("129.99"))
                .discountPercentage(new BigDecimal("13.34"))
                .status(ProductStatus.PUBLISHED)
                .brand("UpdatedBrand")
                .manufacturer("UpdatedManufacturer")
                .imageUrls(imageUrls)
                .tags(tags)
                .attributes(attributes)
                .weight(2000)
                .length(20)
                .width(10)
                .height(6)
                .isVegetarian(false)
                .isVegan(false)
                .containsEgg(true)
                .containsDairy(true)
                .isFeatured(true)
                .isTrending(true)
                .build();

        // Then
        assertThat(request.getName()).isEqualTo("Updated Product");
        assertThat(request.getDescription()).isEqualTo("Updated Description");
        assertThat(request.getCategoryId()).isEqualTo("cat-2");
        assertThat(request.getPrice()).isEqualByComparingTo(new BigDecimal("149.99"));
        assertThat(request.getDiscountPrice()).isEqualByComparingTo(new BigDecimal("129.99"));
        assertThat(request.getStatus()).isEqualTo(ProductStatus.PUBLISHED);
        assertThat(request.getBrand()).isEqualTo("UpdatedBrand");
        assertThat(request.getImageUrls()).hasSize(2);
        assertThat(request.getTags()).hasSize(2);
        assertThat(request.getAttributes()).hasSize(1);
        assertThat(request.getWeight()).isEqualTo(2000);
        assertThat(request.getIsTrending()).isTrue();
    }

    @Test
    void testUpdateProductRequestSetters() {
        // Given
        UpdateProductRequest request = new UpdateProductRequest();

        // When
        request.setName("Modified Product");
        request.setPrice(new BigDecimal("79.99"));
        request.setStatus(ProductStatus.ARCHIVED);
        request.setIsFeatured(false);

        // Then
        assertThat(request.getName()).isEqualTo("Modified Product");
        assertThat(request.getPrice()).isEqualByComparingTo(new BigDecimal("79.99"));
        assertThat(request.getStatus()).isEqualTo(ProductStatus.ARCHIVED);
        assertThat(request.getIsFeatured()).isFalse();
    }
}

