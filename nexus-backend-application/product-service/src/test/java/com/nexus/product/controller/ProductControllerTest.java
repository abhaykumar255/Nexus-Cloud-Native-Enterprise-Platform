package com.nexus.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.common.enums.ProductStatus;
import com.nexus.product.dto.CreateProductRequest;
import com.nexus.product.dto.ProductDto;
import com.nexus.product.dto.UpdateProductRequest;
import com.nexus.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private ProductDto testProductDto;
    private CreateProductRequest createRequest;

    @BeforeEach
    void setUp() {
        testProductDto = ProductDto.builder()
                .id("prod-1")
                .name("Test Product")
                .sku("SKU-001")
                .sellerId("seller-1")
                .categoryId("cat-1")
                .price(new BigDecimal("99.99"))
                .status(ProductStatus.DRAFT)
                .productType("PRODUCT")
                .build();

        createRequest = CreateProductRequest.builder()
                .name("Test Product")
                .sku("SKU-001")
                .sellerId("seller-1")
                .categoryId("cat-1")
                .price(new BigDecimal("99.99"))
                .productType("PRODUCT")
                .build();
    }

    @Test
    void createProduct_Success() throws Exception {
        // Given
        when(productService.createProduct(any(CreateProductRequest.class)))
                .thenReturn(testProductDto);

        // When & Then
        mockMvc.perform(post("/api/v1/products")
                        .header("X-User-Id", "user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("prod-1"))
                .andExpect(jsonPath("$.data.name").value("Test Product"));
    }

    @Test
    void getProduct_Success() throws Exception {
        // Given
        when(productService.getProduct(anyString())).thenReturn(testProductDto);

        // When & Then
        mockMvc.perform(get("/api/v1/products/prod-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("prod-1"))
                .andExpect(jsonPath("$.data.name").value("Test Product"));
    }

    @Test
    void updateProduct_Success() throws Exception {
        // Given
        UpdateProductRequest updateRequest = UpdateProductRequest.builder()
                .name("Updated Product")
                .price(new BigDecimal("149.99"))
                .build();

        when(productService.updateProduct(anyString(), any(UpdateProductRequest.class)))
                .thenReturn(testProductDto);

        // When & Then
        mockMvc.perform(put("/api/v1/products/prod-1")
                        .header("X-User-Id", "user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void deleteProduct_Success() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/products/prod-1")
                        .header("X-User-Id", "user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void getPublishedProducts_Success() throws Exception {
        // Given
        Page<ProductDto> productPage = new PageImpl<>(Arrays.asList(testProductDto));
        when(productService.getPublishedProducts(any(PageRequest.class)))
                .thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void searchProducts_Success() throws Exception {
        // Given
        Page<ProductDto> productPage = new PageImpl<>(Arrays.asList(testProductDto));
        when(productService.searchProducts(anyString(), any(PageRequest.class)))
                .thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/v1/products/search")
                        .param("q", "test")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void publishProduct_Success() throws Exception {
        // Given
        when(productService.publishProduct(anyString())).thenReturn(testProductDto);

        // When & Then
        mockMvc.perform(post("/api/v1/products/prod-1/publish")
                        .header("X-User-Id", "user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void getFeaturedProducts_Success() throws Exception {
        // Given
        when(productService.getFeaturedProducts()).thenReturn(Arrays.asList(testProductDto));

        // When & Then
        mockMvc.perform(get("/api/v1/products/featured"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getTrendingProducts_Success() throws Exception {
        // Given
        when(productService.getTrendingProducts()).thenReturn(Arrays.asList(testProductDto));

        // When & Then
        mockMvc.perform(get("/api/v1/products/trending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void filterProducts_Success() throws Exception {
        // Given
        Page<ProductDto> productPage = new PageImpl<>(Arrays.asList(testProductDto));
        when(productService.filterProducts(
                anyString(),
                any(BigDecimal.class),
                any(BigDecimal.class),
                anyString(),
                any(PageRequest.class)
        )).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/v1/products/filter")
                        .param("categoryId", "cat-1")
                        .param("minPrice", "0")
                        .param("maxPrice", "1000")
                        .param("productType", "PRODUCT")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void getProductsByCategory_Success() throws Exception {
        // Given
        Page<ProductDto> productPage = new PageImpl<>(Arrays.asList(testProductDto));
        when(productService.getProductsByCategory(anyString(), any(PageRequest.class)))
                .thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/v1/products/category/cat-1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void getProductsBySeller_Success() throws Exception {
        // Given
        Page<ProductDto> productPage = new PageImpl<>(Arrays.asList(testProductDto));
        when(productService.getProductsBySeller(anyString(), any(PageRequest.class)))
                .thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/v1/products/seller/seller-1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}

