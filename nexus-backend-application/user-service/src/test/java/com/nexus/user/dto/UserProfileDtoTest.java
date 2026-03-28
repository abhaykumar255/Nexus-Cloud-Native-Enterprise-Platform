package com.nexus.user.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UserProfileDto Unit Tests")
class UserProfileDtoTest {

    @Test
    @DisplayName("Builder creates DTO with all fields")
    void builder_createsDtoWithAllFields() {
        // Arrange
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        Instant now = Instant.now();

        // Act
        UserProfileDto dto = UserProfileDto.builder()
            .id("profile-123")
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .firstName("John")
            .lastName("Doe")
            .phoneNumber("+1234567890")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .bio("Test bio")
            .avatarUrl("https://example.com/avatar.jpg")
            .department("Engineering")
            .jobTitle("Developer")
            .location("New York")
            .timezone("EST")
            .language("en")
            .profileComplete(true)
            .roles(roles)
            .createdAt(now)
            .updatedAt(now)
            .build();

        // Assert
        assertThat(dto.getId()).isEqualTo("profile-123");
        assertThat(dto.getUserId()).isEqualTo("user-123");
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
        assertThat(dto.getUsername()).isEqualTo("testuser");
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getLastName()).isEqualTo("Doe");
        assertThat(dto.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(dto.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(dto.getBio()).isEqualTo("Test bio");
        assertThat(dto.getAvatarUrl()).isEqualTo("https://example.com/avatar.jpg");
        assertThat(dto.getDepartment()).isEqualTo("Engineering");
        assertThat(dto.getJobTitle()).isEqualTo("Developer");
        assertThat(dto.getLocation()).isEqualTo("New York");
        assertThat(dto.getTimezone()).isEqualTo("EST");
        assertThat(dto.getLanguage()).isEqualTo("en");
        assertThat(dto.isProfileComplete()).isTrue();
        assertThat(dto.getRoles()).containsExactly("ROLE_USER");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("No-args constructor creates empty DTO")
    void noArgsConstructor_createsEmptyDto() {
        // Act
        UserProfileDto dto = new UserProfileDto();

        // Assert
        assertThat(dto.getId()).isNull();
        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getEmail()).isNull();
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        UserProfileDto dto = new UserProfileDto();
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        Instant now = Instant.now();

        // Act
        dto.setId("profile-456");
        dto.setUserId("user-456");
        dto.setEmail("new@example.com");
        dto.setUsername("newuser");
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setPhoneNumber("+9876543210");
        dto.setDateOfBirth(LocalDate.of(1995, 5, 5));
        dto.setBio("New bio");
        dto.setAvatarUrl("https://example.com/new-avatar.jpg");
        dto.setDepartment("Sales");
        dto.setJobTitle("Manager");
        dto.setLocation("London");
        dto.setTimezone("GMT");
        dto.setLanguage("fr");
        dto.setProfileComplete(false);
        dto.setRoles(roles);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);

        // Assert
        assertThat(dto.getId()).isEqualTo("profile-456");
        assertThat(dto.getUserId()).isEqualTo("user-456");
        assertThat(dto.getEmail()).isEqualTo("new@example.com");
        assertThat(dto.getUsername()).isEqualTo("newuser");
        assertThat(dto.getFirstName()).isEqualTo("Jane");
        assertThat(dto.getLastName()).isEqualTo("Smith");
        assertThat(dto.getPhoneNumber()).isEqualTo("+9876543210");
        assertThat(dto.getDateOfBirth()).isEqualTo(LocalDate.of(1995, 5, 5));
        assertThat(dto.getBio()).isEqualTo("New bio");
        assertThat(dto.getAvatarUrl()).isEqualTo("https://example.com/new-avatar.jpg");
        assertThat(dto.getDepartment()).isEqualTo("Sales");
        assertThat(dto.getJobTitle()).isEqualTo("Manager");
        assertThat(dto.getLocation()).isEqualTo("London");
        assertThat(dto.getTimezone()).isEqualTo("GMT");
        assertThat(dto.getLanguage()).isEqualTo("fr");
        assertThat(dto.isProfileComplete()).isFalse();
        assertThat(dto.getRoles()).containsExactly("ROLE_ADMIN");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        // Arrange
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        UserProfileDto dto1 = UserProfileDto.builder()
            .id("profile-123")
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .firstName("John")
            .roles(roles)
            .build();

        UserProfileDto dto2 = UserProfileDto.builder()
            .id("profile-123")
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .firstName("John")
            .roles(roles)
            .build();

        UserProfileDto dto3 = UserProfileDto.builder()
            .id("profile-456")
            .userId("user-456")
            .email("other@example.com")
            .build();

        // Assert
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1).isNotEqualTo(dto3);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isEqualTo(dto1);
        assertThat(dto1).isNotEqualTo(null);
        assertThat(dto1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        UserProfileDto dto = UserProfileDto.builder()
            .id("profile-123")
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .firstName("John")
            .lastName("Doe")
            .build();

        // Act
        String result = dto.toString();

        // Assert
        assertThat(result).contains("profile-123");
        assertThat(result).contains("user-123");
        assertThat(result).contains("test@example.com");
        assertThat(result).contains("testuser");
        assertThat(result).contains("John");
        assertThat(result).contains("Doe");
    }
}

