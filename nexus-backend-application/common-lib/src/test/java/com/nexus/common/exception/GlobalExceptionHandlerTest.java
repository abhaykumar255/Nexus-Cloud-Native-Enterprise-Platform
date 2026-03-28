package com.nexus.common.exception;

import com.nexus.common.dto.ApiResponse;
import com.nexus.common.dto.ErrorDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for GlobalExceptionHandler
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    @Test
    void handleNexusException_withResourceNotFoundException_shouldReturn404() {
        ResourceNotFoundException exception = new ResourceNotFoundException("User", "123");
        
        ResponseEntity<ApiResponse<Void>> response = handler.handleNexusException(exception, webRequest);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getError()).isNotNull();
        assertThat(response.getBody().getError().getCode()).isEqualTo("RESOURCE_NOT_FOUND");
    }

    @Test
    void handleNexusException_withValidationException_shouldReturn400() {
        ValidationException exception = new ValidationException("Validation failed");
        
        ResponseEntity<ApiResponse<Void>> response = handler.handleNexusException(exception, webRequest);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError().getCode()).isEqualTo("VALIDATION_FAILED");
    }

    @Test
    void handleNexusException_withAuthenticationException_shouldReturn401() {
        AuthenticationException exception = new AuthenticationException("Unauthorized");
        
        ResponseEntity<ApiResponse<Void>> response = handler.handleNexusException(exception, webRequest);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(401);
        assertThat(response.getBody().getError().getCode()).isEqualTo("UNAUTHORIZED");
    }

    @Test
    void handleNexusException_withAuthorizationException_shouldReturn403() {
        AuthorizationException exception = new AuthorizationException("Access denied");
        
        ResponseEntity<ApiResponse<Void>> response = handler.handleNexusException(exception, webRequest);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(403);
        assertThat(response.getBody().getError().getCode()).isEqualTo("ACCESS_DENIED");
    }

    @Test
    void handleValidationException_shouldReturn400WithValidationErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "email", "invalid@", false, null, null, "Invalid email format"));
        bindingResult.addError(new FieldError("target", "name", null, false, null, null, "Name is required"));
        
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        
        ResponseEntity<ApiResponse<Void>> response = handler.handleValidationException(exception, webRequest);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isNotNull();
        assertThat(response.getBody().getError().getCode()).isEqualTo("VALIDATION_FAILED");
        assertThat(response.getBody().getError().getMessage()).isEqualTo("Request validation failed");
        assertThat(response.getBody().getError().getDetails()).isNotNull();
        assertThat(response.getBody().getError().getDetails()).hasSize(2);
    }

    @Test
    void handleGenericException_shouldReturn500() {
        Exception exception = new RuntimeException("Unexpected error");
        
        ResponseEntity<ApiResponse<Void>> response = handler.handleGenericException(exception, webRequest);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getError()).isNotNull();
        assertThat(response.getBody().getError().getCode()).isEqualTo("INTERNAL_ERROR");
        assertThat(response.getBody().getError().getMessage()).isEqualTo("An unexpected error occurred");
    }

    @Test
    void handleGenericException_withNullPointerException_shouldReturn500() {
        NullPointerException exception = new NullPointerException("Null value encountered");
        
        ResponseEntity<ApiResponse<Void>> response = handler.handleGenericException(exception, webRequest);
        
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getError().getCode()).isEqualTo("INTERNAL_ERROR");
    }
}

