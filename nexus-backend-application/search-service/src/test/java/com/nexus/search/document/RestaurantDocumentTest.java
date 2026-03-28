package com.nexus.search.document;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantDocumentTest {

    @Test
    void testRestaurantDocumentBuilder() {
        List<String> cuisines = Arrays.asList("Italian", "Mexican");
        List<String> dishes = Arrays.asList("Pizza", "Pasta");
        Instant now = Instant.now();

        RestaurantDocument restaurant = RestaurantDocument.builder()
                .id("rest-1")
                .name("Bella Italia")
                .description("Authentic Italian restaurant")
                .cuisineType("Italian")
                .cuisineTypes(cuisines)
                .sellerId("seller-1")
                .address("123 Main St")
                .latitude(40.7128)
                .longitude(-74.0060)
                .phoneNumber("+1234567890")
                .email("info@bellaitalia.com")
                .isOpen(true)
                .openingTime("10:00")
                .closingTime("22:00")
                .rating(new BigDecimal("4.7"))
                .totalReviews(500L)
                .minOrderAmount(new BigDecimal("15.00"))
                .deliveryFee(new BigDecimal("5.00"))
                .averageDeliveryTimeMinutes(30)
                .status("ACTIVE")
                .popularDishes(dishes)
                .createdAt(now)
                .build();

        assertEquals("rest-1", restaurant.getId());
        assertEquals("Bella Italia", restaurant.getName());
        assertEquals("Authentic Italian restaurant", restaurant.getDescription());
        assertEquals("Italian", restaurant.getCuisineType());
        assertEquals(cuisines, restaurant.getCuisineTypes());
        assertEquals("seller-1", restaurant.getSellerId());
        assertEquals("123 Main St", restaurant.getAddress());
        assertEquals(40.7128, restaurant.getLatitude());
        assertEquals(-74.0060, restaurant.getLongitude());
        assertEquals("+1234567890", restaurant.getPhoneNumber());
        assertEquals("info@bellaitalia.com", restaurant.getEmail());
        assertTrue(restaurant.getIsOpen());
        assertEquals("10:00", restaurant.getOpeningTime());
        assertEquals("22:00", restaurant.getClosingTime());
        assertEquals(new BigDecimal("4.7"), restaurant.getRating());
        assertEquals(500L, restaurant.getTotalReviews());
        assertEquals(new BigDecimal("15.00"), restaurant.getMinOrderAmount());
        assertEquals(new BigDecimal("5.00"), restaurant.getDeliveryFee());
        assertEquals(30, restaurant.getAverageDeliveryTimeMinutes());
        assertEquals("ACTIVE", restaurant.getStatus());
        assertEquals(dishes, restaurant.getPopularDishes());
        assertEquals(now, restaurant.getCreatedAt());
    }

    @Test
    void testRestaurantDocumentNoArgsConstructor() {
        RestaurantDocument restaurant = new RestaurantDocument();
        assertNotNull(restaurant);
    }

    @Test
    void testRestaurantDocumentSettersAndGetters() {
        RestaurantDocument restaurant = new RestaurantDocument();
        restaurant.setId("rest-2");
        restaurant.setName("Taco Bell");
        restaurant.setIsOpen(false);
        restaurant.setRating(new BigDecimal("4.2"));

        assertEquals("rest-2", restaurant.getId());
        assertEquals("Taco Bell", restaurant.getName());
        assertFalse(restaurant.getIsOpen());
        assertEquals(new BigDecimal("4.2"), restaurant.getRating());
    }

    @Test
    void testRestaurantDocumentEqualsAndHashCode() {
        RestaurantDocument restaurant1 = RestaurantDocument.builder()
                .id("rest-1")
                .name("Test Restaurant")
                .build();

        RestaurantDocument restaurant2 = RestaurantDocument.builder()
                .id("rest-1")
                .name("Test Restaurant")
                .build();

        assertEquals(restaurant1, restaurant2);
        assertEquals(restaurant1.hashCode(), restaurant2.hashCode());
    }

    @Test
    void testRestaurantDocumentToString() {
        RestaurantDocument restaurant = RestaurantDocument.builder()
                .id("rest-1")
                .name("Test")
                .build();

        String toString = restaurant.toString();
        assertTrue(toString.contains("rest-1"));
        assertTrue(toString.contains("Test"));
    }

    @Test
    void testRestaurantDocumentAllFields() {
        RestaurantDocument restaurant = RestaurantDocument.builder()
                .latitude(40.7589)
                .longitude(-73.9851)
                .averageDeliveryTimeMinutes(45)
                .build();

        assertEquals(40.7589, restaurant.getLatitude());
        assertEquals(-73.9851, restaurant.getLongitude());
        assertEquals(45, restaurant.getAverageDeliveryTimeMinutes());
    }
}

