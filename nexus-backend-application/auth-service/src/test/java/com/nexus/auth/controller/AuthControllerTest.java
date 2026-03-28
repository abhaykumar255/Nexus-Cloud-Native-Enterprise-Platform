package com.nexus.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.auth.dto.AuthResponse;
import com.nexus.auth.dto.LoginRequest;
import com.nexus.auth.dto.RegisterRequest;
import com.nexus.auth.service.AuthService;
import com.nexus.common.exception.AuthenticationException;
import com.nexus.common.exception.DuplicateResourceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)  // Disable Spring Security for tests
@DisplayName("AuthController Unit Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("POST /api/v1/auth/register - valid request -> returns 201")
    void register_validRequest_returns201() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("test@example.com", "testuser", "SecurePass123!");
        AuthResponse response = AuthResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .tokenType("Bearer")
                .expiresIn(3600000L)
                .user(AuthResponse.UserInfo.builder()
                        .id("user-123")
                        .email("test@example.com")
                        .username("testuser")
                        .emailVerified(false)
                        .build())
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"));

        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - duplicate email -> returns 409")
    void register_duplicateEmail_returns409() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("test@example.com", "testuser", "SecurePass123!");
        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new DuplicateResourceException("Email already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - invalid email -> returns 400")
    void register_invalidEmail_returns400() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("invalid-email", "testuser", "SecurePass123!");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any());
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - valid credentials -> returns 200")
    void login_validCredentials_returns200() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("test@example.com", "SecurePass123!");
        AuthResponse response = AuthResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .tokenType("Bearer")
                .expiresIn(3600000L)
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - invalid credentials -> returns 401")
    void login_invalidCredentials_returns401() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("test@example.com", "WrongPassword");
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new AuthenticationException("Invalid credentials"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/refresh - valid token -> returns 200")
    void refresh_validToken_returns200() throws Exception {
        // Arrange
        AuthResponse response = AuthResponse.builder()
                .accessToken("new-access-token")
                .refreshToken("new-refresh-token")
                .tokenType("Bearer")
                .expiresIn(3600000L)
                .build();

        when(authService.refreshToken(anyString())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .header("Refresh-Token", "old-refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("new-access-token"));

        verify(authService).refreshToken("old-refresh-token");
    }

    @Test
    @DisplayName("POST /api/v1/auth/refresh - invalid token -> returns 401")
    void refresh_invalidToken_returns401() throws Exception {
        // Arrange
        when(authService.refreshToken(anyString()))
                .thenThrow(new AuthenticationException("Invalid refresh token"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .header("Refresh-Token", "invalid-token"))
                .andExpect(status().isUnauthorized());

        verify(authService).refreshToken("invalid-token");
    }

    @Test
    @DisplayName("POST /api/v1/auth/logout - valid token -> returns 200")
    void logout_validToken_returns200() throws Exception {
        // Arrange
        doNothing().when(authService).logout(anyString());

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Refresh-Token", "refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logout successful"));

        verify(authService).logout("refresh-token");
    }

    @Test
    @DisplayName("GET /api/v1/auth/validate - returns 200")
    void validateToken_returns200() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/auth/validate")
                        .header("Authorization", "Bearer access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));

        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - missing email -> returns 400")
    void register_missingEmail_returns400() throws Exception {
        // Arrange - Create request with null email
        String invalidRequest = "{\"username\":\"testuser\",\"password\":\"SecurePass123!\"}";

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any());
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - missing password -> returns 400")
    void login_missingPassword_returns400() throws Exception {
        // Arrange - Create request with null password
        String invalidRequest = "{\"usernameOrEmail\":\"test@example.com\"}";

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any());
    }
}

