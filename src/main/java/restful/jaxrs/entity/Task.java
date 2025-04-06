package restful.jaxrs.entity;
import jakarta.persistence.*;
import lombok.Data;
import restful.jaxrs.enums.TaskStatus;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Project project;

}
