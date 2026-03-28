package com.nexus.cart.controller;

import com.nexus.cart.dto.AddToCartRequest;
import com.nexus.cart.dto.ApplyCouponRequest;
import com.nexus.cart.dto.CartDto;
import com.nexus.cart.dto.UpdateCartItemRequest;
import com.nexus.cart.service.CartService;
import com.nexus.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Cart Controller
 * Manages shopping cart operations
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    
    /**
     * Get cart for user or guest
     */
    @GetMapping
    public ResponseEntity<ApiResponse<CartDto>> getCart(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Guest-Id", required = false) String guestId) {
        
        log.info("Getting cart for userId={}, guestId={}", userId, guestId);
        CartDto cart = cartService.getCart(userId, guestId);

        return ResponseEntity.ok(ApiResponse.success("Cart retrieved successfully", cart));
    }
    
    /**
     * Add item to cart
     */
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartDto>> addToCart(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Guest-Id", required = false) String guestId,
            @Valid @RequestBody AddToCartRequest request) {
        
        log.info("Adding product {} to cart for userId={}, guestId={}",
                request.getProductId(), userId, guestId);

        CartDto cart = cartService.addToCart(userId, guestId, request);

        return ResponseEntity.ok(ApiResponse.success("Item added to cart", cart));
    }

    /**
     * Update cart item quantity
     */
    @PutMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> updateCartItem(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Guest-Id", required = false) String guestId,
            @PathVariable String productId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        log.info("Updating product {} quantity to {} in cart for userId={}, guestId={}",
                productId, request.getQuantity(), userId, guestId);

        CartDto cart = cartService.updateCartItem(userId, guestId, productId, request);

        return ResponseEntity.ok(ApiResponse.success("Cart item updated", cart));
    }

    /**
     * Remove item from cart
     */
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> removeFromCart(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Guest-Id", required = false) String guestId,
            @PathVariable String productId) {

        log.info("Removing product {} from cart for userId={}, guestId={}",
                productId, userId, guestId);

        CartDto cart = cartService.removeFromCart(userId, guestId, productId);

        return ResponseEntity.ok(ApiResponse.success("Item removed from cart", cart));
    }

    /**
     * Clear cart
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> clearCart(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Guest-Id", required = false) String guestId) {

        log.info("Clearing cart for userId={}, guestId={}", userId, guestId);
        cartService.clearCart(userId, guestId);

        return ResponseEntity.ok(ApiResponse.success("Cart cleared", "Cart has been cleared successfully"));
    }

    /**
     * Merge guest cart with user cart on login
     */
    @PostMapping("/merge")
    public ResponseEntity<ApiResponse<CartDto>> mergeCart(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Guest-Id") String guestId) {

        log.info("Merging guest cart {} with user cart {}", guestId, userId);
        CartDto cart = cartService.mergeCart(userId, guestId);

        return ResponseEntity.ok(ApiResponse.success("Carts merged successfully", cart));
    }

    /**
     * Apply coupon to cart
     */
    @PostMapping("/coupon")
    public ResponseEntity<ApiResponse<CartDto>> applyCoupon(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Guest-Id", required = false) String guestId,
            @Valid @RequestBody ApplyCouponRequest request) {

        log.info("Applying coupon {} to cart for userId={}, guestId={}",
                request.getCouponCode(), userId, guestId);

        CartDto cart = cartService.applyCoupon(userId, guestId, request.getCouponCode());

        return ResponseEntity.ok(ApiResponse.success("Coupon applied successfully", cart));
    }

    /**
     * Validate cart
     */
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateCart(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Guest-Id", required = false) String guestId) {

        log.info("Validating cart for userId={}, guestId={}", userId, guestId);
        boolean isValid = cartService.validateCart(userId, guestId);

        String message = isValid ? "Cart is valid" : "Cart has invalid items";
        return ResponseEntity.ok(ApiResponse.success(message, isValid));
    }
}

