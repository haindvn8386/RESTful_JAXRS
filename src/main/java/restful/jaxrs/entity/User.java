package restful.jaxrs.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String Name;

    @Email(message = "Email should be valid")
    private String email;

    @OneToMany(mappedBy = "assignedUser")
    private List<Task> tasks;
}
