package com.nexus.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.*;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * End-to-end integration test for complete order placement flow:
 * 1. User authentication
 * 2. Browse products
 * 3. Add items to cart
 * 4. Apply coupon
 * 5. Place order
 * 6. Process payment
 * 7. Assign delivery
 * 8. Track order
 * 9. Submit review
 */
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("E2E Order Placement Flow Integration Tests")
public class OrderPlacementFlowIT {

    private static final String API_GATEWAY_URL = System.getProperty("api.gateway.url", "http://localhost:8080");
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    private static String authToken;
    private static String userId;
    private static String productId;
    private static String cartId;
    private static String orderId;
    private static String paymentId;
    private static String deliveryId;
    private static String couponCode = "FIRST10";

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = API_GATEWAY_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Order(1)
    @DisplayName("1. User Registration and Authentication")
    void testUserRegistrationAndLogin() {
        // Register new user
        Map<String, Object> registerRequest = new HashMap<>();
        registerRequest.put("email", "testuser@example.com");
        registerRequest.put("password", "Test@123");
        registerRequest.put("firstName", "Test");
        registerRequest.put("lastName", "User");

        userId = given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
            .when()
                .post("/api/v1/auth/register")
            .then()
                .statusCode(201)
                .body("email", equalTo("testuser@example.com"))
                .extract()
                .path("id");

        // Login
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "testuser@example.com");
        loginRequest.put("password", "Test@123");

        authToken = given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
            .when()
                .post("/api/v1/auth/login")
            .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .path("token");

