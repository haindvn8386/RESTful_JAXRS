package restful.jaxrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import restful.jaxrs.dto.TaskDTO;
import restful.jaxrs.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDTO toDTO(Task task);
    Task toEntity(TaskDTO taskDTO);
}
