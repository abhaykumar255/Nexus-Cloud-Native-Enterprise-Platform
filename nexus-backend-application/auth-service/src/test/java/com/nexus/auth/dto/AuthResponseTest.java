package com.nexus.auth.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuthResponse DTO Tests")
class AuthResponseTest {

    @Test
    @DisplayName("AuthResponse builder creates valid response")
    void authResponseBuilder_createsValidResponse() {
        // Arrange
        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                .id("user-123")
                .email("test@example.com")
                .username("testuser")
                .emailVerified(true)
                .build();

        // Act
        AuthResponse response = AuthResponse.builder()
                .accessToken("access-token-123")
                .refreshToken("refresh-token-123")
                .tokenType("Bearer")
                .expiresIn(3600000L)
                .user(userInfo)
                .build();

        // Assert
        assertThat(response.getAccessToken()).isEqualTo("access-token-123");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token-123");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getExpiresIn()).isEqualTo(3600000L);
        assertThat(response.getUser()).isNotNull();
        assertThat(response.getUser().getId()).isEqualTo("user-123");
    }

    @Test
    @DisplayName("AuthResponse setters work correctly")
    void authResponse_settersWorkCorrectly() {
        // Arrange
        AuthResponse response = new AuthResponse();
        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                .id("user-456")
                .email("new@example.com")
                .username("newuser")
                .emailVerified(false)
                .build();

        // Act
        response.setAccessToken("new-access-token");
        response.setRefreshToken("new-refresh-token");
        response.setTokenType("Bearer");
        response.setExpiresIn(7200000L);
        response.setUser(userInfo);

        // Assert
        assertThat(response.getAccessToken()).isEqualTo("new-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("new-refresh-token");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getExpiresIn()).isEqualTo(7200000L);
        assertThat(response.getUser()).isEqualTo(userInfo);
    }

    @Test
    @DisplayName("UserInfo builder creates valid user info")
    void userInfoBuilder_createsValidUserInfo() {
        // Act
        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                .id("user-789")
                .email("user@example.com")
                .username("username")
                .emailVerified(true)
                .build();

        // Assert
        assertThat(userInfo.getId()).isEqualTo("user-789");
        assertThat(userInfo.getEmail()).isEqualTo("user@example.com");
        assertThat(userInfo.getUsername()).isEqualTo("username");
        assertThat(userInfo.isEmailVerified()).isTrue();
    }

    @Test
    @DisplayName("UserInfo setters work correctly")
    void userInfo_settersWorkCorrectly() {
        // Arrange
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();

        // Act
        userInfo.setId("user-999");
        userInfo.setEmail("updated@example.com");
        userInfo.setUsername("updateduser");
        userInfo.setEmailVerified(false);

        // Assert
        assertThat(userInfo.getId()).isEqualTo("user-999");
        assertThat(userInfo.getEmail()).isEqualTo("updated@example.com");
        assertThat(userInfo.getUsername()).isEqualTo("updateduser");
        assertThat(userInfo.isEmailVerified()).isFalse();
    }

    @Test
    @DisplayName("AuthResponse equals and hashCode work correctly")
    void authResponse_equalsAndHashCode() {
        // Arrange
        AuthResponse response1 = AuthResponse.builder()
                .accessToken("token-123")
                .refreshToken("refresh-123")
                .tokenType("Bearer")
                .expiresIn(3600000L)
                .build();

        AuthResponse response2 = AuthResponse.builder()
                .accessToken("token-123")
                .refreshToken("refresh-123")
                .tokenType("Bearer")
                .expiresIn(3600000L)
                .build();

        AuthResponse response3 = AuthResponse.builder()
                .accessToken("different-token")
                .refreshToken("different-refresh")
                .tokenType("Bearer")
                .expiresIn(7200000L)
                .build();

        // Assert
        assertThat(response1).isEqualTo(response2);
        assertThat(response1).isNotEqualTo(response3);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    @DisplayName("UserInfo equals and hashCode work correctly")
    void userInfo_equalsAndHashCode() {
        // Arrange
        AuthResponse.UserInfo userInfo1 = AuthResponse.UserInfo.builder()
                .id("user-123")
                .email("test@example.com")
                .username("testuser")
                .emailVerified(true)
                .build();

        AuthResponse.UserInfo userInfo2 = AuthResponse.UserInfo.builder()
                .id("user-123")
                .email("test@example.com")
                .username("testuser")
                .emailVerified(true)
                .build();

        AuthResponse.UserInfo userInfo3 = AuthResponse.UserInfo.builder()
                .id("user-456")
                .email("other@example.com")
                .username("otheruser")
                .emailVerified(false)
                .build();

        // Assert
        assertThat(userInfo1).isEqualTo(userInfo2);
        assertThat(userInfo1).isNotEqualTo(userInfo3);
        assertThat(userInfo1.hashCode()).isEqualTo(userInfo2.hashCode());
    }

    @Test
    @DisplayName("AuthResponse toString contains key fields")
    void authResponse_toStringContainsKeyFields() {
        // Arrange
        AuthResponse response = AuthResponse.builder()
                .accessToken("token-123")
                .tokenType("Bearer")
                .build();

        // Act
        String toString = response.toString();

        // Assert
        assertThat(toString).contains("Bearer");
    }

    @Test
    @DisplayName("AuthResponse no-args constructor works")
    void authResponse_noArgsConstructor() {
        // Act
        AuthResponse response = new AuthResponse();

        // Assert
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("UserInfo no-args constructor works")
    void userInfo_noArgsConstructor() {
        // Act
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();

        // Assert
        assertThat(userInfo).isNotNull();
    }

    @Test
    @DisplayName("UserInfo toString contains email")
    void userInfo_toStringContainsEmail() {
        // Arrange
        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                .id("user-123")
                .email("test@example.com")
                .username("testuser")
                .emailVerified(true)
                .build();

        // Act
        String toString = userInfo.toString();

        // Assert
        assertThat(toString).contains("test@example.com");
    }
}

