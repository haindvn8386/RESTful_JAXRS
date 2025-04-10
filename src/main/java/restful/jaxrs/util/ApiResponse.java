package restful.jaxrs.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private int status;          // Mã trạng thái HTTP (ví dụ: 200, 400, 404, 500)
    private String message;      // Thông điệp mô tả (dành cho người dùng hoặc debug)
    private T data;              // Dữ liệu trả về (có thể là object, list, hoặc null nếu lỗi)
    private String errorCode;    // Mã lỗi nội bộ (nếu có, để frontend xử lý logic cụ thể)
    private long timestamp;      // Thời gian phản hồi (thường là milliseconds)


    //metadata is null
    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(true, status, message, data, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> error(int status, String message, T data, String errorCode) {
        return new ApiResponse<>(false, status, message, data, errorCode, System.currentTimeMillis());
    }


}

