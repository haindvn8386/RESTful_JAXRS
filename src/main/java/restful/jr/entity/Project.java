package restful.jr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import restful.jr.enums.ProjectStatus;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "projects")

public class Project extends BaseEntity<Long> {

    @Column(unique = true, nullable = false)
    @NotNull(message = "Code required")
    private String code;

    @Column(unique = true, nullable = false)
    @NotNull(message = "Name required")
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="status_project", nullable = false)
    private ProjectStatus statusProject;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff StaffManager;


    @ManyToMany
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "staff_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    )
    private List<Staff> members = new ArrayList<>();

    //projects-task
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();
}


