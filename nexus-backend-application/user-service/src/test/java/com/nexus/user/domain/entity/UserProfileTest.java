package com.nexus.user.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UserProfile Entity Unit Tests")
class UserProfileTest {

    @Test
    @DisplayName("Builder creates entity with all fields")
    void builder_createsEntityWithAllFields() {
        // Arrange
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");

        // Act
        UserProfile profile = UserProfile.builder()
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
            .build();

        // Assert
        assertThat(profile.getId()).isEqualTo("profile-123");
        assertThat(profile.getUserId()).isEqualTo("user-123");
        assertThat(profile.getEmail()).isEqualTo("test@example.com");
        assertThat(profile.getUsername()).isEqualTo("testuser");
        assertThat(profile.getFirstName()).isEqualTo("John");
        assertThat(profile.getLastName()).isEqualTo("Doe");
        assertThat(profile.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(profile.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(profile.getBio()).isEqualTo("Test bio");
        assertThat(profile.getAvatarUrl()).isEqualTo("https://example.com/avatar.jpg");
        assertThat(profile.getDepartment()).isEqualTo("Engineering");
        assertThat(profile.getJobTitle()).isEqualTo("Developer");
        assertThat(profile.getLocation()).isEqualTo("New York");
        assertThat(profile.getTimezone()).isEqualTo("EST");
        assertThat(profile.getLanguage()).isEqualTo("en");
        assertThat(profile.isProfileComplete()).isTrue();
        assertThat(profile.getRoles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    @DisplayName("Default constructor creates empty entity")
    void defaultConstructor_createsEmptyEntity() {
        // Act
        UserProfile profile = new UserProfile();

        // Assert
        assertThat(profile.getId()).isNull();
        assertThat(profile.getUserId()).isNull();
        assertThat(profile.getEmail()).isNull();
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        UserProfile profile = new UserProfile();

        // Act
        profile.setId("profile-456");
        profile.setUserId("user-456");
        profile.setEmail("new@example.com");
        profile.setUsername("newuser");
        profile.setFirstName("Jane");
        profile.setLastName("Smith");
        profile.setPhoneNumber("+9876543210");
        profile.setDateOfBirth(LocalDate.of(1995, 5, 5));
        profile.setBio("New bio");
        profile.setAvatarUrl("https://example.com/new-avatar.jpg");
        profile.setDepartment("Sales");
        profile.setJobTitle("Manager");
        profile.setLocation("London");
        profile.setTimezone("GMT");
        profile.setLanguage("fr");
        profile.setProfileComplete(false);

        // Assert
        assertThat(profile.getId()).isEqualTo("profile-456");
        assertThat(profile.getUserId()).isEqualTo("user-456");
        assertThat(profile.getEmail()).isEqualTo("new@example.com");
        assertThat(profile.getUsername()).isEqualTo("newuser");
        assertThat(profile.getFirstName()).isEqualTo("Jane");
        assertThat(profile.getLastName()).isEqualTo("Smith");
        assertThat(profile.getPhoneNumber()).isEqualTo("+9876543210");
        assertThat(profile.getDateOfBirth()).isEqualTo(LocalDate.of(1995, 5, 5));
        assertThat(profile.getBio()).isEqualTo("New bio");
        assertThat(profile.getAvatarUrl()).isEqualTo("https://example.com/new-avatar.jpg");
        assertThat(profile.getDepartment()).isEqualTo("Sales");
        assertThat(profile.getJobTitle()).isEqualTo("Manager");
        assertThat(profile.getLocation()).isEqualTo("London");
        assertThat(profile.getTimezone()).isEqualTo("GMT");
        assertThat(profile.getLanguage()).isEqualTo("fr");
        assertThat(profile.isProfileComplete()).isFalse();
    }

    @Test
    @DisplayName("Roles can be added and removed")
    void roles_canBeAddedAndRemoved() {
        // Arrange
        UserProfile profile = UserProfile.builder()
            .id("profile-123")
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .roles(new HashSet<>())
            .build();

        // Act & Assert
        profile.getRoles().add("ROLE_USER");
        assertThat(profile.getRoles()).containsExactly("ROLE_USER");

        profile.getRoles().add("ROLE_ADMIN");
        assertThat(profile.getRoles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");

        profile.getRoles().remove("ROLE_USER");
        assertThat(profile.getRoles()).containsExactly("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Profile complete default is false")
    void profileComplete_defaultIsFalse() {
        // Act
        UserProfile profile = UserProfile.builder()
            .id("profile-123")
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .build();

        // Assert - Using default value
        assertThat(profile.isProfileComplete()).isFalse();
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        // Arrange
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        UserProfile profile1 = UserProfile.builder()
            .id("profile-123")
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .firstName("John")
            .roles(roles)
            .build();

        UserProfile profile2 = UserProfile.builder()
            .id("profile-123")
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .firstName("John")
            .roles(roles)
            .build();

        UserProfile profile3 = UserProfile.builder()
            .id("profile-456")
            .userId("user-456")
            .email("other@example.com")
            .username("otheruser")
            .build();

        // Assert
        assertThat(profile1).isEqualTo(profile2);
        assertThat(profile1).isNotEqualTo(profile3);
        assertThat(profile1.hashCode()).isEqualTo(profile2.hashCode());
        assertThat(profile1).isEqualTo(profile1);
        assertThat(profile1).isNotEqualTo(null);
        assertThat(profile1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        UserProfile profile = UserProfile.builder()
            .id("profile-123")
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .firstName("John")
            .lastName("Doe")
            .department("Engineering")
            .build();

        // Act
        String result = profile.toString();

        // Assert
        assertThat(result).contains("profile-123");
        assertThat(result).contains("user-123");
        assertThat(result).contains("test@example.com");
        assertThat(result).contains("testuser");
        assertThat(result).contains("John");
        assertThat(result).contains("Doe");
    }
}

