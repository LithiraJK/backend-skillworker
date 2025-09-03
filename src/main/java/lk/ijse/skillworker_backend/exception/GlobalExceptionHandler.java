package lk.ijse.skillworker_backend.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lk.ijse.skillworker_backend.dto.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<String>> handleAllExceptions(Exception e) {
        System.err.println("Unhandled Exception: " + e.getMessage());
        return new ResponseEntity<>(new APIResponse<>(
                500,
                "An unexpected error occurred: " + e.getMessage(),
                null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse<String>> handleGenericException(ResourceNotFoundException e) {    //handle all exception here

        System.err.println("Resource not found: " + e.getMessage());
        return new ResponseEntity<>(new APIResponse<>(
                404,
                "Resource Not Found: " + e.getMessage(),
                null),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<APIResponse<String>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        System.err.println("Resource already exists: " + e.getMessage());
        return new ResponseEntity<>(new APIResponse<>(
                409,
                "Resource Already Exists: " + e.getMessage(),
                null),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Object>>  handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> { // Loop through each field error and add it to the errors map
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(new APIResponse<>( // Create a new APIResponse object with the validation errors
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errors
        ),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotFoundException .class)
    public ResponseEntity<APIResponse<String>> handleEmailNotFoundException(EmailNotFoundException e) {
        return new ResponseEntity<>(
                new APIResponse<>(
                        404,
                        e.getMessage(),
                        null
                ),HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIResponse<String>>  handleBadCredentialsException(BadCredentialsException e) {
        return new ResponseEntity<>(
                new APIResponse<>(
                        401,
                        e.getMessage(),
                        null
                ),HttpStatus.UNAUTHORIZED) ;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<APIResponse<String>> handleExpiredJwtException(ExpiredJwtException e) {
        return new ResponseEntity<>(
                new APIResponse<>(
                        401,
                        e.getMessage(),
                        null
                ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(
                new APIResponse<>(
                        400,
                        e.getMessage(),
                        null
                ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<APIResponse<String>> handleCategoryNotFoundException(CategoryNotFoundException e) {
        return new ResponseEntity<>(
                new APIResponse<>(
                        404,
                        e.getMessage(),
                        null
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<APIResponse<String>> handleIllegalStateException(IllegalStateException e) {
        return new ResponseEntity<>(
                new APIResponse<>(
                        400,
                        e.getMessage(),
                        null
                ), HttpStatus.BAD_REQUEST);
    }
}