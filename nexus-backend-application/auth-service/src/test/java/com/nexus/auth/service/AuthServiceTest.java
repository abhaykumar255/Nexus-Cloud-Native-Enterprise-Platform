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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;
    private User testUser;
    private RefreshToken testRefreshToken;

    @BeforeEach
    void setUp() {
        validRegisterRequest = new RegisterRequest(
                "test@example.com",
                "testuser",
                "SecurePass123!"
        );

        validLoginRequest = new LoginRequest(
                "test@example.com",
                "SecurePass123!"
        );

        testUser = User.builder()
                .id("user-123")
                .email("test@example.com")
                .username("testuser")
                .passwordHash("$2a$12$hashedpassword")
                .authProvider(User.AuthProvider.LOCAL)
                .emailVerified(false)
                .active(true)
                .locked(false)
                .failedLoginAttempts(0)
                .build();

        testRefreshToken = RefreshToken.builder()
                .id("token-123")
                .token("refresh-token-xyz")
                .userId("user-123")
                .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
                .revoked(false)
                .build();
    }

    @Test
    @DisplayName("register: valid request -> creates user successfully")
    void register_validRequest_success() {
        // Arrange
        when(userRepository.existsByEmail(validRegisterRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(validRegisterRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(validRegisterRequest.getPassword())).thenReturn("$2a$12$hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateAccessToken(anyString(), anyString(), anyList())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");
        when(jwtService.getExpirationMs()).thenReturn(3600000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

        // Act
        AuthResponse response = authService.register(validRegisterRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getUser()).isNotNull();
        assertThat(response.getUser().getEmail()).isEqualTo("test@example.com");
        assertThat(response.getUser().getUsername()).isEqualTo("testuser");

        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository).existsByUsername("testuser");
        verify(passwordEncoder).encode("SecurePass123!");
        verify(userRepository).save(any(User.class));
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("register: duplicate email -> throws DuplicateResourceException")
    void register_duplicateEmail_throwsException() {
        // Arrange
        when(userRepository.existsByEmail(validRegisterRequest.getEmail())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.register(validRegisterRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email");

        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("register: duplicate username -> throws DuplicateResourceException")
    void register_duplicateUsername_throwsException() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(validRegisterRequest.getUsername())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.register(validRegisterRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("username");

        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("register: weak password -> throws ValidationException")
    void register_weakPassword_throwsException() {
        // Arrange
        RegisterRequest weakPasswordRequest = new RegisterRequest(
                "test@example.com",
                "testuser",
                "weak"
        );
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> authService.register(weakPasswordRequest))
                .isInstanceOf(ValidationException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("login: valid credentials -> returns auth response")
    void login_validCredentials_success() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("SecurePass123!", testUser.getPasswordHash())).thenReturn(true);
        when(jwtService.generateAccessToken(anyString(), anyString(), anyList())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");
        when(jwtService.getExpirationMs()).thenReturn(3600000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        AuthResponse response = authService.login(validLoginRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");

        verify(passwordEncoder).matches("SecurePass123!", testUser.getPasswordHash());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("login: user not found -> throws AuthenticationException")
    void login_userNotFound_throwsException() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.login(validLoginRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Invalid credentials");

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("login: invalid password -> throws AuthenticationException")
    void login_invalidPassword_throwsException() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act & Assert
        assertThatThrownBy(() -> authService.login(validLoginRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Invalid credentials");

        verify(passwordEncoder).matches("SecurePass123!", testUser.getPasswordHash());
    }

    @Test
    @DisplayName("login: locked account -> throws AuthenticationException")
    void login_lockedAccount_throwsException() {
        // Arrange
        User lockedUser = User.builder()
                .id("user-123")
                .email("test@example.com")
                .username("testuser")
                .passwordHash("$2a$12$hashedpassword")
                .locked(true)
                .lockedUntil(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(lockedUser));

        // Act & Assert
        assertThatThrownBy(() -> authService.login(validLoginRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("locked");

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("login: inactive account -> throws AuthenticationException")
    void login_inactiveAccount_throwsException() {
        // Arrange
        User inactiveUser = User.builder()
                .id("user-123")
                .email("test@example.com")
                .username("testuser")
                .passwordHash("$2a$12$hashedpassword")
                .active(false)
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(inactiveUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.login(validLoginRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("inactive");
    }

    @Test
    @DisplayName("refreshToken: valid token -> returns new auth response")
    void refreshToken_validToken_success() {
        // Arrange
        when(refreshTokenRepository.findByToken("refresh-token-xyz")).thenReturn(Optional.of(testRefreshToken));
        when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
        when(jwtService.generateAccessToken(anyString(), anyString(), anyList())).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("new-refresh-token");
        when(jwtService.getExpirationMs()).thenReturn(3600000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

        // Act
        AuthResponse response = authService.refreshToken("refresh-token-xyz");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("new-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("new-refresh-token");

        verify(refreshTokenRepository).findByToken("refresh-token-xyz");
        verify(userRepository).findById("user-123");
    }

    @Test
    @DisplayName("refreshToken: invalid token -> throws AuthenticationException")
    void refreshToken_invalidToken_throwsException() {
        // Arrange
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.refreshToken("invalid-token"))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Invalid refresh token");

        verify(userRepository, never()).findById(anyString());
    }

    @Test
    @DisplayName("refreshToken: revoked token -> throws AuthenticationException")
    void refreshToken_revokedToken_throwsException() {
        // Arrange
        RefreshToken revokedToken = RefreshToken.builder()
                .id("token-123")
                .token("revoked-token")
                .userId("user-123")
                .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
                .revoked(true)
                .revokedAt(Instant.now())
                .build();

        when(refreshTokenRepository.findByToken("revoked-token")).thenReturn(Optional.of(revokedToken));

        // Act & Assert
        assertThatThrownBy(() -> authService.refreshToken("revoked-token"))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("revoked");

        verify(userRepository, never()).findById(anyString());
    }

    @Test
    @DisplayName("refreshToken: expired token -> throws AuthenticationException")
    void refreshToken_expiredToken_throwsException() {
        // Arrange
        RefreshToken expiredToken = RefreshToken.builder()
                .id("token-123")
                .token("expired-token")
                .userId("user-123")
                .expiresAt(Instant.now().minus(1, ChronoUnit.DAYS))
                .revoked(false)
                .build();

        when(refreshTokenRepository.findByToken("expired-token")).thenReturn(Optional.of(expiredToken));

        // Act & Assert
        assertThatThrownBy(() -> authService.refreshToken("expired-token"))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("expired");

        verify(userRepository, never()).findById(anyString());
    }

    @Test
    @DisplayName("refreshToken: inactive user -> throws AuthenticationException")
    void refreshToken_inactiveUser_throwsException() {
        // Arrange
        User inactiveUser = User.builder()
                .id("user-123")
                .email("test@example.com")
                .username("testuser")
                .active(false)
                .build();

        when(refreshTokenRepository.findByToken("refresh-token-xyz")).thenReturn(Optional.of(testRefreshToken));
        when(userRepository.findById("user-123")).thenReturn(Optional.of(inactiveUser));

        // Act & Assert
        assertThatThrownBy(() -> authService.refreshToken("refresh-token-xyz"))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("inactive");
    }

    @Test
    @DisplayName("logout: valid token -> revokes token")
    void logout_validToken_success() {
        // Arrange
        when(refreshTokenRepository.findByToken("refresh-token-xyz")).thenReturn(Optional.of(testRefreshToken));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testRefreshToken);

        // Act
        authService.logout("refresh-token-xyz");

        // Assert
        verify(refreshTokenRepository).findByToken("refresh-token-xyz");
        verify(refreshTokenRepository).save(argThat(token ->
            token.isRevoked() && token.getRevokedAt() != null
        ));
    }

    @Test
    @DisplayName("logout: token not found -> does nothing")
    void logout_tokenNotFound_doesNothing() {
        // Arrange
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        // Act
        authService.logout("invalid-token");

        // Assert
        verify(refreshTokenRepository).findByToken("invalid-token");
        verify(refreshTokenRepository, never()).save(any());
    }
}
