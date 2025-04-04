package restful.jaxrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import restful.jaxrs.dto.TaskDTO;
import restful.jaxrs.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "assignedUser.id", target = "userId")
    TaskDTO toDto(Task task);

    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "userId", target = "assignedUser.id")
    Task toEntity(TaskDTO taskDTO);

}
