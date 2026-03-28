package com.nexus.search.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserDocument Tests")
class UserDocumentTest {

    @Test
    @DisplayName("Builder creates instance with all fields")
    void builder_createsInstanceWithAllFields() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        List<String> roles = Arrays.asList("ADMIN", "USER");

        // Act
        UserDocument document = UserDocument.builder()
                .id("user-123")
                .username("johndoe")
                .email("john@example.com")
                .firstName("John")
                .lastName("Doe")
                .fullName("John Doe")
                .department("Engineering")
                .position("Senior Developer")
                .roles(roles)
                .status("ACTIVE")
                .createdAt(now)
                .build();

        // Assert
        assertThat(document.getId()).isEqualTo("user-123");
        assertThat(document.getUsername()).isEqualTo("johndoe");
        assertThat(document.getEmail()).isEqualTo("john@example.com");
        assertThat(document.getFirstName()).isEqualTo("John");
        assertThat(document.getLastName()).isEqualTo("Doe");
        assertThat(document.getFullName()).isEqualTo("John Doe");
        assertThat(document.getDepartment()).isEqualTo("Engineering");
        assertThat(document.getPosition()).isEqualTo("Senior Developer");
        assertThat(document.getRoles()).containsExactly("ADMIN", "USER");
        assertThat(document.getStatus()).isEqualTo("ACTIVE");
        assertThat(document.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("No-args constructor creates instance")
    void noArgsConstructor_createsInstance() {
        // Act
        UserDocument document = new UserDocument();

        // Assert
        assertThat(document).isNotNull();
    }

    @Test
    @DisplayName("Setters update fields")
    void setters_updateFields() {
        // Arrange
        UserDocument document = new UserDocument();

        // Act
        document.setId("user-999");
        document.setUsername("janedoe");
        document.setEmail("jane@example.com");
        document.setStatus("INACTIVE");

        // Assert
        assertThat(document.getId()).isEqualTo("user-999");
        assertThat(document.getUsername()).isEqualTo("janedoe");
        assertThat(document.getEmail()).isEqualTo("jane@example.com");
        assertThat(document.getStatus()).isEqualTo("INACTIVE");
    }
}

