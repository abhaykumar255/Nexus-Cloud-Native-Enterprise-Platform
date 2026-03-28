package com.nexus.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for Coupon and Review workflows
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Coupon and Review Workflow Integration Tests")
public class CouponAndReviewWorkflowIT {

    private static final String API_GATEWAY_URL = System.getProperty("api.gateway.url", "http://localhost:8080");
    
    private static String authToken;
    private static String adminToken;
    private static String userId;
    private static String couponId;
    private static String reviewId;
    private static String productId = "test-product-123";

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = API_GATEWAY_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Order(1)
    @DisplayName("1. Admin creates coupon")
    void testCreateCoupon() {
        // Login as admin
        Map<String, String> adminLogin = new HashMap<>();
        adminLogin.put("email", "admin@example.com");
        adminLogin.put("password", "Admin@123");

        adminToken = given()
                .contentType(ContentType.JSON)
                .body(adminLogin)
            .when()
                .post("/api/v1/auth/login")
            .then()
                .statusCode(200)
                .extract()
                .path("token");

        // Create coupon
        Map<String, Object> couponRequest = new HashMap<>();
        couponRequest.put("code", "WELCOME20");
        couponRequest.put("description", "20% off for new users");
        couponRequest.put("couponType", "FIRST_ORDER");
        couponRequest.put("discountType", "PERCENTAGE");
        couponRequest.put("discountValue", 20.0);
        couponRequest.put("maxDiscountAmount", 200.0);
        couponRequest.put("minOrderAmount", 500.0);
        couponRequest.put("usageLimit", 1000);
        couponRequest.put("usageLimitPerUser", 1);
        couponRequest.put("startDate", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        couponRequest.put("expiryDate", LocalDateTime.now().plusDays(30).format(DateTimeFormatter.ISO_DATE_TIME));
        couponRequest.put("status", "ACTIVE");

        couponId = given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(couponRequest)
            .when()
                .post("/api/v1/coupons")
            .then()
                .statusCode(201)
                .body("code", equalTo("WELCOME20"))
                .body("discountType", equalTo("PERCENTAGE"))
                .body("discountValue", equalTo(20.0f))
                .extract()
                .path("id");

        Assertions.assertNotNull(couponId, "Coupon ID should not be null");
    }

    @Test
    @Order(2)
    @DisplayName("2. User validates coupon")
    void testValidateCoupon() {
        Map<String, Object> validationRequest = new HashMap<>();
        validationRequest.put("couponCode", "WELCOME20");
        validationRequest.put("userId", userId);
        validationRequest.put("orderAmount", 1000.0);
        validationRequest.put("isFirstOrder", true);

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(validationRequest)
            .when()
                .post("/api/v1/coupons/validate")
            .then()
                .statusCode(200)
                .body("valid", equalTo(true))
                .body("discountAmount", equalTo(200.0f))
                .body("finalAmount", equalTo(800.0f))
                .body("message", containsString("valid"));
    }

    @Test
    @Order(3)
    @DisplayName("3. User submits product review")
    void testSubmitProductReview() {
        Map<String, Object> reviewRequest = new HashMap<>();
        reviewRequest.put("targetType", "PRODUCT");
        reviewRequest.put("targetId", productId);
        reviewRequest.put("userId", userId);
        reviewRequest.put("rating", 4);
        reviewRequest.put("title", "Good product with minor issues");
        reviewRequest.put("comment", "The product quality is good but delivery was delayed");
        reviewRequest.put("verifiedPurchase", true);

        reviewId = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(reviewRequest)
            .when()
                .post("/api/v1/reviews")
            .then()
                .statusCode(201)
                .body("rating", equalTo(4))
                .body("status", equalTo("PENDING"))
                .body("verifiedPurchase", equalTo(true))
                .extract()
                .path("id");

        Assertions.assertNotNull(reviewId, "Review ID should not be null");
    }

    @Test
    @Order(4)
    @DisplayName("4. Admin approves review")
    void testApproveReview() {
        given()
                .header("Authorization", "Bearer " + adminToken)
            .when()
                .put("/api/v1/reviews/" + reviewId + "/approve")
            .then()
                .statusCode(200)
                .body("status", equalTo("APPROVED"));
    }

    @Test
    @Order(5)
    @DisplayName("5. Get product reviews")
    void testGetProductReviews() {
        given()
                .header("Authorization", "Bearer " + authToken)
            .when()
                .get("/api/v1/reviews/PRODUCT/" + productId)
            .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(1)))
                .body("[0].rating", notNullValue())
                .body("[0].status", equalTo("APPROVED"));
    }
}

