package restful.jaxrs.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import restful.jaxrs.enums.TaskStatus;

import java.util.List;

@Data
public class StaffDTO extends BaseDTO {
    private Long id;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
}
