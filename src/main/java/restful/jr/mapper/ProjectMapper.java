package restful.jr.mapper;

import org.mapstruct.Mapper;
import restful.jr.dto.ProjectDTO;
import restful.jr.entity.Project;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDTO toDTO(Project project);

    Project toEntity(ProjectDTO projectDTO);

    List<ProjectDTO> toDtoList(List<Project> projects);
}
