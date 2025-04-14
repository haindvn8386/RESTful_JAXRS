package restful.jr.dto;
import lombok.Data;
import restful.jr.enums.ProjectStatus;

@Data
public class UpdateProjectDTO {
    private String code;
    private String name;
    private String description;
    private UserDTO manager;
    private ProjectStatus statusProject;
}