package restful.jaxrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import restful.jaxrs.dto.TaskDTO;
import restful.jaxrs.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "user.id", target = "userId")
    TaskDTO toDTO(Task task);

    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "userId", target = "user.id")
    Task toEntity(TaskDTO taskDTO);

}
