package com.nexus.auth.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RefreshToken Entity Tests")
class RefreshTokenTest {

    @Test
    @DisplayName("RefreshToken builder creates valid token")
    void refreshTokenBuilder_createsValidToken() {
        // Arrange
        Instant expiresAt = Instant.now().plusSeconds(3600);
        Instant revokedAt = Instant.now();

        // Act
        RefreshToken token = RefreshToken.builder()
                .id("token-123")
                .token("refresh-token-value")
                .userId("user-123")
                .expiresAt(expiresAt)
                .revoked(true)
                .revokedAt(revokedAt)
                .deviceInfo("Chrome/Windows")
                .ipAddress("192.168.1.1")
                .build();

        // Assert
        assertThat(token.getId()).isEqualTo("token-123");
        assertThat(token.getToken()).isEqualTo("refresh-token-value");
        assertThat(token.getUserId()).isEqualTo("user-123");
        assertThat(token.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(token.isRevoked()).isTrue();
        assertThat(token.getRevokedAt()).isEqualTo(revokedAt);
        assertThat(token.getDeviceInfo()).isEqualTo("Chrome/Windows");
        assertThat(token.getIpAddress()).isEqualTo("192.168.1.1");
    }

    @Test
    @DisplayName("RefreshToken default values are set correctly")
    void refreshTokenBuilder_setsDefaultValues() {
        // Arrange & Act
        RefreshToken token = RefreshToken.builder()
                .token("refresh-token-value")
                .userId("user-123")
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        // Assert
        assertThat(token.isRevoked()).isFalse();
    }

    @Test
    @DisplayName("RefreshToken setters work correctly")
    void refreshToken_settersWorkCorrectly() {
        // Arrange
        RefreshToken token = new RefreshToken();
        Instant expiresAt = Instant.now().plusSeconds(3600);
        Instant revokedAt = Instant.now();
        Instant createdAt = Instant.now();

        // Act
        token.setId("token-456");
        token.setToken("new-token-value");
        token.setUserId("user-456");
        token.setExpiresAt(expiresAt);
        token.setRevoked(true);
        token.setRevokedAt(revokedAt);
        token.setDeviceInfo("Firefox/Linux");
        token.setIpAddress("10.0.0.1");
        token.setCreatedAt(createdAt);

        // Assert
        assertThat(token.getId()).isEqualTo("token-456");
        assertThat(token.getToken()).isEqualTo("new-token-value");
        assertThat(token.getUserId()).isEqualTo("user-456");
        assertThat(token.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(token.isRevoked()).isTrue();
        assertThat(token.getRevokedAt()).isEqualTo(revokedAt);
        assertThat(token.getDeviceInfo()).isEqualTo("Firefox/Linux");
        assertThat(token.getIpAddress()).isEqualTo("10.0.0.1");
        assertThat(token.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("RefreshToken equals and hashCode work correctly")
    void refreshToken_equalsAndHashCode() {
        // Arrange
        Instant expiresAt = Instant.now().plusSeconds(3600);
        
        RefreshToken token1 = RefreshToken.builder()
                .id("token-123")
                .token("refresh-token-value")
                .userId("user-123")
                .expiresAt(expiresAt)
                .build();

        RefreshToken token2 = RefreshToken.builder()
                .id("token-123")
                .token("refresh-token-value")
                .userId("user-123")
                .expiresAt(expiresAt)
                .build();

        RefreshToken token3 = RefreshToken.builder()
                .id("token-456")
                .token("different-token")
                .userId("user-456")
                .expiresAt(expiresAt)
                .build();

        // Assert
        assertThat(token1).isEqualTo(token2);
        assertThat(token1).isNotEqualTo(token3);
        assertThat(token1.hashCode()).isEqualTo(token2.hashCode());
    }

    @Test
    @DisplayName("RefreshToken toString contains key fields")
    void refreshToken_toStringContainsKeyFields() {
        // Arrange
        RefreshToken token = RefreshToken.builder()
                .id("token-123")
                .token("refresh-token-value")
                .userId("user-123")
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        // Act
        String toString = token.toString();

        // Assert
        assertThat(toString).contains("token-123");
        assertThat(toString).contains("user-123");
    }

    @Test
    @DisplayName("RefreshToken no-args constructor works")
    void refreshToken_noArgsConstructor() {
        // Act
        RefreshToken token = new RefreshToken();

        // Assert
        assertThat(token).isNotNull();
        assertThat(token.getId()).isNull();
        assertThat(token.getToken()).isNull();
        assertThat(token.getUserId()).isNull();
    }

    @Test
    @DisplayName("RefreshToken all-args constructor works")
    void refreshToken_allArgsConstructor() {
        // Arrange
        Instant expiresAt = Instant.now().plusSeconds(3600);
        Instant revokedAt = Instant.now();
        Instant createdAt = Instant.now();

        // Act
        RefreshToken token = new RefreshToken(
                "token-123",
                "refresh-token-value",
                "user-123",
                expiresAt,
                false,
                revokedAt,
                "Chrome/Windows",
                "192.168.1.1",
                createdAt
        );

        // Assert
        assertThat(token.getId()).isEqualTo("token-123");
        assertThat(token.getToken()).isEqualTo("refresh-token-value");
        assertThat(token.getUserId()).isEqualTo("user-123");
        assertThat(token.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(token.isRevoked()).isFalse();
    }

    @Test
    @DisplayName("RefreshToken builder for deviceInfo and ipAddress works")
    void refreshToken_builderForDeviceInfoAndIpAddress() {
        // Arrange & Act
        RefreshToken token = RefreshToken.builder()
                .token("test-token")
                .userId("user-123")
                .expiresAt(Instant.now().plusSeconds(3600))
                .deviceInfo("Firefox/Linux")
                .ipAddress("10.0.0.1")
                .build();

        // Assert
        assertThat(token.getDeviceInfo()).isEqualTo("Firefox/Linux");
        assertThat(token.getIpAddress()).isEqualTo("10.0.0.1");
    }

    @Test
    @DisplayName("RefreshToken setter for revokedAt works")
    void refreshToken_setterForRevokedAt() {
        // Arrange
        RefreshToken token = new RefreshToken();
        Instant revokedAt = Instant.now();

        // Act
        token.setRevokedAt(revokedAt);

        // Assert
        assertThat(token.getRevokedAt()).isEqualTo(revokedAt);
    }
}

