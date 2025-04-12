package restful.jaxrs.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "permission")
public class Permission extends BaseEntity<Long> {

    @NotBlank(message = "name is mandatory")
    @Column(length = 256, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    //n-n permission-role
    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles = new ArrayList<>();

}
