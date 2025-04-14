package restful.jr.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import restful.jr.util.ApiResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(DuplicateResourceException.class)
//    public ResponseEntity<ApiResponse<Object>> handleDuplicateResourceException(DuplicateResourceException ex) {
//        ApiResponse<Object> response = new ApiResponse<>(
//                HttpStatus.CONFLICT.value(),
//                ex.getMessage(),
//                ex.getErrors().isEmpty() ? null : ex.getErrors()
//        );
//        response.setErrorCode("DUPLICATE_RESOURCE");
//        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//    }
//
//    // Xử lý các exception khác
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
//        ApiResponse<Object> response = new ApiResponse<>(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "An unexpected error occurred",
//                "SERVER_ERROR"
//        );
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//    /*

    //int status, String message, T data, String errorCode
    //Utility function to create ApiResponse
    private ResponseEntity<ApiResponse<Object>> buildApiErrorResponse(HttpStatus status, String message, Object details, String errorCode) {
        ApiResponse<Object> apiResponse = ApiResponse.error(status.value(), message, details,errorCode);
        return new ResponseEntity<>(apiResponse, status);
    }

    //General Exception Handling
    //Internal Server Error:500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex, WebRequest request) {
        return buildApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ex.getMessage(), "INTERNAL_SERVER_ERROR");
    }

    //Handle resource not found exception (ResourceNotFoundException)
    //404 NotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return buildApiErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage(), "NOT_FOUND");
    }

    //Handle resource not found exception (ResourceNotFoundException)
    //409 conflictException
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResourceException(DuplicateResourceException ex) {
        return buildApiErrorResponse(HttpStatus.CONFLICT, "Resource conflict", ex.getErrors(), "CONFLICT");
    }

    //Handle method not found exception (NoHandlerFoundException)
    //404 NotFoundException
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFoundException(
            org.springframework.web.servlet.NoHandlerFoundException ex, WebRequest request) {
        return buildApiErrorResponse(HttpStatus.NOT_FOUND, "Endpoint not found", ex.getMessage(), "NOT_FOUND");
    }

    //Handling invalid data exception (MethodArgumentNotValidException)
    //Validation Errors (400 - Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return buildApiErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors, "BAD_REQUEST");
    }

    //Handling invalid request exception (IllegalArgumentException)
    //Validation Errors (400 - Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildApiErrorResponse(HttpStatus.BAD_REQUEST, "Invalid argument", ex.getMessage(), "BAD_REQUEST");
    }


    //Handling AccessDeniedException
    //Access Denied (403 - Forbidden)
//    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
//    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
//            org.springframework.security.access.AccessDeniedException ex, WebRequest request) {
//        return buildApiErrorResponse(HttpStatus.FORBIDDEN, "Access denied", ex.getMessage());
//    }

    //Handling duplicate data exceptions (DataIntegrityViolationException)
    //Duplicate Entry (409 - Conflict)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {
        return buildApiErrorResponse(HttpStatus.CONFLICT, "Data integrity violation", ex.getMessage(), "CONFLICT");
    }

    //Application: When calling the wrong HTTP method (for example: calling GET instead of POST).
    //MethodNotAllowed (405)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex) {
        return buildApiErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,"Method not allowed",ex.getMessage(), "METHOD_NOT_ALLOWED");
    }

    //Application: When the uploaded file exceeds the allowed size.
    //File Upload Errors
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex) {
        return buildApiErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE,"File too large! Max size", ex.getMaxUploadSize(), "PAYLOAD_TOO_LARGE");
    }

    //JSON parse error
    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<ApiResponse<Object>> handleJsonMappingException(JsonMappingException ex) {
        return buildApiErrorResponse(HttpStatus.BAD_REQUEST,"JSON parse error",ex.getMessage(), "BAD_REQUEST");
    }
}
