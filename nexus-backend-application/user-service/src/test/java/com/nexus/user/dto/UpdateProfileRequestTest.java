package com.nexus.user.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UpdateProfileRequest Unit Tests")
class UpdateProfileRequestTest {

    @Test
    @DisplayName("No-args constructor creates empty request")
    void noArgsConstructor_createsEmptyRequest() {
        // Act
        UpdateProfileRequest request = new UpdateProfileRequest();

        // Assert
        assertThat(request.getFirstName()).isNull();
        assertThat(request.getLastName()).isNull();
        assertThat(request.getPhoneNumber()).isNull();
        assertThat(request.getDateOfBirth()).isNull();
        assertThat(request.getBio()).isNull();
        assertThat(request.getDepartment()).isNull();
        assertThat(request.getJobTitle()).isNull();
        assertThat(request.getLocation()).isNull();
        assertThat(request.getTimezone()).isNull();
        assertThat(request.getLanguage()).isNull();
    }

    @Test
    @DisplayName("All-args constructor creates request with all fields")
    void allArgsConstructor_createsRequestWithAllFields() {
        // Act
        UpdateProfileRequest request = new UpdateProfileRequest(
            "John",
            "Doe",
            "+1234567890",
            LocalDate.of(1990, 1, 1),
            "Test bio",
            "Engineering",
            "Developer",
            "New York",
            "EST",
            "en"
        );

        // Assert
        assertThat(request.getFirstName()).isEqualTo("John");
        assertThat(request.getLastName()).isEqualTo("Doe");
        assertThat(request.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(request.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(request.getBio()).isEqualTo("Test bio");
        assertThat(request.getDepartment()).isEqualTo("Engineering");
        assertThat(request.getJobTitle()).isEqualTo("Developer");
        assertThat(request.getLocation()).isEqualTo("New York");
        assertThat(request.getTimezone()).isEqualTo("EST");
        assertThat(request.getLanguage()).isEqualTo("en");
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        UpdateProfileRequest request = new UpdateProfileRequest();

        // Act
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setPhoneNumber("+9876543210");
        request.setDateOfBirth(LocalDate.of(1995, 5, 5));
        request.setBio("Updated bio");
        request.setDepartment("Sales");
        request.setJobTitle("Manager");
        request.setLocation("London");
        request.setTimezone("GMT");
        request.setLanguage("fr");

        // Assert
        assertThat(request.getFirstName()).isEqualTo("Jane");
        assertThat(request.getLastName()).isEqualTo("Smith");
        assertThat(request.getPhoneNumber()).isEqualTo("+9876543210");
        assertThat(request.getDateOfBirth()).isEqualTo(LocalDate.of(1995, 5, 5));
        assertThat(request.getBio()).isEqualTo("Updated bio");
        assertThat(request.getDepartment()).isEqualTo("Sales");
        assertThat(request.getJobTitle()).isEqualTo("Manager");
        assertThat(request.getLocation()).isEqualTo("London");
        assertThat(request.getTimezone()).isEqualTo("GMT");
        assertThat(request.getLanguage()).isEqualTo("fr");
    }

    @Test
    @DisplayName("Partial update request works")
    void partialUpdate_works() {
        // Arrange
        UpdateProfileRequest request = new UpdateProfileRequest();

        // Act - Only set some fields
        request.setFirstName("John");
        request.setLastName("Doe");

        // Assert - Other fields remain null
        assertThat(request.getFirstName()).isEqualTo("John");
        assertThat(request.getLastName()).isEqualTo("Doe");
        assertThat(request.getPhoneNumber()).isNull();
        assertThat(request.getDateOfBirth()).isNull();
        assertThat(request.getBio()).isNull();
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        // Arrange
        UpdateProfileRequest request1 = new UpdateProfileRequest();
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPhoneNumber("+1234567890");

        UpdateProfileRequest request2 = new UpdateProfileRequest();
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPhoneNumber("+1234567890");

        UpdateProfileRequest request3 = new UpdateProfileRequest();
        request3.setFirstName("Jane");

        // Assert
        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1).isEqualTo(request1);
        assertThat(request1).isNotEqualTo(null);
        assertThat(request1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhoneNumber("+1234567890");
        request.setDepartment("Engineering");

        // Act
        String result = request.toString();

        // Assert
        assertThat(result).contains("John");
        assertThat(result).contains("Doe");
        assertThat(result).contains("+1234567890");
        assertThat(result).contains("Engineering");
    }
}

