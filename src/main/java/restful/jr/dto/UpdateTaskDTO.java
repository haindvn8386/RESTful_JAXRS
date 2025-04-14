package restful.jr.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import restful.jr.entity.Project;
import restful.jr.enums.TaskStatus;

@Data
public class UpdateTaskDTO {

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus statusStaff;

    private Project project;
}
