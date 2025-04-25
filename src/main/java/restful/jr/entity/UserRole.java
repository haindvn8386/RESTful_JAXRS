package restful.jr.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "user_role")
public class UserRole {

    /**
     * RBAC does not support permissions with expiration dates.
     * For example, if bro wants to assign APPROVE_PROJECT permission to jane_smith for 1 week,
     * You have to add and remove it manually.
     * Temporary Permissions are supported:
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;



}
