package com.nexus.product.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.common.dto.PaginationInfo;
import com.nexus.product.dto.CreateProductRequest;
import com.nexus.product.dto.ProductDto;
import com.nexus.product.dto.UpdateProductRequest;
import com.nexus.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Product REST controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    /**
     * Create a new product
     * POST /api/v1/products
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            @RequestHeader("X-User-Id") String userId) {
        log.info("Create product request from user: {}", userId);
        
        ProductDto product = productService.createProduct(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", product));
    }
    
    /**
     * Get product by ID
     * GET /api/v1/products/{productId}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable String productId) {
        log.info("Get product request: {}", productId);
        
        ProductDto product = productService.getProduct(productId);
        
        return ResponseEntity.ok(ApiResponse.success(product));
    }
    
    /**
     * Update product
     * PUT /api/v1/products/{productId}
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable String productId,
            @Valid @RequestBody UpdateProductRequest request,
            @RequestHeader("X-User-Id") String userId) {
        log.info("Update product request: {} from user: {}", productId, userId);
        
        ProductDto product = productService.updateProduct(productId, request);
        
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", product));
    }
    
    /**
     * Delete product
     * DELETE /api/v1/products/{productId}
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable String productId,
            @RequestHeader("X-User-Id") String userId) {
        log.info("Delete product request: {} from user: {}", productId, userId);
        
        productService.deleteProduct(productId);
        
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
    
    /**
     * Get all published products (paginated)
     * GET /api/v1/products
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getPublishedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.info("Get published products - page: {}, size: {}", page, size);
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ProductDto> products = productService.getPublishedProducts(pageable);
        
        return ResponseEntity.ok(ApiResponse.paginated(products.getContent(), PaginationInfo.from(products)));
    }
    
    /**
     * Get products by seller
     * GET /api/v1/products/seller/{sellerId}
     */
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsBySeller(
            @PathVariable String sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Get products for seller: {}", sellerId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProductDto> products = productService.getProductsBySeller(sellerId, pageable);
        
        return ResponseEntity.ok(ApiResponse.paginated(products.getContent(), PaginationInfo.from(products)));
    }
    
    /**
     * Search products
     * GET /api/v1/products/search?q=query
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDto>>> searchProducts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Search products request - query: {}", q);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> products = productService.searchProducts(q, pageable);
        
        return ResponseEntity.ok(ApiResponse.paginated(products.getContent(), PaginationInfo.from(products)));
    }

    /**
     * Filter products
     * GET /api/v1/products/filter
     */
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<ProductDto>>> filterProducts(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String productType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filter products - category: {}, priceRange: {}-{}, type: {}",
                categoryId, minPrice, maxPrice, productType);

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> products = productService.filterProducts(categoryId, minPrice, maxPrice, productType, pageable);

        return ResponseEntity.ok(ApiResponse.paginated(products.getContent(), PaginationInfo.from(products)));
    }

    /**
     * Get products by category
     * GET /api/v1/products/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Get products for category: {}", categoryId);

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> products = productService.getProductsByCategory(categoryId, pageable);

        return ResponseEntity.ok(ApiResponse.paginated(products.getContent(), PaginationInfo.from(products)));
    }

    /**
     * Get featured products
     * GET /api/v1/products/featured
     */
    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getFeaturedProducts() {
        log.info("Get featured products request");

        List<ProductDto> products = productService.getFeaturedProducts();

        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Get trending products
     * GET /api/v1/products/trending
     */
    @GetMapping("/trending")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getTrendingProducts() {
        log.info("Get trending products request");

        List<ProductDto> products = productService.getTrendingProducts();

        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Publish product
     * POST /api/v1/products/{productId}/publish
     */
    @PostMapping("/{productId}/publish")
    public ResponseEntity<ApiResponse<ProductDto>> publishProduct(
            @PathVariable String productId,
            @RequestHeader("X-User-Id") String userId) {
        log.info("Publish product request: {} from user: {}", productId, userId);

        ProductDto product = productService.publishProduct(productId);

        return ResponseEntity.ok(ApiResponse.success("Product published successfully", product));
    }
}
