package pt.com.aubay.userservice.controller.advice;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pt.com.aubay.userservice.model.dto.ApiErrorResponse;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            FeignException.NotFound.class,
            HttpClientErrorException.NotFound.class
    })
    public ResponseEntity<ApiErrorResponse> handleNotFound(Exception ex) {
        log.error("Resource not found", ex);
        return error(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND",
                "Requested resource was not found");
    }

    @ExceptionHandler({
            FeignException.BadRequest.class,
            HttpClientErrorException.BadRequest.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception ex) {
        log.error("Bad request", ex);
        return error(HttpStatus.BAD_REQUEST, "BAD_REQUEST",
                "Invalid request parameters");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        log.error("Validation failed", ex);
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation failed");

        return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }

    @ExceptionHandler({
            FeignException.Unauthorized.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(Exception ex) {
        log.error("Unauthorized", ex);
        return error(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED",
                "Authentication is required or invalid");
    }

    @ExceptionHandler(FeignException.Forbidden.class)
    public ResponseEntity<ApiErrorResponse> handleForbidden(Exception ex) {
        log.error("Forbidden", ex);
        return error(HttpStatus.FORBIDDEN, "FORBIDDEN",
                "You do not have permission to access this resource");
    }

    @ExceptionHandler({
            FeignException.ServiceUnavailable.class,
            ResourceAccessException.class
    })
    public ResponseEntity<ApiErrorResponse> handleServiceUnavailable(Exception ex) {
        log.error("External service unavailable", ex);
        return error(HttpStatus.SERVICE_UNAVAILABLE, "EXTERNAL_SERVICE_UNAVAILABLE",
                "External service is unavailable");
    }

    @ExceptionHandler(FeignException.InternalServerError.class)
    public ResponseEntity<ApiErrorResponse> handleExternalInternalError(Exception ex) {
        log.error("External service returned an internal error", ex);
        return error(HttpStatus.BAD_GATEWAY, "EXTERNAL_SERVICE_ERROR",
                "External service returned an internal error");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(Exception ex) {
        log.error("HTTP method is not allowed", ex);
        return error(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED",
                "HTTP method is not allowed for this endpoint");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR",
                "Unexpected error occurred");
    }

    private ResponseEntity<ApiErrorResponse> error(
            HttpStatus status,
            String error,
            String message
    ) {
        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(
                        status.value(),
                        error,
                        message,
                        Instant.now()
                ));
    }
}