package com.nexus.auth.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Entity Tests")
class UserTest {

    @Test
    @DisplayName("User builder creates valid user")
    void userBuilder_createsValidUser() {
        // Arrange & Act
        User user = User.builder()
                .id("user-123")
                .email("test@example.com")
                .username("testuser")
                .passwordHash("hashed-password")
                .emailVerified(true)
                .active(true)
                .locked(false)
                .authProvider(User.AuthProvider.LOCAL)
                .failedLoginAttempts(0)
                .lastLoginAt(Instant.now())
                .build();

        // Assert
        assertThat(user.getId()).isEqualTo("user-123");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getPasswordHash()).isEqualTo("hashed-password");
        assertThat(user.isEmailVerified()).isTrue();
        assertThat(user.isActive()).isTrue();
        assertThat(user.isLocked()).isFalse();
        assertThat(user.getAuthProvider()).isEqualTo(User.AuthProvider.LOCAL);
        assertThat(user.getFailedLoginAttempts()).isZero();
        assertThat(user.getLastLoginAt()).isNotNull();
    }

    @Test
    @DisplayName("User default values are set correctly")
    void userBuilder_setsDefaultValues() {
        // Arrange & Act
        User user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .passwordHash("hashed-password")
                .build();

        // Assert
        assertThat(user.isEmailVerified()).isFalse();
        assertThat(user.isActive()).isTrue();
        assertThat(user.isLocked()).isFalse();
        assertThat(user.getAuthProvider()).isEqualTo(User.AuthProvider.LOCAL);
        assertThat(user.getFailedLoginAttempts()).isZero();
    }

    @Test
    @DisplayName("User setters work correctly")
    void user_settersWorkCorrectly() {
        // Arrange
        User user = new User();
        Instant now = Instant.now();

        // Act
        user.setId("user-456");
        user.setEmail("new@example.com");
        user.setUsername("newuser");
        user.setPasswordHash("new-hash");
        user.setEmailVerified(true);
        user.setActive(false);
        user.setLocked(true);
        user.setAuthProvider(User.AuthProvider.GOOGLE);
        user.setProviderId("google-123");
        user.setFailedLoginAttempts(3);
        user.setLastLoginAt(now);
        user.setLockedUntil(now);
        user.setPasswordChangedAt(now);

        // Assert
        assertThat(user.getId()).isEqualTo("user-456");
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getUsername()).isEqualTo("newuser");
        assertThat(user.getPasswordHash()).isEqualTo("new-hash");
        assertThat(user.isEmailVerified()).isTrue();
        assertThat(user.isActive()).isFalse();
        assertThat(user.isLocked()).isTrue();
        assertThat(user.getAuthProvider()).isEqualTo(User.AuthProvider.GOOGLE);
        assertThat(user.getProviderId()).isEqualTo("google-123");
        assertThat(user.getFailedLoginAttempts()).isEqualTo(3);
        assertThat(user.getLastLoginAt()).isEqualTo(now);
        assertThat(user.getLockedUntil()).isEqualTo(now);
        assertThat(user.getPasswordChangedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("User equals and hashCode work correctly")
    void user_equalsAndHashCode() {
        // Arrange
        User user1 = User.builder()
                .id("user-123")
                .email("test@example.com")
                .username("testuser")
                .passwordHash("hash")
                .build();

        User user2 = User.builder()
                .id("user-123")
                .email("test@example.com")
                .username("testuser")
                .passwordHash("hash")
                .build();

        User user3 = User.builder()
                .id("user-456")
                .email("other@example.com")
                .username("otheruser")
                .passwordHash("hash2")
                .build();

        // Assert
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    @DisplayName("User toString contains key fields")
    void user_toStringContainsKeyFields() {
        // Arrange
        User user = User.builder()
                .id("user-123")
                .email("test@example.com")
                .username("testuser")
                .passwordHash("hash")
                .build();

        // Act
        String toString = user.toString();

        // Assert
        assertThat(toString).contains("user-123");
        assertThat(toString).contains("test@example.com");
        assertThat(toString).contains("testuser");
    }

    @Test
    @DisplayName("AuthProvider enum values are correct")
    void authProvider_enumValues() {
        assertThat(User.AuthProvider.LOCAL).isNotNull();
        assertThat(User.AuthProvider.GOOGLE).isNotNull();
        assertThat(User.AuthProvider.GITHUB).isNotNull();
        assertThat(User.AuthProvider.values()).hasSize(3);
    }

    @Test
    @DisplayName("User no-args constructor works")
    void user_noArgsConstructor() {
        // Act
        User user = new User();

        // Assert
        assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("AuthProvider valueOf works correctly")
    void authProvider_valueOf() {
        assertThat(User.AuthProvider.valueOf("LOCAL")).isEqualTo(User.AuthProvider.LOCAL);
        assertThat(User.AuthProvider.valueOf("GOOGLE")).isEqualTo(User.AuthProvider.GOOGLE);
        assertThat(User.AuthProvider.valueOf("GITHUB")).isEqualTo(User.AuthProvider.GITHUB);
    }

    @Test
    @DisplayName("User setters update fields correctly")
    void user_settersUpdateFields() {
        // Arrange
        User user = new User();
        Instant now = Instant.now();

        // Act
        user.setId("user-456");
        user.setEmail("new@example.com");
        user.setUsername("newuser");
        user.setPasswordHash("new-hash");
        user.setEmailVerified(true);
        user.setActive(false);
        user.setLocked(true);
        user.setAuthProvider(User.AuthProvider.GOOGLE);
        user.setProviderId("google-123");
        user.setFailedLoginAttempts(3);
        user.setLastLoginAt(now);

        // Assert
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getUsername()).isEqualTo("newuser");
        assertThat(user.getPasswordHash()).isEqualTo("new-hash");
        assertThat(user.isEmailVerified()).isTrue();
        assertThat(user.isActive()).isFalse();
        assertThat(user.isLocked()).isTrue();
        assertThat(user.getAuthProvider()).isEqualTo(User.AuthProvider.GOOGLE);
        assertThat(user.getProviderId()).isEqualTo("google-123");
        assertThat(user.getFailedLoginAttempts()).isEqualTo(3);
        assertThat(user.getLastLoginAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("User builder with GITHUB provider creates valid user")
    void user_builderWithGithubProvider() {
        // Arrange & Act
        User user = User.builder()
                .email("github@example.com")
                .username("githubuser")
                .authProvider(User.AuthProvider.GITHUB)
                .providerId("github-123")
                .build();

        // Assert
        assertThat(user.getAuthProvider()).isEqualTo(User.AuthProvider.GITHUB);
        assertThat(user.getProviderId()).isEqualTo("github-123");
    }

    @Test
    @DisplayName("User with lockedUntil and passwordChangedAt")
    void user_withLockedUntilAndPasswordChangedAt() {
        // Arrange
        Instant lockedUntil = Instant.now().plusSeconds(3600);
        Instant passwordChangedAt = Instant.now().minusSeconds(86400);

        // Act
        User user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .passwordHash("hash")
                .lockedUntil(lockedUntil)
                .passwordChangedAt(passwordChangedAt)
                .build();

        // Assert
        assertThat(user.getLockedUntil()).isEqualTo(lockedUntil);
        assertThat(user.getPasswordChangedAt()).isEqualTo(passwordChangedAt);
    }

    @Test
    @DisplayName("User sets updatedAt timestamp")
    void user_setsUpdatedAtTimestamp() {
        // Arrange
        User user = new User();
        Instant updatedAt = Instant.now();

        // Act
        user.setUpdatedAt(updatedAt);

        // Assert
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("User sets createdAt timestamp")
    void user_setsCreatedAtTimestamp() {
        // Arrange
        User user = new User();
        Instant createdAt = Instant.now();

        // Act
        user.setCreatedAt(createdAt);

        // Assert
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
    }
}

