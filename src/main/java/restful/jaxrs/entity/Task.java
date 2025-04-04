package restful.jaxrs.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedUser;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

}
