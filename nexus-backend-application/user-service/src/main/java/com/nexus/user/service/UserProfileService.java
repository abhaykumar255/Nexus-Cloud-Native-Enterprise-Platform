package com.nexus.user.service;

import com.nexus.common.exception.DuplicateResourceException;
import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.user.domain.entity.UserProfile;
import com.nexus.user.domain.repository.UserProfileRepository;
import com.nexus.user.dto.UpdateProfileRequest;
import com.nexus.user.dto.UserProfileDto;
import com.nexus.user.event.UserEvent;
import com.nexus.user.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;

/**
 * User profile service with CRUD operations and event publishing
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {
    
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    
    private static final String USER_CREATED_TOPIC = "user.created";
    private static final String USER_UPDATED_TOPIC = "user.updated";
    private static final String USER_DELETED_TOPIC = "user.deleted";
    
    /**
     * Create user profile (called when user registers in Auth Service)
     */
    @Transactional
    public UserProfileDto createUserProfile(String userId, String email, String username) {
        log.info("Creating user profile for userId: {}", userId);
        
        if (userProfileRepository.existsByUserId(userId)) {
            throw new DuplicateResourceException("User profile already exists for userId: " + userId);
        }
        
        UserProfile userProfile = UserProfile.builder()
                .userId(userId)
                .email(email)
                .username(username)
                .profileComplete(false)
                .roles(new HashSet<>())
                .build();
        
        userProfile.getRoles().add("ROLE_USER"); // Default role
        
        userProfile = userProfileRepository.save(userProfile);
        
        // Publish user created event
        publishUserEvent(UserEvent.EventType.USER_CREATED, userProfile);
        
        log.info("User profile created successfully: {}", userProfile.getId());
        
        return userProfileMapper.toDto(userProfile);
    }
    
    /**
     * Get user profile by user ID (with caching)
     */
    @Cacheable(value = "userProfiles", key = "#userId")
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(String userId) {
        log.debug("Fetching user profile for userId: {}", userId);
        
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", userId));
        
        return userProfileMapper.toDto(userProfile);
    }
    
    /**
     * Update user profile
     */
    @CacheEvict(value = "userProfiles", key = "#userId")
    @Transactional
    public UserProfileDto updateUserProfile(String userId, UpdateProfileRequest request) {
        log.info("Updating user profile for userId: {}", userId);
        
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", userId));
        
        // Update fields
        if (request.getFirstName() != null) userProfile.setFirstName(request.getFirstName());
        if (request.getLastName() != null) userProfile.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) userProfile.setPhoneNumber(request.getPhoneNumber());
        if (request.getDateOfBirth() != null) userProfile.setDateOfBirth(request.getDateOfBirth());
        if (request.getBio() != null) userProfile.setBio(request.getBio());
        if (request.getDepartment() != null) userProfile.setDepartment(request.getDepartment());
        if (request.getJobTitle() != null) userProfile.setJobTitle(request.getJobTitle());
        if (request.getLocation() != null) userProfile.setLocation(request.getLocation());
        if (request.getTimezone() != null) userProfile.setTimezone(request.getTimezone());
        if (request.getLanguage() != null) userProfile.setLanguage(request.getLanguage());
        
        // Check if profile is complete
        checkProfileCompletion(userProfile);
        
        userProfile = userProfileRepository.save(userProfile);
        
        // Publish user updated event
        publishUserEvent(UserEvent.EventType.USER_UPDATED, userProfile);
        
        log.info("User profile updated successfully: {}", userProfile.getId());
        
        return userProfileMapper.toDto(userProfile);
    }
    
    /**
     * Delete user profile
     */
    @CacheEvict(value = "userProfiles", key = "#userId")
    @Transactional
    public void deleteUserProfile(String userId) {
        log.info("Deleting user profile for userId: {}", userId);
        
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", userId));
        
        userProfileRepository.delete(userProfile);
        
        // Publish user deleted event
        publishUserEvent(UserEvent.EventType.USER_DELETED, userProfile);
        
        log.info("User profile deleted successfully: {}", userProfile.getId());
    }
    
    /**
     * Search users
     */
    @Transactional(readOnly = true)
    public Page<UserProfileDto> searchUsers(String search, Pageable pageable) {
        log.debug("Searching users with query: {}", search);
        
        Page<UserProfile> userProfiles = userProfileRepository.searchUsers(search, pageable);
        
        return userProfiles.map(userProfileMapper::toDto);
    }
    
    /**
     * Get all users (paginated)
     */
    @Transactional(readOnly = true)
    public Page<UserProfileDto> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users");
        
        return userProfileRepository.findAll(pageable).map(userProfileMapper::toDto);
    }
    
    /**
     * Check if profile is complete
     */
    private void checkProfileCompletion(UserProfile userProfile) {
        boolean isComplete = userProfile.getFirstName() != null &&
                             userProfile.getLastName() != null &&
                             userProfile.getPhoneNumber() != null &&
                             userProfile.getDateOfBirth() != null;
        
        if (isComplete && !userProfile.isProfileComplete()) {
            userProfile.setProfileComplete(true);
            publishUserEvent(UserEvent.EventType.PROFILE_COMPLETED, userProfile);
        }
    }
    
    /**
     * Publish user event to Kafka
     */
    private void publishUserEvent(UserEvent.EventType eventType, UserProfile userProfile) {
        UserEvent event = UserEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .userId(userProfile.getUserId())
                .email(userProfile.getEmail())
                .username(userProfile.getUsername())
                .timestamp(Instant.now())
                .build();
        
        String topic = switch (eventType) {
            case USER_CREATED, PROFILE_COMPLETED -> USER_CREATED_TOPIC;
            case USER_UPDATED -> USER_UPDATED_TOPIC;
            case USER_DELETED -> USER_DELETED_TOPIC;
        };
        
        kafkaTemplate.send(topic, userProfile.getUserId(), event);
        log.info("Published {} event for user: {}", eventType, userProfile.getUserId());
    }
}

