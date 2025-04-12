package restful.jaxrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import restful.jaxrs.dto.UserDTO;
import restful.jaxrs.entity.Task;
import restful.jaxrs.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "profile.fullName", target = "fullName")
    @Mapping(source = "profile.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "profile.gender", target = "gender")
    @Mapping(source = "profile.phoneNumber", target = "phoneNumber")
    @Mapping(source = "profile.address", target = "address")
    @Mapping(source = "profile.avatarUrl", target = "avatarUrl")
    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);
}
