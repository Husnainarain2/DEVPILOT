package devpilot.backend.exceptions;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice 
public class GlobalException {
    @ExceptionHandler (ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    @ExceptionHandler (BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex) {
        return error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler (UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException ex) {
        return error(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }
    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation error");
        return error(HttpStatus.BAD_REQUEST, message);
    }
    @ExceptionHandler (Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");
    }

	private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(Map.of(
            "status", status.value(),
            "error", status.getReasonPhrase(),
            "message", message,
            "timestamp", Instant.now().toString()
        ));
	}
}
