package com.nexus.product.domain.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductAttributeTest {

    @Test
    void testProductAttributeBuilder() {
        // Given
        Product product = Product.builder()
                .id("prod-1")
                .name("Test Product")
                .sellerId("seller-1")
                .price(new BigDecimal("99.99"))
                .build();

        // When
        ProductAttribute attribute = ProductAttribute.builder()
                .id("attr-1")
                .product(product)
                .attributeName("Color")
                .attributeValue("Red")
                .build();

        // Then
        assertThat(attribute.getId()).isEqualTo("attr-1");
        assertThat(attribute.getProduct()).isEqualTo(product);
        assertThat(attribute.getAttributeName()).isEqualTo("Color");
        assertThat(attribute.getAttributeValue()).isEqualTo("Red");
    }

    @Test
    void testProductAttributeSetters() {
        // Given
        ProductAttribute attribute = new ProductAttribute();
        Product product = Product.builder()
                .id("prod-1")
                .name("Test Product")
                .sellerId("seller-1")
                .price(new BigDecimal("99.99"))
                .build();

        // When
        attribute.setId("attr-1");
        attribute.setProduct(product);
        attribute.setAttributeName("Size");
        attribute.setAttributeValue("Large");

        // Then
        assertThat(attribute.getId()).isEqualTo("attr-1");
        assertThat(attribute.getProduct()).isEqualTo(product);
        assertThat(attribute.getAttributeName()).isEqualTo("Size");
        assertThat(attribute.getAttributeValue()).isEqualTo("Large");
    }

    @Test
    void testMultipleAttributes() {
        // Given
        Product product = Product.builder()
                .id("prod-1")
                .name("T-Shirt")
                .sellerId("seller-1")
                .price(new BigDecimal("29.99"))
                .build();

        // When
        ProductAttribute colorAttr = ProductAttribute.builder()
                .product(product)
                .attributeName("Color")
                .attributeValue("Blue")
                .build();

        ProductAttribute sizeAttr = ProductAttribute.builder()
                .product(product)
                .attributeName("Size")
                .attributeValue("Medium")
                .build();

        ProductAttribute materialAttr = ProductAttribute.builder()
                .product(product)
                .attributeName("Material")
                .attributeValue("Cotton")
                .build();

        // Then
        assertThat(colorAttr.getAttributeName()).isEqualTo("Color");
        assertThat(colorAttr.getAttributeValue()).isEqualTo("Blue");
        assertThat(sizeAttr.getAttributeName()).isEqualTo("Size");
        assertThat(sizeAttr.getAttributeValue()).isEqualTo("Medium");
        assertThat(materialAttr.getAttributeName()).isEqualTo("Material");
        assertThat(materialAttr.getAttributeValue()).isEqualTo("Cotton");
        assertThat(colorAttr.getProduct()).isEqualTo(product);
        assertThat(sizeAttr.getProduct()).isEqualTo(product);
        assertThat(materialAttr.getProduct()).isEqualTo(product);
    }
}

