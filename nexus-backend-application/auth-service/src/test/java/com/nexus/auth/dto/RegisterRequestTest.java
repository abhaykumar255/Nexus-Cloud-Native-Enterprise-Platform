package com.nexus.auth.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RegisterRequest DTO Tests")
class RegisterRequestTest {

    @Test
    @DisplayName("RegisterRequest no-args constructor creates instance")
    void registerRequest_noArgsConstructor() {
        // Act
        RegisterRequest request = new RegisterRequest();

        // Assert
        assertThat(request).isNotNull();
    }

    @Test
    @DisplayName("RegisterRequest setters work correctly")
    void registerRequest_settersWorkCorrectly() {
        // Arrange
        RegisterRequest request = new RegisterRequest();

        // Act
        request.setEmail("test@example.com");
        request.setUsername("testuser");
        request.setPassword("SecurePass123!");

        // Assert
        assertThat(request.getEmail()).isEqualTo("test@example.com");
        assertThat(request.getUsername()).isEqualTo("testuser");
        assertThat(request.getPassword()).isEqualTo("SecurePass123!");
    }

    @Test
    @DisplayName("RegisterRequest getters work correctly")
    void registerRequest_gettersWorkCorrectly() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@example.com");
        request.setUsername("username");
        request.setPassword("password123");

        // Act & Assert
        assertThat(request.getEmail()).isEqualTo("user@example.com");
        assertThat(request.getUsername()).isEqualTo("username");
        assertThat(request.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("RegisterRequest equals and hashCode work correctly")
    void registerRequest_equalsAndHashCode() {
        // Arrange
        RegisterRequest request1 = new RegisterRequest();
        request1.setEmail("test@example.com");
        request1.setUsername("testuser");
        request1.setPassword("password123");

        RegisterRequest request2 = new RegisterRequest();
        request2.setEmail("test@example.com");
        request2.setUsername("testuser");
        request2.setPassword("password123");

        RegisterRequest request3 = new RegisterRequest();
        request3.setEmail("other@example.com");
        request3.setUsername("otheruser");
        request3.setPassword("differentpass");

        // Assert
        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("RegisterRequest toString contains email and username")
    void registerRequest_toStringContainsEmailAndUsername() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setUsername("testuser");
        request.setPassword("password");

        // Act
        String toString = request.toString();

        // Assert
        assertThat(toString).contains("test@example.com");
        assertThat(toString).contains("testuser");
    }
}

