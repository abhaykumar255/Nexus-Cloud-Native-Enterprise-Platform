package com.nexus.auth.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PasswordResetToken Entity Tests")
class PasswordResetTokenTest {

    @Test
    @DisplayName("PasswordResetToken builder creates valid token")
    void passwordResetTokenBuilder_createsValidToken() {
        // Arrange
        Instant expiresAt = Instant.now().plusSeconds(3600);
        Instant usedAt = Instant.now();

        // Act
        PasswordResetToken token = PasswordResetToken.builder()
                .id("token-123")
                .token("reset-token-value")
                .userId("user-123")
                .expiresAt(expiresAt)
                .used(true)
                .usedAt(usedAt)
                .build();

        // Assert
        assertThat(token.getId()).isEqualTo("token-123");
        assertThat(token.getToken()).isEqualTo("reset-token-value");
        assertThat(token.getUserId()).isEqualTo("user-123");
        assertThat(token.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(token.isUsed()).isTrue();
        assertThat(token.getUsedAt()).isEqualTo(usedAt);
    }

    @Test
    @DisplayName("PasswordResetToken default values are set correctly")
    void passwordResetTokenBuilder_setsDefaultValues() {
        // Arrange & Act
        PasswordResetToken token = PasswordResetToken.builder()
                .token("reset-token-value")
                .userId("user-123")
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        // Assert
        assertThat(token.isUsed()).isFalse();
    }

    @Test
    @DisplayName("PasswordResetToken setters work correctly")
    void passwordResetToken_settersWorkCorrectly() {
        // Arrange
        PasswordResetToken token = new PasswordResetToken();
        Instant expiresAt = Instant.now().plusSeconds(3600);
        Instant usedAt = Instant.now();
        Instant createdAt = Instant.now();

        // Act
        token.setId("token-456");
        token.setToken("new-reset-token");
        token.setUserId("user-456");
        token.setExpiresAt(expiresAt);
        token.setUsed(true);
        token.setUsedAt(usedAt);
        token.setCreatedAt(createdAt);

        // Assert
        assertThat(token.getId()).isEqualTo("token-456");
        assertThat(token.getToken()).isEqualTo("new-reset-token");
        assertThat(token.getUserId()).isEqualTo("user-456");
        assertThat(token.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(token.isUsed()).isTrue();
        assertThat(token.getUsedAt()).isEqualTo(usedAt);
        assertThat(token.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("PasswordResetToken equals and hashCode work correctly")
    void passwordResetToken_equalsAndHashCode() {
        // Arrange
        Instant expiresAt = Instant.now().plusSeconds(3600);
        
        PasswordResetToken token1 = PasswordResetToken.builder()
                .id("token-123")
                .token("reset-token-value")
                .userId("user-123")
                .expiresAt(expiresAt)
                .build();

        PasswordResetToken token2 = PasswordResetToken.builder()
                .id("token-123")
                .token("reset-token-value")
                .userId("user-123")
                .expiresAt(expiresAt)
                .build();

        PasswordResetToken token3 = PasswordResetToken.builder()
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
    @DisplayName("PasswordResetToken toString contains key fields")
    void passwordResetToken_toStringContainsKeyFields() {
        // Arrange
        PasswordResetToken token = PasswordResetToken.builder()
                .id("token-123")
                .token("reset-token-value")
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
    @DisplayName("PasswordResetToken no-args constructor works")
    void passwordResetToken_noArgsConstructor() {
        // Act
        PasswordResetToken token = new PasswordResetToken();

        // Assert
        assertThat(token).isNotNull();
        assertThat(token.getId()).isNull();
        assertThat(token.getToken()).isNull();
        assertThat(token.getUserId()).isNull();
    }

    @Test
    @DisplayName("PasswordResetToken all-args constructor works")
    void passwordResetToken_allArgsConstructor() {
        // Arrange
        Instant expiresAt = Instant.now().plusSeconds(3600);
        Instant usedAt = Instant.now();
        Instant createdAt = Instant.now();

        // Act
        PasswordResetToken token = new PasswordResetToken(
                "token-123",
                "reset-token-value",
                "user-123",
                expiresAt,
                false,
                usedAt,
                createdAt
        );

        // Assert
        assertThat(token.getId()).isEqualTo("token-123");
        assertThat(token.getToken()).isEqualTo("reset-token-value");
        assertThat(token.getUserId()).isEqualTo("user-123");
        assertThat(token.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(token.isUsed()).isFalse();
        assertThat(token.getUsedAt()).isEqualTo(usedAt);
    }
}

