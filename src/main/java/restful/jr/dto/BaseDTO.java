package restful.jr.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class BaseDTO {

    private LocalDateTime createdAt ;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String status ;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
