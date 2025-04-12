package restful.jaxrs.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "staffs")
public class Staff extends BaseEntity<Long> {

    @Column(unique = true, nullable = false)
    private String fullName;

    @Column(name = "date_of_birth", length = 50)
    private String dateOfBirth;

    @Column(name = "gender", columnDefinition = "TEXT")
    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @ManyToMany(mappedBy = "staffs")
    private List<Task> tasks = new ArrayList<>();
}
