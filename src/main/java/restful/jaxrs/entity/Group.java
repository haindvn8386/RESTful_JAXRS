package restful.jaxrs.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "groups")
public class Group extends BaseEntity<Long> {


    @NotBlank(message = "Name required input")
    private String name;

    @Column(length = 500)
    private String description;


    //n-n user-group
    @ManyToMany(mappedBy = "groups")
    private List<User> users = new ArrayList<>();

    //n-n role-group
    @ManyToMany(mappedBy = "groups")
    private List<Role> roles = new ArrayList<>();

}
