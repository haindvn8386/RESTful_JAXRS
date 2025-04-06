package restful.jaxrs.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import restful.jaxrs.enums.ProjectStatus;

@Data
public class UpdateProjectDTO {
    private String name;
    private String description;
    private UserDTO manager;
    private ProjectStatus status;
}