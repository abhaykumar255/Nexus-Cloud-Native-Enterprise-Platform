package com.nexus.user.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.user.dto.UpdateProfileRequest;
import com.nexus.user.dto.UserProfileDto;
import com.nexus.user.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserProfileController Unit Tests")
class UserProfileControllerTest {

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private UserProfileController controller;

    private UserProfileDto testUserProfile;
    private UpdateProfileRequest updateRequest;

    @BeforeEach
    void setUp() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        testUserProfile = UserProfileDto.builder()
            .id("profile-123")
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .firstName("John")
            .lastName("Doe")
            .phoneNumber("+1234567890")
            .profileComplete(true)
            .roles(roles)
            .build();

        updateRequest = new UpdateProfileRequest();
        updateRequest.setFirstName("Jane");
        updateRequest.setLastName("Smith");
    }

    @Test
    @DisplayName("Get current user profile - Success")
    void getCurrentUserProfile_success() {
        // Arrange
        when(userProfileService.getUserProfile(anyString())).thenReturn(testUserProfile);

        // Act
        ResponseEntity<ApiResponse<UserProfileDto>> response = controller.getCurrentUserProfile("user-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isEqualTo(testUserProfile);

        verify(userProfileService).getUserProfile("user-123");
    }

    @Test
    @DisplayName("Update current user profile - Success")
    void updateCurrentUserProfile_success() {
        // Arrange
        when(userProfileService.updateUserProfile(anyString(), any(UpdateProfileRequest.class)))
            .thenReturn(testUserProfile);

        // Act
        ResponseEntity<ApiResponse<UserProfileDto>> response = controller.updateCurrentUserProfile("user-123", updateRequest);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isEqualTo(testUserProfile);

        verify(userProfileService).updateUserProfile("user-123", updateRequest);
    }

    @Test
    @DisplayName("Get user profile by ID - Success")
    void getUserProfile_success() {
        // Arrange
        when(userProfileService.getUserProfile(anyString())).thenReturn(testUserProfile);

        // Act
        ResponseEntity<ApiResponse<UserProfileDto>> response = controller.getUserProfile("user-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isEqualTo(testUserProfile);
    }

    @Test
    @DisplayName("Get all users - Success")
    void getAllUsers_success() {
        // Arrange
        Page<UserProfileDto> page = new PageImpl<>(List.of(testUserProfile));
        when(userProfileService.getAllUsers(any(Pageable.class))).thenReturn(page);

        // Act
        ResponseEntity<ApiResponse<List<UserProfileDto>>> response = controller.getAllUsers(0, 20, "createdAt", "DESC");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(1);

        verify(userProfileService).getAllUsers(any(Pageable.class));
    }

    @Test
    @DisplayName("Search users - Success")
    void searchUsers_success() {
        // Arrange
        Page<UserProfileDto> page = new PageImpl<>(List.of(testUserProfile));
        when(userProfileService.searchUsers(anyString(), any(Pageable.class))).thenReturn(page);

        // Act
        ResponseEntity<ApiResponse<List<UserProfileDto>>> response = controller.searchUsers("test", 0, 20);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(1);

        verify(userProfileService).searchUsers(eq("test"), any(Pageable.class));
    }

    @Test
    @DisplayName("Delete user profile - Self deletion allowed")
    void deleteUserProfile_selfDeletion() {
        // Arrange
        doNothing().when(userProfileService).deleteUserProfile(anyString());

        // Act
        ResponseEntity<ApiResponse<Void>> response = controller.deleteUserProfile("user-123", "user-123", "ROLE_USER");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        verify(userProfileService).deleteUserProfile("user-123");
    }

    @Test
    @DisplayName("Delete user profile - Admin can delete")
    void deleteUserProfile_adminDeletion() {
        // Arrange
        doNothing().when(userProfileService).deleteUserProfile(anyString());

        // Act
        ResponseEntity<ApiResponse<Void>> response = controller.deleteUserProfile("user-456", "user-123", "ROLE_ADMIN");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        verify(userProfileService).deleteUserProfile("user-456");
    }

    @Test
    @DisplayName("Delete user profile - Insufficient permissions")
    void deleteUserProfile_insufficientPermissions() {
        // Act
        ResponseEntity<ApiResponse<Void>> response = controller.deleteUserProfile("user-456", "user-123", "ROLE_USER");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(403);

        verify(userProfileService, never()).deleteUserProfile(anyString());
    }
}

