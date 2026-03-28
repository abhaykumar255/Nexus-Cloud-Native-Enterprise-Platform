package com.nexus.search.consumer;

import com.nexus.search.document.ProductDocument;
import com.nexus.search.document.RestaurantDocument;
import com.nexus.search.document.SellerDocument;
import com.nexus.search.document.TaskDocument;
import com.nexus.search.service.ProductSearchService;
import com.nexus.search.service.RestaurantSearchService;
import com.nexus.search.service.SellerSearchService;
import com.nexus.search.service.TaskSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Kafka Event Consumer for indexing
 * Listens to events from other services and updates Elasticsearch
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final TaskSearchService taskSearchService;
    private final ProductSearchService productSearchService;
    private final RestaurantSearchService restaurantSearchService;
    private final SellerSearchService sellerSearchService;

    /**
     * Index tasks when created or updated
     */
    @KafkaListener(topics = {"task.created", "task.updated"}, groupId = "search-service-group")
    public void consumeTaskEvent(Map<String, Object> event) {
        try {
            log.info("Received task event: {}", event);

            TaskDocument taskDocument = TaskDocument.builder()
                    .id(String.valueOf(event.get("taskId")))
                    .title((String) event.get("title"))
                    .description((String) event.get("description"))
                    .status((String) event.get("status"))
                    .priority((String) event.get("priority"))
                    .assigneeId((String) event.get("assigneeId"))
                    .creatorId((String) event.get("creatorId"))
                    .projectId((String) event.get("projectId"))
                    .category((String) event.get("category"))
                    .build();

            taskSearchService.indexTask(taskDocument);

        } catch (Exception e) {
            log.error("Failed to process task event", e);
        }
    }

    /**
     * Index products when created or updated
     */
    @KafkaListener(topics = {"product.created", "product.updated"}, groupId = "search-service-group")
    public void consumeProductEvent(Map<String, Object> event) {
        try {
            log.info("Received product event: {}", event);

            ProductDocument productDocument = ProductDocument.builder()
                    .id(String.valueOf(event.get("productId")))
                    .name((String) event.get("name"))
                    .description((String) event.get("description"))
                    .shortDescription((String) event.get("shortDescription"))
                    .brand((String) event.get("brand"))
                    .category((String) event.get("category"))
                    .subCategory((String) event.get("subCategory"))
                    .sellerId((String) event.get("sellerId"))
                    .sellerName((String) event.get("sellerName"))
                    .price(event.get("price") != null ? new BigDecimal(event.get("price").toString()) : null)
                    .discountedPrice(event.get("discountedPrice") != null ? new BigDecimal(event.get("discountedPrice").toString()) : null)
                    .discountPercentage((Integer) event.get("discountPercentage"))
                    .stockQuantity((Integer) event.get("stockQuantity"))
                    .inStock((Boolean) event.get("inStock"))
                    .sku((String) event.get("sku"))
                    .tags((List<String>) event.get("tags"))
                    .images((List<String>) event.get("images"))
                    .rating(event.get("rating") != null ? new BigDecimal(event.get("rating").toString()) : null)
                    .reviewCount((Integer) event.get("reviewCount"))
                    .status((String) event.get("status"))
                    .createdAt(event.get("createdAt") != null ? Instant.parse(event.get("createdAt").toString()) : Instant.now())
                    .build();

            productSearchService.indexProduct(productDocument);

        } catch (Exception e) {
            log.error("Failed to process product event", e);
        }
    }

    /**
     * Index restaurants when created or updated
     */
    @KafkaListener(topics = {"restaurant.created", "restaurant.updated"}, groupId = "search-service-group")
    public void consumeRestaurantEvent(Map<String, Object> event) {
        try {
            log.info("Received restaurant event: {}", event);

            RestaurantDocument restaurantDocument = RestaurantDocument.builder()
                    .id(String.valueOf(event.get("restaurantId")))
                    .name((String) event.get("name"))
                    .description((String) event.get("description"))
                    .cuisineType((String) event.get("cuisineType"))
                    .cuisineTypes((List<String>) event.get("cuisineTypes"))
                    .sellerId((String) event.get("sellerId"))
                    .address((String) event.get("address"))
                    .latitude(event.get("latitude") != null ? ((Number) event.get("latitude")).doubleValue() : null)
                    .longitude(event.get("longitude") != null ? ((Number) event.get("longitude")).doubleValue() : null)
                    .phoneNumber((String) event.get("phoneNumber"))
                    .email((String) event.get("email"))
                    .isOpen((Boolean) event.get("isOpen"))
                    .openingTime((String) event.get("openingTime"))
                    .closingTime((String) event.get("closingTime"))
                    .rating(event.get("rating") != null ? new BigDecimal(event.get("rating").toString()) : null)
                    .totalReviews(event.get("totalReviews") != null ? ((Number) event.get("totalReviews")).longValue() : 0L)
                    .minOrderAmount(event.get("minOrderAmount") != null ? new BigDecimal(event.get("minOrderAmount").toString()) : null)
                    .deliveryFee(event.get("deliveryFee") != null ? new BigDecimal(event.get("deliveryFee").toString()) : null)
                    .averageDeliveryTimeMinutes((Integer) event.get("averageDeliveryTimeMinutes"))
                    .status((String) event.get("status"))
                    .popularDishes((List<String>) event.get("popularDishes"))
                    .createdAt(event.get("createdAt") != null ? Instant.parse(event.get("createdAt").toString()) : Instant.now())
                    .build();

            restaurantSearchService.indexRestaurant(restaurantDocument);

        } catch (Exception e) {
            log.error("Failed to process restaurant event", e);
        }
    }

    /**
     * Index sellers when created or updated
     */
    @KafkaListener(topics = {"seller.created", "seller.updated"}, groupId = "search-service-group")
    public void consumeSellerEvent(Map<String, Object> event) {
        try {
            log.info("Received seller event: {}", event);

            SellerDocument sellerDocument = SellerDocument.builder()
                    .id(String.valueOf(event.get("sellerId")))
                    .businessName((String) event.get("businessName"))
                    .businessType((String) event.get("businessType"))
                    .businessEmail((String) event.get("businessEmail"))
                    .businessPhone((String) event.get("businessPhone"))
                    .businessAddress((String) event.get("businessAddress"))
                    .status((String) event.get("status"))
                    .verificationStatus((String) event.get("verificationStatus"))
                    .kycVerified((Boolean) event.get("kycVerified"))
                    .rating(event.get("rating") != null ? new BigDecimal(event.get("rating").toString()) : null)
                    .totalReviews((Integer) event.get("totalReviews"))
                    .totalProducts((Integer) event.get("totalProducts"))
                    .totalOrders((Integer) event.get("totalOrders"))
                    .totalRevenue(event.get("totalRevenue") != null ? new BigDecimal(event.get("totalRevenue").toString()) : null)
                    .description((String) event.get("description"))
                    .createdAt(event.get("createdAt") != null ? Instant.parse(event.get("createdAt").toString()) : Instant.now())
                    .build();

            sellerSearchService.indexSeller(sellerDocument);

        } catch (Exception e) {
            log.error("Failed to process seller event", e);
        }
    }
}

