package restful.jr.dto;

import lombok.Data;

@Data
public class StaffDTO extends BaseDTO {
    private Long id;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
}
