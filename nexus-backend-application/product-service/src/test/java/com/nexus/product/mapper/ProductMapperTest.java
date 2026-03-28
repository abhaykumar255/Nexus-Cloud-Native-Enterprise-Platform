package com.nexus.product.mapper;

import com.nexus.common.enums.ProductStatus;
import com.nexus.product.domain.entity.Category;
import com.nexus.product.domain.entity.Product;
import com.nexus.product.domain.entity.ProductAttribute;
import com.nexus.product.dto.CreateProductRequest;
import com.nexus.product.dto.ProductAttributeDto;
import com.nexus.product.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ProductMapperImpl.class})
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    void testToDtoWithCategory() {
        // Given
        Category category = Category.builder()
                .id("cat-1")
                .name("Electronics")
                .build();

        Product product = Product.builder()
                .id("prod-1")
                .name("Test Product")
                .sku("SKU-001")
                .sellerId("seller-1")
                .category(category)
                .price(new BigDecimal("99.99"))
                .status(ProductStatus.PUBLISHED)
                .productType("PRODUCT")
                .build();

        // When
        ProductDto dto = productMapper.toDto(product);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("prod-1");
        assertThat(dto.getName()).isEqualTo("Test Product");
        assertThat(dto.getCategoryId()).isEqualTo("cat-1");
        assertThat(dto.getCategoryName()).isEqualTo("Electronics");
    }

    @Test
    void testToDtoWithoutCategory() {
        // Given
        Product product = Product.builder()
                .id("prod-2")
                .name("Product Without Category")
                .sku("SKU-002")
                .sellerId("seller-1")
                .price(new BigDecimal("49.99"))
                .status(ProductStatus.DRAFT)
                .build();

        // When
        ProductDto dto = productMapper.toDto(product);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("prod-2");
        assertThat(dto.getCategoryId()).isNull();
        assertThat(dto.getCategoryName()).isNull();
    }

    @Test
    void testToDtoList() {
        // Given
        Product product1 = Product.builder()
                .id("prod-1")
                .name("Product 1")
                .sellerId("seller-1")
                .price(new BigDecimal("99.99"))
                .build();

        Product product2 = Product.builder()
                .id("prod-2")
                .name("Product 2")
                .sellerId("seller-1")
                .price(new BigDecimal("149.99"))
                .build();

        List<Product> products = Arrays.asList(product1, product2);

        // When
        List<ProductDto> dtos = productMapper.toDtoList(products);

        // Then
        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo("prod-1");
        assertThat(dtos.get(1).getId()).isEqualTo("prod-2");
    }

    @Test
    void testToEntity() {
        // Given
        CreateProductRequest request = CreateProductRequest.builder()
                .name("New Product")
                .sku("SKU-NEW")
                .sellerId("seller-1")
                .price(new BigDecimal("199.99"))
                .productType("PRODUCT")
                .build();

        // When
        Product product = productMapper.toEntity(request);

        // Then
        assertThat(product).isNotNull();
        assertThat(product.getId()).isNull(); // ID should be ignored
        assertThat(product.getName()).isEqualTo("New Product");
        assertThat(product.getSku()).isEqualTo("SKU-NEW");
        assertThat(product.getSellerId()).isEqualTo("seller-1");
        assertThat(product.getPrice()).isEqualByComparingTo(new BigDecimal("199.99"));
    }

    @Test
    void testAttributeToDto() {
        // Given
        Product product = Product.builder()
                .id("prod-1")
                .build();

        ProductAttribute attribute = ProductAttribute.builder()
                .id("attr-1")
                .product(product)
                .attributeName("Color")
                .attributeValue("Red")
                .build();

        // When
        ProductAttributeDto dto = productMapper.attributeToDto(attribute);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getAttributeName()).isEqualTo("Color");
        assertThat(dto.getAttributeValue()).isEqualTo("Red");
    }
}

