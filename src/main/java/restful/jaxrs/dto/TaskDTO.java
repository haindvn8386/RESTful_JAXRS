package restful.jaxrs.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import restful.jaxrs.entity.Project;
import restful.jaxrs.enums.TaskStatus;

@Data
public class TaskDTO {

    private Long id;
    @NotBlank(message = "Title is mandatory")
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus statusStaff;

    @NotNull(message = "Project ID is mandatory")
    private Project project;
}
