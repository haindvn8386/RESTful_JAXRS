package restful.jaxrs.entity;
import jakarta.persistence.*;
import lombok.Data;
import restful.jaxrs.enums.ProjectStatus;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name="Projects")

public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToMany
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members = new ArrayList<>();


    @OneToMany
    @JoinColumn(name = "project_id")
    private List<Task> tasks = new ArrayList<>();
}


