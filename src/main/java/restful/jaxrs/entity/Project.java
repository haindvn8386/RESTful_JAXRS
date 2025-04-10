package restful.jaxrs.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import restful.jaxrs.enums.ProjectStatus;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "Projects")

public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotNull(message = "Code required")
    private String code;

    @Column(unique = true, nullable = false)
    @NotNull(message = "Name required")
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ManyToOne //1 user can manager many project
    @JoinColumn(name = "staff_id")
    private Staff manager;

    @ManyToMany
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "staff_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    )
    private List<Staff> members = new ArrayList<>();

    @OneToMany
    @JoinColumn(name="project_id")
    private List<Task> tasks = new ArrayList<>();
}


