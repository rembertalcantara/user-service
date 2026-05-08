package pt.com.aubay.userservice.controller.advice;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pt.com.aubay.userservice.model.dto.ApiErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleNotFound() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleNotFound(HttpClientErrorException.NotFound.create(HttpStatus.NOT_FOUND, null, null, null, null));

        assertResponse(response, 404, "RESOURCE_NOT_FOUND", "Requested resource was not found");
    }

    @Test
    void shouldHandleBadRequest() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleBadRequest(new IllegalArgumentException("Invalid"));

        assertResponse(response, 400, "BAD_REQUEST", "Invalid request parameters");
    }

    @Test
    void shouldHandleBadRequestWithConstraintViolationException() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleBadRequest(mock(ConstraintViolationException.class));

        assertResponse(response, 400, "BAD_REQUEST", "Invalid request parameters");
    }

    @Test
    void shouldHandleBadRequestWithMethodArgumentTypeMismatchException() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleBadRequest(mock(MethodArgumentTypeMismatchException.class));

        assertResponse(response, 400, "BAD_REQUEST", "Invalid request parameters");
    }

    @Test
    void shouldHandleUnauthorized() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleUnauthorized(new BadCredentialsException("Invalid credentials"));

        assertResponse(response, 401, "UNAUTHORIZED", "Authentication is required or invalid");
    }

    @Test
    void shouldHandleForbidden() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleForbidden(mock(FeignException.Forbidden.class));

        assertResponse(response, 403, "FORBIDDEN", "You do not have permission to access this resource");
    }

    @Test
    void shouldHandleServiceUnavailable() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleServiceUnavailable(new ResourceAccessException("Service unavailable"));

        assertResponse(response, 503, "EXTERNAL_SERVICE_UNAVAILABLE", "External service is unavailable");
    }

    @Test
    void shouldHandleExternalInternalError() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleExternalInternalError(mock(FeignException.InternalServerError.class));

        assertResponse(response, 502, "EXTERNAL_SERVICE_ERROR", "External service returned an internal error");
    }

    @Test
    void shouldHandleMethodNotAllowed() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleMethodNotAllowed(mock(HttpRequestMethodNotSupportedException.class));

        assertResponse(response, 405, "METHOD_NOT_ALLOWED", "HTTP method is not allowed for this endpoint");
    }

    @Test
    void shouldHandleGenericException() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleGeneric(new RuntimeException("Unexpected"));

        assertResponse(response, 500, "INTERNAL_SERVER_ERROR", "Unexpected error occurred");
    }

    private void assertResponse(
            ResponseEntity<ApiErrorResponse> response,
            int expectedStatus,
            String expectedError,
            String expectedMessage
    ) {
        assertNotNull(response);
        assertEquals(expectedStatus, response.getStatusCode().value());

        ApiErrorResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(expectedStatus, body.status());
        assertEquals(expectedError, body.error());
        assertEquals(expectedMessage, body.message());
        assertNotNull(body.timestamp());
    }
}