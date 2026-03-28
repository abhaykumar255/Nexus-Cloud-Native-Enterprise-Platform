package com.nexus.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for JwtService
 */
@DisplayName("JwtService Tests")
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Set test properties using reflection
        ReflectionTestUtils.setField(jwtService, "secret", 
            "test-secret-key-that-is-at-least-32-characters-long-for-hmac-sha256");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3600000L); // 1 hour
        ReflectionTestUtils.setField(jwtService, "refreshExpirationMs", 604800000L); // 7 days
    }

    @Test
    @DisplayName("Should generate access token with user details")
    void generateAccessToken_validInput_success() {
        // Arrange
        String userId = "user-123";
        String email = "test@example.com";
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");

        // Act
        String token = jwtService.generateAccessToken(userId, email, roles);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT has 3 parts
    }

    @Test
    @DisplayName("Should generate refresh token")
    void generateRefreshToken_validUserId_success() {
        // Arrange
        String userId = "user-123";

        // Act
        String token = jwtService.generateRefreshToken(userId);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("Should validate valid token")
    void validateToken_validToken_returnsTrue() {
        // Arrange
        String userId = "user-123";
        String email = "test@example.com";
        String token = jwtService.generateAccessToken(userId, email, List.of("ROLE_USER"));

        // Act
        boolean isValid = jwtService.validateToken(token);

        // Assert
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should reject invalid token")
    void validateToken_invalidToken_returnsFalse() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        boolean isValid = jwtService.validateToken(invalidToken);

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should extract user ID from valid token")
    void getUserIdFromToken_validToken_returnsUserId() {
        // Arrange
        String userId = "user-123";
        String email = "test@example.com";
        String token = jwtService.generateAccessToken(userId, email, List.of("ROLE_USER"));

        // Act
        String extractedUserId = jwtService.getUserIdFromToken(token);

        // Assert
        assertThat(extractedUserId).isEqualTo(userId);
    }

    @Test
    @DisplayName("Should return correct expiration time")
    void getExpirationMs_returnsConfiguredValue() {
        // Act
        long expirationMs = jwtService.getExpirationMs();

        // Assert
        assertThat(expirationMs).isEqualTo(3600000L);
    }

    @Test
    @DisplayName("Should generate different tokens for different users")
    void generateAccessToken_differentUsers_generatesDifferentTokens() {
        // Arrange
        String user1Id = "user-1";
        String user2Id = "user-2";
        String email = "test@example.com";
        List<String> roles = List.of("ROLE_USER");

        // Act
        String token1 = jwtService.generateAccessToken(user1Id, email, roles);
        String token2 = jwtService.generateAccessToken(user2Id, email, roles);

        // Assert
        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    @DisplayName("Should generate valid refresh tokens on multiple calls")
    void generateRefreshToken_multipleCalls_generatesValidTokens() {
        // Arrange
        String userId = "user-123";

        // Act
        String token1 = jwtService.generateRefreshToken(userId);
        String token2 = jwtService.generateRefreshToken(userId);

        // Assert - both tokens should be valid even if identical when generated in same second
        assertThat(token1).isNotNull();
        assertThat(token2).isNotNull();
        assertThat(jwtService.validateToken(token1)).isTrue();
        assertThat(jwtService.validateToken(token2)).isTrue();
    }
}

