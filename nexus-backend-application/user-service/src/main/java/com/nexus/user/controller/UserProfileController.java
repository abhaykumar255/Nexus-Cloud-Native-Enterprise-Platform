package com.nexus.user.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.common.dto.PaginationInfo;
import com.nexus.user.dto.UpdateProfileRequest;
import com.nexus.user.dto.UserProfileDto;
import com.nexus.user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User profile REST controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {
    
    private final UserProfileService userProfileService;
    
    /**
     * Get current user profile (from JWT)
     * GET /api/v1/users/me
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getCurrentUserProfile(
            @RequestHeader("X-User-Id") String userId) {
        log.info("Get current user profile request for userId: {}", userId);
        
        UserProfileDto profile = userProfileService.getUserProfile(userId);
        
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
    
    /**
     * Update current user profile
     * PUT /api/v1/users/me
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateCurrentUserProfile(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody UpdateProfileRequest request) {
        log.info("Update current user profile request for userId: {}", userId);
        
        UserProfileDto profile = userProfileService.updateUserProfile(userId, request);
        
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profile));
    }
    
    /**
     * Get user profile by ID
     * GET /api/v1/users/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileDto>> getUserProfile(@PathVariable String userId) {
        log.info("Get user profile request for userId: {}", userId);
        
        UserProfileDto profile = userProfileService.getUserProfile(userId);
        
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
    
    /**
     * Get all users (paginated)
     * GET /api/v1/users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfileDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.info("Get all users request - page: {}, size: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("ASC") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserProfileDto> users = userProfileService.getAllUsers(pageable);

        return ResponseEntity.ok(ApiResponse.paginated(users.getContent(), PaginationInfo.from(users)));
    }

    /**
     * Search users
     * GET /api/v1/users/search
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserProfileDto>>> searchUsers(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Search users request - query: {}", q);

        Pageable pageable = PageRequest.of(page, size);
        Page<UserProfileDto> users = userProfileService.searchUsers(q, pageable);

        return ResponseEntity.ok(ApiResponse.paginated(users.getContent(), PaginationInfo.from(users)));
    }
    
    /**
     * Delete user profile
     * DELETE /api/v1/users/{userId}
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserProfile(
            @PathVariable String userId,
            @RequestHeader("X-User-Id") String currentUserId,
            @RequestHeader("X-User-Roles") String roles) {
        log.info("Delete user profile request for userId: {}", userId);
        
        // Only allow self-deletion or admin
        if (!userId.equals(currentUserId) && !roles.contains("ROLE_ADMIN")) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error(403, "Insufficient permissions"));
        }
        
        userProfileService.deleteUserProfile(userId);
        
        return ResponseEntity.ok(ApiResponse.success("User profile deleted successfully", null));
    }
}

