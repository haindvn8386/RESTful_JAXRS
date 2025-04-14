package restful.jr.dto;
import lombok.Data;
import restful.jr.entity.Staff;
import restful.jr.enums.ProjectStatus;

@Data
public class ProjectDTO {
    private Long id;
    private String Code;
    private String name;
    private String description;
    private Staff manager;
    private ProjectStatus statusProject;
}