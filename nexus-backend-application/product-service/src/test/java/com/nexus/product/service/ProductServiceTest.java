package com.nexus.product.service;

import com.nexus.common.enums.ProductStatus;
import com.nexus.common.exception.DuplicateResourceException;
import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.product.domain.entity.Category;
import com.nexus.product.domain.entity.Product;
import com.nexus.product.domain.entity.ProductAttribute;
import com.nexus.product.domain.repository.CategoryRepository;
import com.nexus.product.domain.repository.ProductRepository;
import com.nexus.product.dto.CreateProductRequest;
import com.nexus.product.dto.ProductAttributeDto;
import com.nexus.product.dto.ProductDto;
import com.nexus.product.dto.UpdateProductRequest;
import com.nexus.product.event.ProductEvent;
import com.nexus.product.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private KafkaTemplate<String, ProductEvent> kafkaTemplate;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductDto testProductDto;
    private Category testCategory;
    private CreateProductRequest createRequest;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id("cat-1")
                .name("Electronics")
                .build();

        testProduct = Product.builder()
                .id("prod-1")
                .name("Test Product")
                .sku("SKU-001")
                .sellerId("seller-1")
                .category(testCategory)
                .price(new BigDecimal("99.99"))
                .status(ProductStatus.DRAFT)
                .productType("PRODUCT")
                .viewCount(0)
                .build();

        testProductDto = ProductDto.builder()
                .id("prod-1")
                .name("Test Product")
                .sku("SKU-001")
                .sellerId("seller-1")
                .categoryId("cat-1")
                .price(new BigDecimal("99.99"))
                .status(ProductStatus.DRAFT)
                .build();

        ProductAttributeDto attrDto = ProductAttributeDto.builder()
                .attributeName("Color")
                .attributeValue("Red")
                .build();

        createRequest = CreateProductRequest.builder()
                .name("Test Product")
                .sku("SKU-001")
                .sellerId("seller-1")
                .categoryId("cat-1")
                .price(new BigDecimal("99.99"))
                .productType("PRODUCT")
                .attributes(Arrays.asList(attrDto))
                .build();
    }

    @Test
    void createProduct_Success() {
        // Given
        when(productRepository.findBySku(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.findById(anyString())).thenReturn(Optional.of(testCategory));
        when(productMapper.toEntity(any(CreateProductRequest.class))).thenReturn(testProduct);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        ProductDto result = productService.createProduct(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository).findBySku("SKU-001");
        verify(categoryRepository).findById("cat-1");
        verify(productRepository).save(any(Product.class));
        verify(kafkaTemplate).send(anyString(), anyString(), any(ProductEvent.class));
    }

    @Test
    void createProduct_DuplicateSku_ThrowsException() {
        // Given
        when(productRepository.findBySku(anyString())).thenReturn(Optional.of(testProduct));

        // When & Then
        assertThatThrownBy(() -> productService.createProduct(createRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("SKU");

        verify(productRepository).findBySku("SKU-001");
        verify(productRepository, never()).save(any());
    }

    @Test
    void createProduct_CategoryNotFound_ThrowsException() {
        // Given
        when(productRepository.findBySku(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.findById(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.createProduct(createRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category");

        verify(categoryRepository).findById("cat-1");
        verify(productRepository, never()).save(any());
    }

    @Test
    void getProduct_Success() {
        // Given
        when(productRepository.findById(anyString())).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        ProductDto result = productService.getProduct("prod-1");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("prod-1");
        verify(productRepository).findById("prod-1");
        verify(productRepository).save(any(Product.class)); // View count increment
    }

    @Test
    void getProduct_NotFound_ThrowsException() {
        // Given
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.getProduct("prod-999"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product");

        verify(productRepository).findById("prod-999");
    }

    @Test
    void updateProduct_Success() {
        // Given
        UpdateProductRequest updateRequest = UpdateProductRequest.builder()
                .name("Updated Product")
                .price(new BigDecimal("149.99"))
                .build();

        when(productRepository.findById(anyString())).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        ProductDto result = productService.updateProduct("prod-1", updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(productRepository).findById("prod-1");
        verify(productRepository).save(any(Product.class));
        verify(kafkaTemplate).send(anyString(), anyString(), any(ProductEvent.class));
    }

    @Test
    void updateProduct_NotFound_ThrowsException() {
        // Given
        UpdateProductRequest updateRequest = UpdateProductRequest.builder()
                .name("Updated Product")
                .build();

        when(productRepository.findById(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.updateProduct("prod-999", updateRequest))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productRepository).findById("prod-999");
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_Success() {
        // Given
        when(productRepository.findById(anyString())).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).delete(any(Product.class));

        // When
        productService.deleteProduct("prod-1");

        // Then
        verify(productRepository).findById("prod-1");
        verify(productRepository).delete(testProduct);
        verify(kafkaTemplate).send(anyString(), anyString(), any(ProductEvent.class));
    }

    @Test
    void deleteProduct_NotFound_ThrowsException() {
        // Given
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.deleteProduct("prod-999"))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productRepository).findById("prod-999");
        verify(productRepository, never()).delete(any());
    }

    @Test
    void publishProduct_Success() {
        // Given
        when(productRepository.findById(anyString())).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        ProductDto result = productService.publishProduct("prod-1");

        // Then
        assertThat(result).isNotNull();
        verify(productRepository).findById("prod-1");
        verify(productRepository).save(any(Product.class));
        verify(kafkaTemplate).send(anyString(), anyString(), any(ProductEvent.class));
    }

    @Test
    void getPublishedProducts_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(testProduct));

        when(productRepository.findByStatus(any(ProductStatus.class), any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        Page<ProductDto> result = productService.getPublishedProducts(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(productRepository).findByStatus(ProductStatus.PUBLISHED, pageable);
    }

    @Test
    void searchProducts_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(testProduct));

        when(productRepository.searchProducts(anyString(), any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        Page<ProductDto> result = productService.searchProducts("test", pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(productRepository).searchProducts("test", pageable);
    }

    @Test
    void getFeaturedProducts_Success() {
        // Given
        when(productRepository.findByIsFeaturedTrue()).thenReturn(Arrays.asList(testProduct));
        when(productMapper.toDtoList(anyList())).thenReturn(Arrays.asList(testProductDto));

        // When
        List<ProductDto> result = productService.getFeaturedProducts();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(productRepository).findByIsFeaturedTrue();
    }

    @Test
    void filterProducts_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(testProduct));

        when(productRepository.findByFilters(
                any(ProductStatus.class),
                anyString(),
                any(BigDecimal.class),
                any(BigDecimal.class),
                anyString(),
                any(Pageable.class)
        )).thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        Page<ProductDto> result = productService.filterProducts(
                "cat-1",
                new BigDecimal("0"),
                new BigDecimal("1000"),
                "PRODUCT",
                pageable
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(productRepository).findByFilters(
                ProductStatus.PUBLISHED,
                "cat-1",
                new BigDecimal("0"),
                new BigDecimal("1000"),
                "PRODUCT",
                pageable
        );
    }

    @Test
    void getProductsByCategory_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(testProduct));

        when(productRepository.findByCategoryId(anyString(), any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        Page<ProductDto> result = productService.getProductsByCategory("cat-1", pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(productRepository).findByCategoryId("cat-1", pageable);
    }

    @Test
    void getTrendingProducts_Success() {
        // Given
        when(productRepository.findByIsTrendingTrue()).thenReturn(Arrays.asList(testProduct));
        when(productMapper.toDtoList(anyList())).thenReturn(Arrays.asList(testProductDto));

        // When
        List<ProductDto> result = productService.getTrendingProducts();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(productRepository).findByIsTrendingTrue();
    }

    @Test
    void getProductsBySeller_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(testProduct));

        when(productRepository.findBySellerId(anyString(), any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        Page<ProductDto> result = productService.getProductsBySeller("seller-1", pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(productRepository).findBySellerId("seller-1", pageable);
    }

    @Test
    void createProduct_WithoutCategory_Success() {
        // Given
        createRequest.setCategoryId(null);

        when(productRepository.findBySku(anyString())).thenReturn(Optional.empty());
        when(productMapper.toEntity(any(CreateProductRequest.class))).thenReturn(testProduct);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        ProductDto result = productService.createProduct(createRequest);

        // Then
        assertThat(result).isNotNull();
        verify(categoryRepository, never()).findById(anyString());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_WithoutAttributes_Success() {
        // Given
        createRequest.setAttributes(null);

        when(productRepository.findBySku(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.findById(anyString())).thenReturn(Optional.of(testCategory));
        when(productMapper.toEntity(any(CreateProductRequest.class))).thenReturn(testProduct);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        ProductDto result = productService.createProduct(createRequest);

        // Then
        assertThat(result).isNotNull();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_WithoutCategory_Success() {
        // Given
        UpdateProductRequest updateRequest = UpdateProductRequest.builder()
                .name("Updated Product")
                .price(new BigDecimal("149.99"))
                .build();

        when(productRepository.findById(anyString())).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        ProductDto result = productService.updateProduct("prod-1", updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(categoryRepository, never()).findById(anyString());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_WithAttributes_Success() {
        // Given
        ProductAttributeDto attr = ProductAttributeDto.builder()
                .attributeName("Size")
                .attributeValue("Large")
                .build();

        UpdateProductRequest updateRequest = UpdateProductRequest.builder()
                .name("Updated Product")
                .price(new BigDecimal("149.99"))
                .categoryId("cat-1")
                .attributes(Arrays.asList(attr))
                .build();

        when(productRepository.findById(anyString())).thenReturn(Optional.of(testProduct));
        when(categoryRepository.findById(anyString())).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(testProductDto);

        // When
        ProductDto result = productService.updateProduct("prod-1", updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(productRepository).save(any(Product.class));
        verify(kafkaTemplate).send(anyString(), anyString(), any(ProductEvent.class));
    }
}

