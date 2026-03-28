package com.nexus.user.service;

import com.nexus.common.exception.DuplicateResourceException;
import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.user.domain.entity.UserProfile;
import com.nexus.user.domain.repository.UserProfileRepository;
import com.nexus.user.dto.UpdateProfileRequest;
import com.nexus.user.dto.UserProfileDto;
import com.nexus.user.event.UserEvent;
import com.nexus.user.mapper.UserProfileMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserProfileService Unit Tests")
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserProfileMapper userProfileMapper;

    @Mock
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @InjectMocks
    private UserProfileService userProfileService;

    private UserProfile testUserProfile;
    private UserProfileDto testUserProfileDto;
    private UpdateProfileRequest updateRequest;

    @BeforeEach
    void setUp() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        testUserProfile = UserProfile.builder()
            .id("profile-123")
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .firstName("John")
            .lastName("Doe")
            .phoneNumber("+1234567890")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .bio("Test bio")
            .department("Engineering")
            .jobTitle("Developer")
            .location("New York")
            .timezone("EST")
            .language("en")
            .profileComplete(true)
            .roles(roles)
            .build();

        testUserProfileDto = UserProfileDto.builder()
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

    // ========== CREATE USER PROFILE TESTS ==========

    @Test
    @DisplayName("Create user profile - Success")
    void createUserProfile_success() {
        // Arrange
        when(userProfileRepository.existsByUserId(anyString())).thenReturn(false);
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUserProfile);
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        UserProfileDto result = userProfileService.createUserProfile("user-123", "test@example.com", "testuser");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo("user-123");
        assertThat(result.getEmail()).isEqualTo("test@example.com");

        verify(userProfileRepository).existsByUserId("user-123");
        verify(userProfileRepository).save(any(UserProfile.class));
        verify(kafkaTemplate).send(eq("user.created"), eq("user-123"), any(UserEvent.class));
    }

    @Test
    @DisplayName("Create user profile - Duplicate should throw exception")
    void createUserProfile_duplicate_throwsException() {
        // Arrange
        when(userProfileRepository.existsByUserId(anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userProfileService.createUserProfile("user-123", "test@example.com", "testuser"))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessageContaining("already exists");

        verify(userProfileRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("Create user profile - Default role is added")
    void createUserProfile_addsDefaultRole() {
        // Arrange
        when(userProfileRepository.existsByUserId(anyString())).thenReturn(false);
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUserProfile);
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        userProfileService.createUserProfile("user-123", "test@example.com", "testuser");

        // Assert
        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(captor.capture());
        UserProfile savedProfile = captor.getValue();
        assertThat(savedProfile.getRoles()).contains("ROLE_USER");
    }

    // ========== GET USER PROFILE TESTS ==========

    @Test
    @DisplayName("Get user profile - Success")
    void getUserProfile_success() {
        // Arrange
        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.of(testUserProfile));
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        UserProfileDto result = userProfileService.getUserProfile("user-123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo("user-123");

        verify(userProfileRepository).findByUserId("user-123");
        verify(userProfileMapper).toDto(testUserProfile);
    }

    @Test
    @DisplayName("Get user profile - Not found throws exception")
    void getUserProfile_notFound_throwsException() {
        // Arrange
        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userProfileService.getUserProfile("user-123"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("UserProfile");
    }

    // ========== UPDATE USER PROFILE TESTS ==========

    @Test
    @DisplayName("Update user profile - Success")
    void updateUserProfile_success() {
        // Arrange
        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.of(testUserProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUserProfile);
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        UserProfileDto result = userProfileService.updateUserProfile("user-123", updateRequest);

        // Assert
        assertThat(result).isNotNull();

        verify(userProfileRepository).findByUserId("user-123");
        verify(userProfileRepository).save(any(UserProfile.class));
        verify(kafkaTemplate).send(eq("user.updated"), eq("user-123"), any(UserEvent.class));
    }

    @Test
    @DisplayName("Update user profile - Not found throws exception")
    void updateUserProfile_notFound_throwsException() {
        // Arrange
        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userProfileService.updateUserProfile("user-123", updateRequest))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(userProfileRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update user profile - Profile completion triggers event")
    void updateUserProfile_profileCompletion_triggersEvent() {
        // Arrange
        UserProfile incompleteProfile = UserProfile.builder()
            .id("profile-123")
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .profileComplete(false)
            .roles(new HashSet<>(Set.of("ROLE_USER")))
            .build();

        UpdateProfileRequest completeRequest = new UpdateProfileRequest();
        completeRequest.setFirstName("John");
        completeRequest.setLastName("Doe");
        completeRequest.setPhoneNumber("+1234567890");
        completeRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.of(incompleteProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(incompleteProfile);
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        userProfileService.updateUserProfile("user-123", completeRequest);

        // Assert - Should send 2 events: USER_UPDATED and PROFILE_COMPLETED
        verify(kafkaTemplate, times(2)).send(anyString(), eq("user-123"), any(UserEvent.class));
    }

    // ========== DELETE USER PROFILE TESTS ==========

    @Test
    @DisplayName("Delete user profile - Success")
    void deleteUserProfile_success() {
        // Arrange
        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.of(testUserProfile));

        // Act
        userProfileService.deleteUserProfile("user-123");

        // Assert
        verify(userProfileRepository).delete(testUserProfile);
        verify(kafkaTemplate).send(eq("user.deleted"), eq("user-123"), any(UserEvent.class));
    }

    @Test
    @DisplayName("Delete user profile - Not found throws exception")
    void deleteUserProfile_notFound_throwsException() {
        // Arrange
        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userProfileService.deleteUserProfile("user-123"))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(userProfileRepository, never()).delete(any());
    }

    // ========== SEARCH AND GET ALL TESTS ==========

    @Test
    @DisplayName("Search users - Returns results")
    void searchUsers_returnsResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserProfile> profilePage = new PageImpl<>(List.of(testUserProfile));
        when(userProfileRepository.searchUsers(anyString(), any(Pageable.class))).thenReturn(profilePage);
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        Page<UserProfileDto> result = userProfileService.searchUsers("test", pageable);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);

        verify(userProfileRepository).searchUsers("test", pageable);
    }

    @Test
    @DisplayName("Get all users - Returns paginated results")
    void getAllUsers_returnsPaginatedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserProfile> profilePage = new PageImpl<>(List.of(testUserProfile));
        when(userProfileRepository.findAll(any(Pageable.class))).thenReturn(profilePage);
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        Page<UserProfileDto> result = userProfileService.getAllUsers(pageable);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);

        verify(userProfileRepository).findAll(pageable);
    }

    // ========== UPDATE SPECIFIC FIELDS TESTS ==========

    @Test
    @DisplayName("Update profile - Bio field is updated")
    void updateProfile_bioField_isUpdated() {
        // Arrange
        UserProfile profile = UserProfile.builder()
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .roles(new HashSet<>())
            .build();

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setBio("New bio content");

        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.of(profile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(profile);
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        userProfileService.updateUserProfile("user-123", request);

        // Assert
        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(captor.capture());
        assertThat(captor.getValue().getBio()).isEqualTo("New bio content");
    }

    @Test
    @DisplayName("Update profile - Department field is updated")
    void updateProfile_departmentField_isUpdated() {
        // Arrange
        UserProfile profile = UserProfile.builder()
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .roles(new HashSet<>())
            .build();

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setDepartment("Engineering");

        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.of(profile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(profile);
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        userProfileService.updateUserProfile("user-123", request);

        // Assert
        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(captor.capture());
        assertThat(captor.getValue().getDepartment()).isEqualTo("Engineering");
    }

    @Test
    @DisplayName("Update profile - JobTitle field is updated")
    void updateProfile_jobTitleField_isUpdated() {
        // Arrange
        UserProfile profile = UserProfile.builder()
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .roles(new HashSet<>())
            .build();

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setJobTitle("Senior Developer");

        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.of(profile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(profile);
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        userProfileService.updateUserProfile("user-123", request);

        // Assert
        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(captor.capture());
        assertThat(captor.getValue().getJobTitle()).isEqualTo("Senior Developer");
    }

    @Test
    @DisplayName("Update profile - Location field is updated")
    void updateProfile_locationField_isUpdated() {
        // Arrange
        UserProfile profile = UserProfile.builder()
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .roles(new HashSet<>())
            .build();

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setLocation("San Francisco");

        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.of(profile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(profile);
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        userProfileService.updateUserProfile("user-123", request);

        // Assert
        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(captor.capture());
        assertThat(captor.getValue().getLocation()).isEqualTo("San Francisco");
    }

    @Test
    @DisplayName("Update profile - Timezone field is updated")
    void updateProfile_timezoneField_isUpdated() {
        // Arrange
        UserProfile profile = UserProfile.builder()
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .roles(new HashSet<>())
            .build();

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setTimezone("PST");

        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.of(profile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(profile);
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        userProfileService.updateUserProfile("user-123", request);

        // Assert
        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(captor.capture());
        assertThat(captor.getValue().getTimezone()).isEqualTo("PST");
    }

    @Test
    @DisplayName("Update profile - Language field is updated")
    void updateProfile_languageField_isUpdated() {
        // Arrange
        UserProfile profile = UserProfile.builder()
            .userId("user-123")
            .email("test@example.com")
            .username("testuser")
            .roles(new HashSet<>())
            .build();

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setLanguage("es");

        when(userProfileRepository.findByUserId(anyString())).thenReturn(Optional.of(profile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(profile);
        when(userProfileMapper.toDto(any(UserProfile.class))).thenReturn(testUserProfileDto);

        // Act
        userProfileService.updateUserProfile("user-123", request);

        // Assert
        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(captor.capture());
        assertThat(captor.getValue().getLanguage()).isEqualTo("es");
    }
}

