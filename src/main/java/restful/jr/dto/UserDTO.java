package restful.jr.dto;

import lombok.Data;
import restful.jr.enums.Gender;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class UserDTO extends BaseDTO {
    private Long id;
    private String username;
    private String passwordHash;
    private String normalizedUsername;
    private String email;
    private String normalizedEmail;
    private boolean emailConfirmed;
    private String securityStamp;
    private String concurrencyStamp;
    private boolean phoneNumberConfirmed;
    private boolean twoFactorEnabled;
    private OffsetDateTime lockoutEnd;
    private boolean lockoutEnabled;
    private int accessFailedCount;


    private LocalDateTime createdAt ;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    //private Profile profile;
    // Thay Profile bằng các trường thô
    private String fullName;
    private String dateOfBirth;
    private Gender gender;
    private String phoneNumber;
    private String avatarUrl;

}
