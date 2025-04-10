package restful.jaxrs.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 256)
    @Column(name = "user_name", length = 256, unique = true)
    private String userName;

    @NotBlank
    @Column(name = "password_hash", columnDefinition = "TEXT")
    private String passwordHash;

    @Size(max = 256)
    @Column(name = "normalized_user_name", length = 256, unique = true)
    private String normalizedUserName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Size(max = 256)
    @Column(name = "email", length = 256, unique = true)
    private String email;

    @Size(max = 256)
    @Column(name = "normalized_email", length = 256, unique = true)
    private String normalizedEmail;

    @Column(name = "email_confirmed")
    private boolean emailConfirmed;

    @Column(name = "security_stamp", columnDefinition = "TEXT")
    private String securityStamp;

    @Column(name = "concurrency_stamp", columnDefinition = "TEXT")
    private String concurrencyStamp;

    @Column(name = "phone_number_confirmed")
    private boolean phoneNumberConfirmed;

    @Column(name = "two_factor_enabled")
    private boolean twoFactorEnabled;

    @Column(name = "lockout_end")
    private OffsetDateTime lockoutEnd;

    @Column(name = "lockout_enabled")
    private boolean lockoutEnabled;

    @Column(name = "access_failed_count")
    private int accessFailedCount;

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Task> tasks = new ArrayList<>();
}
