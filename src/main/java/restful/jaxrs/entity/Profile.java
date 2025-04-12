package restful.jaxrs.entity;

import jakarta.persistence.*;
import lombok.Data;
import restful.jaxrs.enums.Gender;

import java.util.Date;

@Entity
@Data
@Table(name = "profiles")
public class Profile extends BaseEntity<Long> {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth", length = 50)
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "avatarUrl")
    private String avatarUrl;

}
