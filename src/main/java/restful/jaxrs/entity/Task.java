package restful.jaxrs.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import restful.jaxrs.enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tasks")
public class Task extends BaseEntity<Long> {

    @Column(unique = true, nullable = false)
    @NotNull(message = "Title is mandatory")
    private String title;

    private String description;


    @Enumerated(EnumType.STRING)
    @Column(name="status_staff", nullable = false)
    private TaskStatus statusStaff;


    //n-n Task-Staff
    //CascadeType.PERSIST: When you persist entity A, if entity B is not persisted, it will be persisted automatically.
    //CascadeType.MERGE: When you merge entity A, entity B will be merged as well.
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "task_staff",
            joinColumns = @JoinColumn(name = "task_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "staff_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    )
    private List<Staff> staffs = new ArrayList<>();


    //task - project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
