package restful.jaxrs.exception;

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
import restful.jaxrs.util.ApiResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Hàm tiện ích để tạo ErrorResponse
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, Object details) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                message,
                details
        );
        return new ResponseEntity<>(errorResponse, status);
    }
    // Hàm tiện ích để tạo ApiResponse
    private ResponseEntity<ApiResponse<Object>> buildApiErrorResponse(HttpStatus status, String message, Object details) {
        ApiResponse<Object> apiResponse = ApiResponse.error(status.value(), message, details);
        return new ResponseEntity<>(apiResponse, status);
    }
    //Xử lý ngoại lệ chung (Exception)
    //Internal Server Error:500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex, WebRequest request) {
        return buildApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ex.getMessage());
    }

    // Xử lý ngoại lệ không tìm thấy tài nguyên (ResourceNotFoundException)
    //404 NotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return buildApiErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage());
    }

    // Xử lý ngoại lệ không tìm thấy phương thức (NoHandlerFoundException)
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFoundException(
            org.springframework.web.servlet.NoHandlerFoundException ex, WebRequest request) {
        return buildApiErrorResponse(HttpStatus.NOT_FOUND, "Endpoint not found", ex.getMessage());
    }

    // Xử lý ngoại lệ dữ liệu không hợp lệ (MethodArgumentNotValidException)
    //Validation Errors (400 - Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return buildApiErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    // Xử lý ngoại lệ yêu cầu không hợp lệ (IllegalArgumentException)
    //Validation Errors (400 - Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildApiErrorResponse(HttpStatus.BAD_REQUEST, "Invalid argument", ex.getMessage());
    }

    // Xử lý ngoại lệ truy cập bị từ chối (AccessDeniedException)
    //Access Denied (403 - Forbidden)
//    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
//    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
//            org.springframework.security.access.AccessDeniedException ex, WebRequest request) {
//        return buildApiErrorResponse(HttpStatus.FORBIDDEN, "Access denied", ex.getMessage());
//    }

    // Xử lý ngoại lệ dữ liệu trùng lặp (DataIntegrityViolationException)
    //Duplicate Entry (409 - Conflict)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {
        return buildApiErrorResponse(HttpStatus.CONFLICT, "Data integrity violation", ex.getMessage());
    }

    //MethodNotAllowed (405)
    //Ứng dụng: Khi gọi sai HTTP method (ví dụ: gọi GET thay vì POST).
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex) {
        return buildApiErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,"Method not allowed",ex.getMessage());
    }

    //File Upload Errors
    //Ứng dụng: Khi file upload vượt quá kích thước cho phép.
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex) {
        return buildApiErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE,"File too large! Max size", ex.getMaxUploadSize());
    }

    //JSON parse error
    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<ApiResponse<Object>> handleJsonMappingException(JsonMappingException ex) {
        return buildApiErrorResponse(HttpStatus.BAD_REQUEST,"JSON parse error",ex.getMessage());
    }
}
