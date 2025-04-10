package restful.jaxrs.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_token")
@Data
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 450)
    private String userId;

    @Column(name = "login_provider", nullable = false, length = 450)
    private String loginProvider;

    @Column(name = "name", nullable = false, length = 450)
    private String name;

    @Column(name = "value", nullable = true)
    private String value;
}