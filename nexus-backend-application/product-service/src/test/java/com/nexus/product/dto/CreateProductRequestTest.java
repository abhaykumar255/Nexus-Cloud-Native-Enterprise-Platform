package com.nexus.product.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateProductRequestTest {

    @Test
    void testCreateProductRequestBuilder() {
        // Given
        List<String> imageUrls = Arrays.asList("image1.jpg", "image2.jpg");
        List<String> tags = Arrays.asList("new", "sale");
        
        ProductAttributeDto attr = ProductAttributeDto.builder()
                .attributeName("Color")
                .attributeValue("Red")
                .build();
        
        List<ProductAttributeDto> attributes = Arrays.asList(attr);

        // When
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Test Product")
                .sku("SKU-001")
                .description("Test Description")
                .sellerId("seller-1")
                .categoryId("cat-1")
                .price(new BigDecimal("99.99"))
                .discountPrice(new BigDecimal("79.99"))
                .discountPercentage(new BigDecimal("20.00"))
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
                .isFeatured(true)
                .isTrending(false)
                .build();

        // Then
        assertThat(request.getName()).isEqualTo("Test Product");
        assertThat(request.getSku()).isEqualTo("SKU-001");
        assertThat(request.getDescription()).isEqualTo("Test Description");
        assertThat(request.getSellerId()).isEqualTo("seller-1");
        assertThat(request.getCategoryId()).isEqualTo("cat-1");
        assertThat(request.getPrice()).isEqualByComparingTo(new BigDecimal("99.99"));
        assertThat(request.getDiscountPrice()).isEqualByComparingTo(new BigDecimal("79.99"));
        assertThat(request.getDiscountPercentage()).isEqualByComparingTo(new BigDecimal("20.00"));
        assertThat(request.getProductType()).isEqualTo("PRODUCT");
        assertThat(request.getBrand()).isEqualTo("TestBrand");
        assertThat(request.getManufacturer()).isEqualTo("TestManufacturer");
        assertThat(request.getImageUrls()).hasSize(2);
        assertThat(request.getTags()).hasSize(2);
        assertThat(request.getAttributes()).hasSize(1);
        assertThat(request.getWeight()).isEqualTo(1000);
        assertThat(request.getIsVegetarian()).isTrue();
        assertThat(request.getIsFeatured()).isTrue();
    }

    @Test
    void testCreateProductRequestSetters() {
        // Given
        CreateProductRequest request = new CreateProductRequest();

        // When
        request.setName("New Product");
        request.setPrice(new BigDecimal("49.99"));
        request.setSellerId("seller-2");
        request.setProductType("FOOD");

        // Then
        assertThat(request.getName()).isEqualTo("New Product");
        assertThat(request.getPrice()).isEqualByComparingTo(new BigDecimal("49.99"));
        assertThat(request.getSellerId()).isEqualTo("seller-2");
        assertThat(request.getProductType()).isEqualTo("FOOD");
    }
}

