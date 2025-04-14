package restful.jr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "roles")

public class Role extends BaseEntity<Long> {

    @NotBlank(message = "name is mandatory")
    @Column(length = 256, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    //1-n role-userRole
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles = new ArrayList<>();

    //n-n role-permission
    //CascadeType.PERSIST: When you persist entity A, if entity B is not persisted, it will be persisted automatically.
    //CascadeType.MERGE: When you merge entity A, entity B will be merged as well.
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "permission_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    )
    private List<Permission> permissions = new ArrayList<>();

    //n-n role-group
    //CascadeType.PERSIST: When you persist entity A, if entity B is not persisted, it will be persisted automatically.
    //CascadeType.MERGE: When you merge entity A, entity B will be merged as well.
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "role_group",
            joinColumns = @JoinColumn(name = "role_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "group_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    )
    private List<Group> groups = new ArrayList<>();

}
