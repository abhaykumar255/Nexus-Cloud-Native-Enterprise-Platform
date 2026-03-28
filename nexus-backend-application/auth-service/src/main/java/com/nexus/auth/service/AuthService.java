package com.nexus.auth.service;

import com.nexus.auth.domain.entity.RefreshToken;
import com.nexus.auth.domain.entity.User;
import com.nexus.auth.domain.repository.RefreshTokenRepository;
import com.nexus.auth.domain.repository.UserRepository;
import com.nexus.auth.dto.AuthResponse;
import com.nexus.auth.dto.LoginRequest;
import com.nexus.auth.dto.RegisterRequest;
import com.nexus.common.exception.AuthenticationException;
import com.nexus.common.exception.DuplicateResourceException;
import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Authentication service for user registration, login, and token management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    /**
     * Register new user
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getEmail());
        
        // Validate uniqueness
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }
        
        // Validate password strength
        validatePasswordStrength(request.getPassword());
        
        // Create user
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .authProvider(User.AuthProvider.LOCAL)
                .emailVerified(false)
                .active(true)
                .locked(false)
                .build();
        
        user = userRepository.save(user);
        
        log.info("User registered successfully: {}", user.getId());
        
        // Generate tokens
        return generateAuthResponse(user);
    }
    
    /**
     * Login user
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("User login attempt: {}", request.getUsernameOrEmail());
        
        // Find user by email or username
        User user = userRepository.findByEmail(request.getUsernameOrEmail())
                .or(() -> userRepository.findByUsername(request.getUsernameOrEmail()))
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));
        
        // Check if account is locked
        if (user.isLocked() && user.getLockedUntil() != null && user.getLockedUntil().isAfter(Instant.now())) {
            throw new AuthenticationException("Account is locked. Please try again later.");
        }
        
        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            handleFailedLogin(user);
            throw new AuthenticationException("Invalid credentials");
        }
        
        // Check if account is active
        if (!user.isActive()) {
            throw new AuthenticationException("Account is inactive");
        }
        
        // Reset failed login attempts
        user.setFailedLoginAttempts(0);
        user.setLocked(false);
        user.setLockedUntil(null);
        user.setLastLoginAt(Instant.now());
        userRepository.save(user);
        
        log.info("User logged in successfully: {}", user.getId());

        // Generate tokens
        return generateAuthResponse(user);
    }

    /**
     * Refresh access token
     */
    @Transactional
    public AuthResponse refreshToken(String refreshTokenStr) {
        log.info("Refreshing access token");

        // Find and validate refresh token
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new AuthenticationException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            throw new AuthenticationException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new AuthenticationException("Refresh token has expired");
        }

        // Get user
        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", refreshToken.getUserId()));

        if (!user.isActive()) {
            throw new AuthenticationException("Account is inactive");
        }

        // Generate new tokens
        return generateAuthResponse(user);
    }

    /**
     * Logout user (revoke refresh token)
     */
    @Transactional
    public void logout(String refreshTokenStr) {
        log.info("User logout");

        refreshTokenRepository.findByToken(refreshTokenStr).ifPresent(token -> {
            token.setRevoked(true);
            token.setRevokedAt(Instant.now());
            refreshTokenRepository.save(token);
        });
    }

    /**
     * Generate authentication response with tokens
     */
    private AuthResponse generateAuthResponse(User user) {
        // For now, use default USER role. In production, fetch from User Service
        List<String> roles = List.of("ROLE_USER");

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(), roles);
        String refreshTokenStr = jwtService.generateRefreshToken(user.getId());

        // Save refresh token
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenStr)
                .userId(user.getId())
                .expiresAt(Instant.now().plusMillis(604800000)) // 7 days
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationMs() / 1000) // in seconds
                .user(AuthResponse.UserInfo.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .emailVerified(user.isEmailVerified())
                        .build())
                .build();
    }

    /**
     * Handle failed login attempt
     */
    private void handleFailedLogin(User user) {
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        // Lock account after 5 failed attempts
        if (attempts >= 5) {
            user.setLocked(true);
            user.setLockedUntil(Instant.now().plusSeconds(900)); // Lock for 15 minutes
            log.warn("Account locked due to failed login attempts: {}", user.getEmail());
        }

        userRepository.save(user);
    }

    /**
     * Validate password strength
     */
    private void validatePasswordStrength(String password) {
        if (password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new ValidationException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new ValidationException("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new ValidationException("Password must contain at least one digit");
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            throw new ValidationException("Password must contain at least one special character");
        }
    }
}

