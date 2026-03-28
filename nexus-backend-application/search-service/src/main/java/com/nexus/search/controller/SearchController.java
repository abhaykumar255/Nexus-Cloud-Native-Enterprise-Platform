package com.nexus.search.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.search.document.ProductDocument;
import com.nexus.search.document.RestaurantDocument;
import com.nexus.search.document.SellerDocument;
import com.nexus.search.document.TaskDocument;
import com.nexus.search.dto.SearchResponse;
import com.nexus.search.service.ProductSearchService;
import com.nexus.search.service.RestaurantSearchService;
import com.nexus.search.service.SellerSearchService;
import com.nexus.search.service.TaskSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Search REST Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final TaskSearchService taskSearchService;
    private final ProductSearchService productSearchService;
    private final RestaurantSearchService restaurantSearchService;
    private final SellerSearchService sellerSearchService;

    /**
     * Full-text search tasks
     * GET /api/v1/search/tasks?q=keyword&from=0&size=20
     */
    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<SearchResponse<TaskDocument>>> searchTasks(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader("X-User-Id") String userId) {

        log.info("Search tasks request: query={}, from={}, size={}, userId={}", query, from, size, userId);

        SearchResponse<TaskDocument> response = taskSearchService.searchTasks(query, from, size);

        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
    }

    /**
     * Search my tasks
     * GET /api/v1/search/tasks/my?q=keyword&from=0&size=20
     */
    @GetMapping("/tasks/my")
    public ResponseEntity<ApiResponse<SearchResponse<TaskDocument>>> searchMyTasks(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader("X-User-Id") String userId) {

        log.info("Search my tasks request: query={}, userId={}", query, userId);

        SearchResponse<TaskDocument> response = taskSearchService.searchTasksByAssignee(userId, query, from, size);

        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
    }

    /**
     * Autocomplete task titles
     * GET /api/v1/search/autocomplete?prefix=impl
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<ApiResponse<List<String>>> autocomplete(
            @RequestParam("prefix") String prefix,
            @RequestHeader("X-User-Id") String userId) {

        log.info("Autocomplete request: prefix={}", prefix);

        List<String> suggestions = taskSearchService.autocomplete(prefix);

        return ResponseEntity.ok(ApiResponse.success("Autocomplete results", suggestions));
    }

    /**
     * Search products
     * GET /api/v1/search/products?q=keyword&from=0&size=20
     */
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<SearchResponse<ProductDocument>>> searchProducts(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Search products request: query={}, from={}, size={}", query, from, size);

        SearchResponse<ProductDocument> response = productSearchService.searchProducts(query, from, size);

        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
    }

    /**
     * Search products by category
     * GET /api/v1/search/products/category?category=Electronics&q=laptop&minPrice=10000&maxPrice=50000&inStock=true
     */
    @GetMapping("/products/category")
    public ResponseEntity<ApiResponse<SearchResponse<ProductDocument>>> searchProductsByCategory(
            @RequestParam(required = false) String category,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Search products by category: category={}, query={}", category, query);

        SearchResponse<ProductDocument> response = productSearchService.searchProductsByCategory(
                category, query, minPrice, maxPrice, inStock, from, size);

        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
    }

    /**
     * Search restaurants
     * GET /api/v1/search/restaurants?q=keyword&from=0&size=20
     */
    @GetMapping("/restaurants")
    public ResponseEntity<ApiResponse<SearchResponse<RestaurantDocument>>> searchRestaurants(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Search restaurants request: query={}, from={}, size={}", query, from, size);

        SearchResponse<RestaurantDocument> response = restaurantSearchService.searchRestaurants(query, from, size);

        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
    }

    /**
     * Search restaurants by cuisine
     * GET /api/v1/search/restaurants/cuisine?cuisine=INDIAN&q=biryani&isOpen=true
     */
    @GetMapping("/restaurants/cuisine")
    public ResponseEntity<ApiResponse<SearchResponse<RestaurantDocument>>> searchRestaurantsByCuisine(
            @RequestParam(required = false) String cuisine,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(required = false) Boolean isOpen,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Search restaurants by cuisine: cuisine={}, query={}", cuisine, query);

        SearchResponse<RestaurantDocument> response = restaurantSearchService.searchRestaurantsByCuisine(
                cuisine, query, isOpen, from, size);

        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
    }

    /**
     * Search sellers
     * GET /api/v1/search/sellers?q=keyword&from=0&size=20
     */
    @GetMapping("/sellers")
    public ResponseEntity<ApiResponse<SearchResponse<SellerDocument>>> searchSellers(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Search sellers request: query={}, from={}, size={}", query, from, size);

        SearchResponse<SellerDocument> response = sellerSearchService.searchSellers(query, from, size);

        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
    }

    /**
     * Search verified sellers
     * GET /api/v1/search/sellers/verified?q=keyword&kycVerified=true
     */
    @GetMapping("/sellers/verified")
    public ResponseEntity<ApiResponse<SearchResponse<SellerDocument>>> searchVerifiedSellers(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(required = false) Boolean kycVerified,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Search verified sellers: query={}, kycVerified={}", query, kycVerified);

        SearchResponse<SellerDocument> response = sellerSearchService.searchVerifiedSellers(
                query, kycVerified, from, size);

        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Search Service is running");
    }
}

