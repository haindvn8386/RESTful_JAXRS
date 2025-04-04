package restful.jaxrs.util;

import restful.jaxrs.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ErrorResponse error;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(int code, String message, Object details) {
        return new ApiResponse<>(false, null, new ErrorResponse(java.time.LocalDateTime.now(), code, message, details));
    }
}

