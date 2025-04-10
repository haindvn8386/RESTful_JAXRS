package restful.jaxrs.mapper;

import org.mapstruct.Mapper;
import restful.jaxrs.dto.StaffDTO;
import restful.jaxrs.entity.Staff;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    StaffDTO toDto(Staff staff);
    Staff toEntity(StaffDTO staffDTO);
}
