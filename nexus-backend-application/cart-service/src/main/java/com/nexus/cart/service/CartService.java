package com.nexus.cart.service;

import com.nexus.cart.domain.model.Cart;
import com.nexus.cart.domain.model.CartItem;
import com.nexus.cart.domain.repository.CartRepository;
import com.nexus.cart.dto.AddToCartRequest;
import com.nexus.cart.dto.CartDto;
import com.nexus.cart.dto.UpdateCartItemRequest;
import com.nexus.cart.mapper.CartMapper;
import com.nexus.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final WebClient.Builder webClientBuilder;
    
    private static final long CART_TTL = 604800L; // 7 days in seconds
    
    /**
     * Get or create cart for user/guest
     */
    public CartDto getCart(String userId, String guestId) {
        Cart cart;
        
        if (userId != null) {
            cart = cartRepository.findByUserId(userId)
                    .orElseGet(() -> createNewCart(userId, null));
        } else if (guestId != null) {
            cart = cartRepository.findByGuestId(guestId)
                    .orElseGet(() -> createNewCart(null, guestId));
        } else {
            throw new IllegalArgumentException("Either userId or guestId must be provided");
        }
        
        return cartMapper.toDto(cart);
    }
    
    /**
     * Add item to cart
     */
    public CartDto addToCart(String userId, String guestId, AddToCartRequest request) {
        Cart cart = getOrCreateCart(userId, guestId);
        
        // Fetch product details from Product Service
        Map<String, Object> productDetails = fetchProductDetails(request.getProductId());
        
        // Check if item already exists in cart
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()) 
                        && (request.getVariantId() == null || request.getVariantId().equals(item.getVariantId())))
                .findFirst()
                .orElse(null);
        
        if (existingItem != null) {
            // Update quantity
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            existingItem.calculateTotalPrice();
        } else {
            // Add new item
            CartItem newItem = CartItem.builder()
                    .productId(request.getProductId())
                    .productName((String) productDetails.get("name"))
                    .productImage((String) productDetails.get("primaryImage"))
                    .sellerId((String) productDetails.get("sellerId"))
                    .sellerName((String) productDetails.get("sellerName"))
                    .quantity(request.getQuantity())
                    .unitPrice(new BigDecimal(productDetails.get("price").toString()))
                    .variantId(request.getVariantId())
                    .inStock(true)
                    .availableStock((Integer) productDetails.getOrDefault("stockQuantity", 0))
                    .addedAt(Instant.now())
                    .build();
            
            newItem.calculateTotalPrice();
            cart.getItems().add(newItem);
        }
        
        cart.calculateTotals();
        cart = cartRepository.save(cart);
        
        log.info("Added product {} to cart {}", request.getProductId(), cart.getId());
        return cartMapper.toDto(cart);
    }
    
    /**
     * Update cart item quantity
     */
    public CartDto updateCartItem(String userId, String guestId, String productId, UpdateCartItemRequest request) {
        Cart cart = getOrCreateCart(userId, guestId);
        
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart: " + productId));
        
        item.setQuantity(request.getQuantity());
        item.calculateTotalPrice();
        
        cart.calculateTotals();
        cart = cartRepository.save(cart);
        
        log.info("Updated product {} quantity to {} in cart {}", productId, request.getQuantity(), cart.getId());
        return cartMapper.toDto(cart);
    }
    
    /**
     * Remove item from cart
     */
    public CartDto removeFromCart(String userId, String guestId, String productId) {
        Cart cart = getOrCreateCart(userId, guestId);
        
        boolean removed = cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        
        if (!removed) {
            throw new ResourceNotFoundException("Product not found in cart: " + productId);
        }
        
        cart.calculateTotals();
        cart = cartRepository.save(cart);
        
        log.info("Removed product {} from cart {}", productId, cart.getId());
        return cartMapper.toDto(cart);
    }
    
    /**
     * Clear cart
     */
    public void clearCart(String userId, String guestId) {
        Cart cart = getOrCreateCart(userId, guestId);
        cart.getItems().clear();
        cart.calculateTotals();
        cartRepository.save(cart);
        log.info("Cleared cart {}", cart.getId());
    }
    
    /**
     * Merge guest cart with user cart on login
     */
    public CartDto mergeCart(String userId, String guestId) {
        Cart userCart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId, null));
        
        Cart guestCart = cartRepository.findByGuestId(guestId).orElse(null);
        
        if (guestCart != null && !guestCart.isEmpty()) {
            // Merge items
            for (CartItem guestItem : guestCart.getItems()) {
                CartItem existingItem = userCart.getItems().stream()
                        .filter(item -> item.getProductId().equals(guestItem.getProductId()))
                        .findFirst()
                        .orElse(null);

                if (existingItem != null) {
                    existingItem.setQuantity(existingItem.getQuantity() + guestItem.getQuantity());
                    existingItem.calculateTotalPrice();
                } else {
                    userCart.getItems().add(guestItem);
                }
            }

            userCart.calculateTotals();
            cartRepository.save(userCart);

            // Delete guest cart
            cartRepository.delete(guestCart);
            log.info("Merged guest cart {} with user cart {}", guestId, userId);
        }

        return cartMapper.toDto(userCart);
    }

    /**
     * Apply coupon to cart
     */
    public CartDto applyCoupon(String userId, String guestId, String couponCode) {
        Cart cart = getOrCreateCart(userId, guestId);

        // TODO: Call Coupon Service to validate and get discount
        // For now, mock a 10% discount
        BigDecimal discount = cart.getSubtotal().multiply(new BigDecimal("0.10"));

        cart.setCouponCode(couponCode);
        cart.setCouponDiscount(discount);
        cart.calculateTotals();

        cart = cartRepository.save(cart);
        log.info("Applied coupon {} to cart {}", couponCode, cart.getId());

        return cartMapper.toDto(cart);
    }

    /**
     * Validate cart against inventory
     */
    public boolean validateCart(String userId, String guestId) {
        Cart cart = getOrCreateCart(userId, guestId);

        for (CartItem item : cart.getItems()) {
            // TODO: Call Inventory Service to check stock
            // For now, assume all items are in stock
            item.setInStock(true);
        }

        return cart.getItems().stream().allMatch(CartItem::isAvailable);
    }

    // Helper methods

    private Cart getOrCreateCart(String userId, String guestId) {
        if (userId != null) {
            return cartRepository.findByUserId(userId)
                    .orElseGet(() -> createNewCart(userId, null));
        } else if (guestId != null) {
            return cartRepository.findByGuestId(guestId)
                    .orElseGet(() -> createNewCart(null, guestId));
        } else {
            throw new IllegalArgumentException("Either userId or guestId must be provided");
        }
    }

    private Cart createNewCart(String userId, String guestId) {
        String cartId = userId != null ? userId : guestId;

        Cart cart = Cart.builder()
                .id(cartId)
                .userId(userId)
                .guestId(guestId)
                .items(new ArrayList<>())
                .subtotal(BigDecimal.ZERO)
                .discount(BigDecimal.ZERO)
                .tax(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .ttl(CART_TTL)
                .build();

        return cartRepository.save(cart);
    }

    private Map<String, Object> fetchProductDetails(String productId) {
        // TODO: Call Product Service via WebClient
        // For now, return mock data
        return Map.of(
                "name", "Sample Product",
                "primaryImage", "https://example.com/image.jpg",
                "sellerId", "seller123",
                "sellerName", "Sample Seller",
                "price", 99.99,
                "stockQuantity", 100
        );
    }
}

