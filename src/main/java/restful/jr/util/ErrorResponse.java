package restful.jr.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse<T> { // Thêm <T> để ErrorResponse thành generic
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T details; // T giờ hợp lệ
}