        Assertions.assertNotNull(authToken, "Auth token should not be null");
    }

    @Test
    @Order(2)
    @DisplayName("2. Browse and Search Products")
    void testBrowseProducts() {
        productId = given()
                .header("Authorization", "Bearer " + authToken)
                .queryParam("category", "ELECTRONICS")
                .queryParam("page", 0)
                .queryParam("size", 10)
            .when()
                .get("/api/v1/products")
            .then()
                .statusCode(200)
                .body("content", not(empty()))
                .body("content[0].id", notNullValue())
                .body("content[0].name", notNullValue())
                .body("content[0].price", greaterThan(0f))
                .extract()
                .path("content[0].id");

        Assertions.assertNotNull(productId, "Product ID should not be null");
    }

    @Test
    @Order(3)
    @DisplayName("3. Add Products to Cart")
    void testAddToCart() {
        Map<String, Object> addToCartRequest = new HashMap<>();
        addToCartRequest.put("productId", productId);
        addToCartRequest.put("quantity", 2);

        cartId = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(addToCartRequest)
            .when()
                .post("/api/v1/cart/items")
            .then()
                .statusCode(200)
                .body("items", hasSize(1))
                .body("items[0].productId", equalTo(productId))
                .body("items[0].quantity", equalTo(2))
                .body("totalAmount", greaterThan(0f))
                .extract()
                .path("id");

        Assertions.assertNotNull(cartId, "Cart ID should not be null");
    }

    @Test
    @Order(4)
    @DisplayName("4. Apply Coupon to Cart")
    void testApplyCoupon() {
        Map<String, Object> applyCouponRequest = new HashMap<>();
        applyCouponRequest.put("couponCode", couponCode);
        applyCouponRequest.put("orderAmount", 1000.00);

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(applyCouponRequest)
            .when()
                .post("/api/v1/coupons/validate")
            .then()
                .statusCode(200)
                .body("valid", equalTo(true))
                .body("discountAmount", greaterThan(0f))
                .body("finalAmount", lessThan(1000f));
    }

    @Test
    @Order(5)
    @DisplayName("5. Place Order with Coupon")
    void testPlaceOrder() {
        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("userId", userId);
        orderRequest.put("couponCode", couponCode);
        orderRequest.put("deliveryAddress", Map.of(
                "line1", "123 Test Street",
                "city", "Test City",
                "state", "Test State",
                "zipCode", "12345",
                "country", "Test Country"
        ));

        orderId = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(orderRequest)
            .when()
                .post("/api/v1/orders")
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("status", equalTo("CREATED"))
                .body("totalAmount", greaterThan(0f))
                .extract()
                .path("id");

        Assertions.assertNotNull(orderId, "Order ID should not be null");
    }

    @Test
    @Order(6)
    @DisplayName("6. Process Payment")
    void testProcessPayment() {
        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("orderId", orderId);
        paymentRequest.put("paymentMethod", "UPI");
        paymentRequest.put("upiId", "test@upi");

        paymentId = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(paymentRequest)
            .when()
                .post("/api/v1/payments")
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("orderId", equalTo(orderId))
                .body("status", in("PENDING", "PROCESSING", "COMPLETED"))
                .extract()
                .path("id");

        Assertions.assertNotNull(paymentId, "Payment ID should not be null");

        // Wait for payment to complete
        await()
                .atMost(30, SECONDS)
                .pollInterval(2, SECONDS)
                .until(() -> {
                    String status = given()
                            .header("Authorization", "Bearer " + authToken)
                        .when()
                            .get("/api/v1/payments/" + paymentId)
                        .then()
                            .statusCode(200)
                            .extract()
                            .path("status");
                    return "COMPLETED".equals(status);
                });
    }

    @Test
    @Order(7)
    @DisplayName("7. Assign Delivery")
    void testAssignDelivery() {
        // Wait for delivery to be assigned automatically
        await()
                .atMost(30, SECONDS)
                .pollInterval(2, SECONDS)
                .until(() -> {
                    try {
                        deliveryId = given()
                                .header("Authorization", "Bearer " + authToken)
                                .queryParam("orderId", orderId)
                            .when()
                                .get("/api/v1/deliveries")
                            .then()
                                .statusCode(200)
                                .body("id", notNullValue())
                                .body("orderId", equalTo(orderId))
                                .extract()
                                .path("id");
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                });

        Assertions.assertNotNull(deliveryId, "Delivery ID should not be null");
    }

    @Test
    @Order(8)
    @DisplayName("8. Track Order in Real-time")
    void testTrackOrder() {
        given()
                .header("Authorization", "Bearer " + authToken)
            .when()
                .get("/api/v1/tracking/order/" + orderId)
            .then()
                .statusCode(200)
                .body("orderId", equalTo(orderId))
                .body("status", notNullValue())
                .body("estimatedDeliveryTime", notNullValue());
    }

    @Test
    @Order(9)
    @DisplayName("9. Submit Product Review")
    void testSubmitReview() {
        Map<String, Object> reviewRequest = new HashMap<>();
        reviewRequest.put("targetType", "PRODUCT");
        reviewRequest.put("targetId", productId);
        reviewRequest.put("userId", userId);
        reviewRequest.put("rating", 5);
        reviewRequest.put("title", "Excellent Product!");
        reviewRequest.put("comment", "This product exceeded my expectations. Highly recommended!");
        reviewRequest.put("verifiedPurchase", true);

        String reviewId = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(reviewRequest)
            .when()
                .post("/api/v1/reviews")
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("rating", equalTo(5))
                .body("status", equalTo("PENDING"))
                .extract()
                .path("id");

        Assertions.assertNotNull(reviewId, "Review ID should not be null");
    }

    @Test
    @Order(10)
    @DisplayName("10. Verify Order History")
    void testGetOrderHistory() {
        given()
                .header("Authorization", "Bearer " + authToken)
            .when()
                .get("/api/v1/orders/user/" + userId)
            .then()
                .statusCode(200)
                .body("content", hasSize(greaterThanOrEqualTo(1)))
                .body("content[0].id", equalTo(orderId))
                .body("content[0].status", in("DELIVERED", "IN_TRANSIT", "CONFIRMED"));
    }
}

