package com.nexus.auth.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LoginRequest DTO Tests")
class LoginRequestTest {

    @Test
    @DisplayName("LoginRequest no-args constructor creates instance")
    void loginRequest_noArgsConstructor() {
        // Act
        LoginRequest request = new LoginRequest();

        // Assert
        assertThat(request).isNotNull();
    }

    @Test
    @DisplayName("LoginRequest setters work correctly")
    void loginRequest_settersWorkCorrectly() {
        // Arrange
        LoginRequest request = new LoginRequest();

        // Act
        request.setUsernameOrEmail("test@example.com");
        request.setPassword("SecurePass123!");

        // Assert
        assertThat(request.getUsernameOrEmail()).isEqualTo("test@example.com");
        assertThat(request.getPassword()).isEqualTo("SecurePass123!");
    }

    @Test
    @DisplayName("LoginRequest equals and hashCode work correctly")
    void loginRequest_equalsAndHashCode() {
        // Arrange
        LoginRequest request1 = new LoginRequest();
        request1.setUsernameOrEmail("test@example.com");
        request1.setPassword("password123");

        LoginRequest request2 = new LoginRequest();
        request2.setUsernameOrEmail("test@example.com");
        request2.setPassword("password123");

        LoginRequest request3 = new LoginRequest();
        request3.setUsernameOrEmail("other@example.com");
        request3.setPassword("differentpass");

        // Assert
        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("LoginRequest toString contains usernameOrEmail")
    void loginRequest_toStringContainsUsernameOrEmail() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("testuser");
        request.setPassword("password");

        // Act
        String toString = request.toString();

        // Assert
        assertThat(toString).contains("testuser");
    }

    @Test
    @DisplayName("LoginRequest getters work correctly")
    void loginRequest_gettersWorkCorrectly() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("username");
        request.setPassword("pass123");

        // Act & Assert
        assertThat(request.getUsernameOrEmail()).isEqualTo("username");
        assertThat(request.getPassword()).isEqualTo("pass123");
    }
}

