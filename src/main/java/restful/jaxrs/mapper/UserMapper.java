package restful.jaxrs.mapper;

import org.mapstruct.Mapper;
import restful.jaxrs.dto.UserDTO;
import restful.jaxrs.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(UserDTO userDTO);
}
