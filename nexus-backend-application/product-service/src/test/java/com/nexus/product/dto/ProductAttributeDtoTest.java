package com.nexus.product.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductAttributeDtoTest {

    @Test
    void testProductAttributeDtoBuilder() {
        // When
        ProductAttributeDto dto = ProductAttributeDto.builder()
                .attributeName("Color")
                .attributeValue("Red")
                .build();

        // Then
        assertThat(dto.getAttributeName()).isEqualTo("Color");
        assertThat(dto.getAttributeValue()).isEqualTo("Red");
    }

    @Test
    void testProductAttributeDtoSetters() {
        // Given
        ProductAttributeDto dto = new ProductAttributeDto();

        // When
        dto.setAttributeName("Size");
        dto.setAttributeValue("Large");

        // Then
        assertThat(dto.getAttributeName()).isEqualTo("Size");
        assertThat(dto.getAttributeValue()).isEqualTo("Large");
    }

    @Test
    void testMultipleAttributes() {
        // Given
        ProductAttributeDto color = ProductAttributeDto.builder()
                .attributeName("Color")
                .attributeValue("Blue")
                .build();

        ProductAttributeDto size = ProductAttributeDto.builder()
                .attributeName("Size")
                .attributeValue("Medium")
                .build();

        // Then
        assertThat(color.getAttributeName()).isEqualTo("Color");
        assertThat(color.getAttributeValue()).isEqualTo("Blue");
        assertThat(size.getAttributeName()).isEqualTo("Size");
        assertThat(size.getAttributeValue()).isEqualTo("Medium");
    }
}

