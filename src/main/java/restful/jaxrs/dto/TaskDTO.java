package restful.jaxrs.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskDTO {

    private Long id;
    @NotBlank(message = "Title is mandatory")
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private String status;

    @NotBlank(message = "projectId is mandatory")
    private Long projectId;

    @NotBlank(message = "userId is mandatory")
    private Long userId;
}
