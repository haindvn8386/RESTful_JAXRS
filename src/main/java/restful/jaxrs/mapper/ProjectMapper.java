package restful.jaxrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import restful.jaxrs.dto.ProjectDTO;
import restful.jaxrs.entity.Project;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDTO toDTO(Project project);

    Project toEntity(ProjectDTO projectDTO);

    List<ProjectDTO> toDtoList(List<Project> projects);
}
