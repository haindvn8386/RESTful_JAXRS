package restful.jr.mapper;

import org.mapstruct.Mapper;
import restful.jr.dto.TaskDTO;
import restful.jr.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDTO toDTO(Task task);
    Task toEntity(TaskDTO taskDTO);
}
