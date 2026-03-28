package com.nexus.common.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ApiResponse
 */
class ApiResponseTest {

    @Test
    void success_withData_shouldCreateSuccessResponse() {
        String testData = "test data";
        
        ApiResponse<String> response = ApiResponse.success(testData);
        
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isEqualTo(testData);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void success_withMessageAndData_shouldCreateSuccessResponse() {
        String testData = "test data";
        String message = "Custom success message";
        
        ApiResponse<String> response = ApiResponse.success(message, testData);
        
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isEqualTo(testData);
    }

    @Test
    void success_withStatusMessageAndData_shouldCreateSuccessResponse() {
        String testData = "test data";
        String message = "Created successfully";
        int status = 201;
        
        ApiResponse<String> response = ApiResponse.success(status, message, testData);
        
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isEqualTo(testData);
    }

    @Test
    void error_withMessage_shouldCreateErrorResponse() {
        String errorMessage = "Error occurred";
        
        ApiResponse<Void> response = ApiResponse.error(errorMessage);
        
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getStatus()).isEqualTo(500);
        assertThat(response.getMessage()).isEqualTo(errorMessage);
        assertThat(response.getData()).isNull();
    }

    @Test
    void error_withStatusAndMessage_shouldCreateErrorResponse() {
        String errorMessage = "Not found";
        int status = 404;
        
        ApiResponse<Void> response = ApiResponse.error(status, errorMessage);
        
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    void error_withStatusAndErrorDetails_shouldCreateErrorResponse() {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("TEST_ERROR")
                .message("Test error message")
                .build();
        int status = 400;
        
        ApiResponse<Void> response = ApiResponse.error(status, errorDetails);
        
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getError()).isEqualTo(errorDetails);
        assertThat(response.getError().getCode()).isEqualTo("TEST_ERROR");
    }

    @Test
    void paginated_withDataAndPagination_shouldCreatePaginatedResponse() {
        String testData = "paginated data";
        PaginationInfo paginationInfo = PaginationInfo.builder()
                .page(0)
                .size(10)
                .totalElements(100)
                .totalPages(10)
                .hasNext(true)
                .hasPrevious(false)
                .build();
        
        ApiResponse<String> response = ApiResponse.paginated(testData, paginationInfo);
        
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isEqualTo(testData);
        assertThat(response.getPagination()).isEqualTo(paginationInfo);
        assertThat(response.getPagination().getPage()).isEqualTo(0);
        assertThat(response.getPagination().getTotalElements()).isEqualTo(100);
    }

    @Test
    void builder_shouldSetAllFields() {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("CUSTOM_ERROR")
                .message("Custom error")
                .build();
        
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(false)
                .status(403)
                .message("Forbidden")
                .requestId("req-123")
                .traceId("trace-456")
                .error(errorDetails)
                .build();
        
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getMessage()).isEqualTo("Forbidden");
        assertThat(response.getRequestId()).isEqualTo("req-123");
        assertThat(response.getTraceId()).isEqualTo("trace-456");
        assertThat(response.getError()).isEqualTo(errorDetails);
    }
}

