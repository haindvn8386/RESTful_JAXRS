package restful.jaxrs.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import restful.jaxrs.entity.Staff;
import restful.jaxrs.enums.ProjectStatus;

@Data
public class ProjectDTO {
    private Long id;
    private String Code;
    private String name;
    private String description;
    private Staff manager;
    private ProjectStatus statusProject;
}