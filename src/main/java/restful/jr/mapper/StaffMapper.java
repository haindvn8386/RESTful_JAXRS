package restful.jr.mapper;

import org.mapstruct.Mapper;
import restful.jr.dto.StaffDTO;
import restful.jr.entity.Staff;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    StaffDTO toDto(Staff staff);
    Staff toEntity(StaffDTO staffDTO);
}
