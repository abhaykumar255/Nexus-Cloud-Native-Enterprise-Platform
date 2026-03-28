package com.nexus.auth.controller;

import com.nexus.auth.dto.AuthResponse;
import com.nexus.auth.dto.LoginRequest;
import com.nexus.auth.dto.RegisterRequest;
import com.nexus.auth.service.AuthService;
import com.nexus.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication REST controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * Register new user
     * POST /api/v1/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request for email: {}", request.getEmail());
        
        AuthResponse response = authService.register(request);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "User registered successfully", response));
    }
    
    /**
     * Login user
     * POST /api/v1/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for: {}", request.getUsernameOrEmail());
        
        AuthResponse response = authService.login(request);
        
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
    
    /**
     * Refresh access token
     * POST /api/v1/auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
        log.info("Token refresh request");
        
        AuthResponse response = authService.refreshToken(refreshToken);
        
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    }
    
    /**
     * Logout user
     * POST /api/v1/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Refresh-Token") String refreshToken) {
        log.info("Logout request");
        
        authService.logout(refreshToken);
        
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }
    
    /**
     * Validate token (for other services)
     * GET /api/v1/auth/validate
     */
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestHeader("Authorization") String token) {
        // This endpoint can be used by other services to validate tokens
        // The actual validation is done by the gateway, this is just a health check
        return ResponseEntity.ok(ApiResponse.success(true));
    }
}

