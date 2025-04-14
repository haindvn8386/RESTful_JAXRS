package restful.jr.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import restful.jr.dto.UserDTO;
import restful.jr.entity.User;

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
