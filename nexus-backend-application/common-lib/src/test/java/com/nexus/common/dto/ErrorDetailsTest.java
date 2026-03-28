package com.nexus.common.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ErrorDetails
 */
class ErrorDetailsTest {

    @Test
    void builder_shouldCreateErrorDetailsWithAllFields() {
        ErrorDetails.ValidationError validationError = ErrorDetails.ValidationError.builder()
                .field("email")
                .rejected("invalid-email")
                .message("Email format is invalid")
                .build();
        
        List<ErrorDetails.ValidationError> validationErrors = Arrays.asList(validationError);
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("VALIDATION_ERROR")
                .message("Validation failed")
                .details(validationErrors)
                .build();
        
        assertThat(errorDetails).isNotNull();
        assertThat(errorDetails.getCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(errorDetails.getMessage()).isEqualTo("Validation failed");
        assertThat(errorDetails.getDetails()).hasSize(1);
        assertThat(errorDetails.getDetails().get(0).getField()).isEqualTo("email");
        assertThat(errorDetails.getDetails().get(0).getRejected()).isEqualTo("invalid-email");
        assertThat(errorDetails.getDetails().get(0).getMessage()).isEqualTo("Email format is invalid");
    }

    @Test
    void builder_withoutDetails_shouldCreateErrorDetails() {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("GENERAL_ERROR")
                .message("An error occurred")
                .build();
        
        assertThat(errorDetails).isNotNull();
        assertThat(errorDetails.getCode()).isEqualTo("GENERAL_ERROR");
        assertThat(errorDetails.getMessage()).isEqualTo("An error occurred");
        assertThat(errorDetails.getDetails()).isNull();
    }

    @Test
    void validationError_shouldCreateWithAllFields() {
        ErrorDetails.ValidationError validationError = ErrorDetails.ValidationError.builder()
                .field("username")
                .rejected(null)
                .message("Username is required")
                .build();
        
        assertThat(validationError).isNotNull();
        assertThat(validationError.getField()).isEqualTo("username");
        assertThat(validationError.getRejected()).isNull();
        assertThat(validationError.getMessage()).isEqualTo("Username is required");
    }

    @Test
    void validationError_withMultipleErrors_shouldCreateList() {
        ErrorDetails.ValidationError error1 = ErrorDetails.ValidationError.builder()
                .field("password")
                .rejected("123")
                .message("Password too short")
                .build();
        
        ErrorDetails.ValidationError error2 = ErrorDetails.ValidationError.builder()
                .field("email")
                .rejected("not-an-email")
                .message("Invalid email format")
                .build();
        
        List<ErrorDetails.ValidationError> errors = Arrays.asList(error1, error2);
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("VALIDATION_FAILED")
                .message("Multiple validation errors")
                .details(errors)
                .build();
        
        assertThat(errorDetails.getDetails()).hasSize(2);
        assertThat(errorDetails.getDetails()).contains(error1, error2);
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyErrorDetails() {
        ErrorDetails errorDetails = new ErrorDetails();
        
        assertThat(errorDetails).isNotNull();
        assertThat(errorDetails.getCode()).isNull();
        assertThat(errorDetails.getMessage()).isNull();
        assertThat(errorDetails.getDetails()).isNull();
    }

    @Test
    void allArgsConstructor_shouldCreateErrorDetailsWithAllFields() {
        ErrorDetails.ValidationError validationError = new ErrorDetails.ValidationError("field1", "rejected1", "message1");
        List<ErrorDetails.ValidationError> details = Arrays.asList(validationError);
        
        ErrorDetails errorDetails = new ErrorDetails("CODE1", "Message1", details);
        
        assertThat(errorDetails.getCode()).isEqualTo("CODE1");
        assertThat(errorDetails.getMessage()).isEqualTo("Message1");
        assertThat(errorDetails.getDetails()).hasSize(1);
    }
}

