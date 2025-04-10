package restful.jaxrs.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import restful.jaxrs.enums.TaskStatus;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)

    @NotNull(message = "Title is mandatory")
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @NotNull(message = "Staff is mandatory")
    @Column(name = "staff_id")
    private Long staffId;

    @NotNull(message = "Project  is mandatory")
    @Column(name = "project_id")
    private Long projectId;
}
