package com.nexus.product.service;

import com.nexus.common.enums.ProductStatus;
import com.nexus.common.exception.DuplicateResourceException;
import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.product.domain.entity.Category;
import com.nexus.product.domain.entity.Product;
import com.nexus.product.domain.entity.ProductAttribute;
import com.nexus.product.domain.repository.CategoryRepository;
import com.nexus.product.domain.repository.ProductRepository;
import com.nexus.product.dto.*;
import com.nexus.product.event.ProductEvent;
import com.nexus.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final String PRODUCT_CREATED_TOPIC = "product.created";
    private static final String PRODUCT_UPDATED_TOPIC = "product.updated";
    private static final String PRODUCT_DELETED_TOPIC = "product.deleted";
    
    /**
     * Create a new product
     */
    @Transactional
    public ProductDto createProduct(CreateProductRequest request) {
        log.info("Creating product: {}", request.getName());
        
        // Check if SKU already exists
        if (request.getSku() != null && productRepository.findBySku(request.getSku()).isPresent()) {
            throw new DuplicateResourceException("Product with SKU " + request.getSku() + " already exists");
        }
        
        Product product = productMapper.toEntity(request);
        
        // Set category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            product.setCategory(category);
        }
        
        // Set attributes
        if (request.getAttributes() != null && !request.getAttributes().isEmpty()) {
            List<ProductAttribute> attributes = new ArrayList<>();
            for (ProductAttributeDto attrDto : request.getAttributes()) {
                ProductAttribute attr = ProductAttribute.builder()
                        .product(product)
                        .attributeName(attrDto.getAttributeName())
                        .attributeValue(attrDto.getAttributeValue())
                        .build();
                attributes.add(attr);
            }
            product.setAttributes(attributes);
        }
        
        // Set default status to DRAFT
        product.setStatus(ProductStatus.DRAFT);
        
        product = productRepository.save(product);
        
        // Publish product created event
        publishProductEvent(ProductEvent.EventType.PRODUCT_CREATED, product);
        
        log.info("Product created successfully: {}", product.getId());
        
        return productMapper.toDto(product);
    }
    
    /**
     * Get product by ID (with caching)
     */
    @Cacheable(value = "products", key = "#productId")
    @Transactional(readOnly = true)
    public ProductDto getProduct(String productId) {
        log.debug("Fetching product: {}", productId);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        
        // Increment view count
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
        
        return productMapper.toDto(product);
    }
    
    /**
     * Update product
     */
    @CacheEvict(value = "products", key = "#productId")
    @Transactional
    public ProductDto updateProduct(String productId, UpdateProductRequest request) {
        log.info("Updating product: {}", productId);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        
        // Update fields if provided
        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getDiscountPrice() != null) product.setDiscountPrice(request.getDiscountPrice());
        if (request.getDiscountPercentage() != null) product.setDiscountPercentage(request.getDiscountPercentage());
        if (request.getStatus() != null) product.setStatus(request.getStatus());
        if (request.getBrand() != null) product.setBrand(request.getBrand());
        if (request.getManufacturer() != null) product.setManufacturer(request.getManufacturer());
        if (request.getImageUrls() != null) product.setImageUrls(request.getImageUrls());
        if (request.getTags() != null) product.setTags(request.getTags());
        if (request.getWeight() != null) product.setWeight(request.getWeight());
        if (request.getLength() != null) product.setLength(request.getLength());
        if (request.getWidth() != null) product.setWidth(request.getWidth());
        if (request.getHeight() != null) product.setHeight(request.getHeight());
        if (request.getIsVegetarian() != null) product.setIsVegetarian(request.getIsVegetarian());
        if (request.getIsVegan() != null) product.setIsVegan(request.getIsVegan());
        if (request.getContainsEgg() != null) product.setContainsEgg(request.getContainsEgg());
        if (request.getContainsDairy() != null) product.setContainsDairy(request.getContainsDairy());
        if (request.getIsFeatured() != null) product.setIsFeatured(request.getIsFeatured());
        if (request.getIsTrending() != null) product.setIsTrending(request.getIsTrending());
        
        // Update category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            product.setCategory(category);
        }
        
        product = productRepository.save(product);
        
        // Publish product updated event
        publishProductEvent(ProductEvent.EventType.PRODUCT_UPDATED, product);
        
        log.info("Product updated successfully: {}", productId);

        return productMapper.toDto(product);
    }

    /**
     * Delete product
     */
    @CacheEvict(value = "products", key = "#productId")
    @Transactional
    public void deleteProduct(String productId) {
        log.info("Deleting product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        productRepository.delete(product);

        // Publish product deleted event
        publishProductEvent(ProductEvent.EventType.PRODUCT_DELETED, product);

        log.info("Product deleted successfully: {}", productId);
    }

    /**
     * Get products by seller
     */
    @Transactional(readOnly = true)
    public Page<ProductDto> getProductsBySeller(String sellerId, Pageable pageable) {
        log.debug("Fetching products for seller: {}", sellerId);

        Page<Product> products = productRepository.findBySellerId(sellerId, pageable);

        return products.map(productMapper::toDto);
    }

    /**
     * Get all published products
     */
    @Transactional(readOnly = true)
    public Page<ProductDto> getPublishedProducts(Pageable pageable) {
        log.debug("Fetching all published products");

        Page<Product> products = productRepository.findByStatus(ProductStatus.PUBLISHED, pageable);

        return products.map(productMapper::toDto);
    }

    /**
     * Search products
     */
    @Transactional(readOnly = true)
    public Page<ProductDto> searchProducts(String query, Pageable pageable) {
        log.debug("Searching products with query: {}", query);

        Page<Product> products = productRepository.searchProducts(query, pageable);

        return products.map(productMapper::toDto);
    }

    /**
     * Filter products
     */
    @Transactional(readOnly = true)
    public Page<ProductDto> filterProducts(
            String categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String productType,
            Pageable pageable
    ) {
        log.debug("Filtering products - category: {}, priceRange: {}-{}, type: {}",
                categoryId, minPrice, maxPrice, productType);

        Page<Product> products = productRepository.findByFilters(
                ProductStatus.PUBLISHED,
                categoryId,
                minPrice,
                maxPrice,
                productType,
                pageable
        );

        return products.map(productMapper::toDto);
    }

    /**
     * Get products by category
     */
    @Transactional(readOnly = true)
    public Page<ProductDto> getProductsByCategory(String categoryId, Pageable pageable) {
        log.debug("Fetching products for category: {}", categoryId);

        Page<Product> products = productRepository.findByCategoryId(categoryId, pageable);

        return products.map(productMapper::toDto);
    }

    /**
     * Get featured products
     */
    @Transactional(readOnly = true)
    public List<ProductDto> getFeaturedProducts() {
        log.debug("Fetching featured products");

        List<Product> products = productRepository.findByIsFeaturedTrue();

        return productMapper.toDtoList(products);
    }

    /**
     * Get trending products
     */
    @Transactional(readOnly = true)
    public List<ProductDto> getTrendingProducts() {
        log.debug("Fetching trending products");

        List<Product> products = productRepository.findByIsTrendingTrue();

        return productMapper.toDtoList(products);
    }

    /**
     * Publish product status change (e.g., DRAFT -> PUBLISHED)
     */
    @CacheEvict(value = "products", key = "#productId")
    @Transactional
    public ProductDto publishProduct(String productId) {
        log.info("Publishing product: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        product.setStatus(ProductStatus.PUBLISHED);
        product = productRepository.save(product);

        // Publish product published event
        publishProductEvent(ProductEvent.EventType.PRODUCT_PUBLISHED, product);

        log.info("Product published successfully: {}", productId);

        return productMapper.toDto(product);
    }

    /**
     * Publish product event to Kafka
     */
    private void publishProductEvent(ProductEvent.EventType eventType, Product product) {
        try {
            ProductEvent event = ProductEvent.builder()
                    .eventType(eventType)
                    .productId(product.getId())
                    .sellerId(product.getSellerId())
                    .name(product.getName())
                    .sku(product.getSku())
                    .price(product.getPrice())
                    .discountPrice(product.getDiscountPrice())
                    .status(product.getStatus())
                    .productType(product.getProductType())
                    .timestamp(Instant.now())
                    .build();

            String topic = switch (eventType) {
                case PRODUCT_CREATED -> PRODUCT_CREATED_TOPIC;
                case PRODUCT_UPDATED, PRODUCT_PUBLISHED -> PRODUCT_UPDATED_TOPIC;
                case PRODUCT_DELETED -> PRODUCT_DELETED_TOPIC;
                default -> PRODUCT_UPDATED_TOPIC;
            };

            kafkaTemplate.send(topic, product.getId(), event);
            log.debug("Published {} event for product: {}", eventType, product.getId());
        } catch (Exception e) {
            log.error("Failed to publish product event: {}", e.getMessage(), e);
        }
    }
}
