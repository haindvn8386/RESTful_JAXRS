package restful.jr.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity<Long> implements UserDetails {

    @NotBlank
    @Size(max = 256)
    @Column(name = "user_name", length = 256, unique = true)
    private String username;

    @NotBlank
    @Column(name = "password_hash", columnDefinition = "TEXT")
    private String passwordHash;

    @Size(max = 256)
    @Column(name = "normalized_user_name", length = 256, unique = true)
    private String normalizedUsername;

    @Email(message = "Email should be valid")
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


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    //@JsonIgnore
    private Profile profile;

    //1-n user-userRole
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles = new ArrayList<>();

    //1-n user-address
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Address> addresses = new ArrayList<>();


    //n-n user-group
    //CascadeType.PERSIST: When you persist entity A, if entity B is not persisted, it will be persisted automatically.
    //CascadeType.MERGE: When you merge entity A, entity B will be merged as well.
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "group_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    )
    private List<Group> groups = new ArrayList<>();



    /***add more**/
    /***add more**/
    /***add more**/
    /***add more**/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //get roles
        List<Role> roles = userRoles.stream().map(UserRole::getRole).toList();
        //get role name
        List<String> roleNames = roles.stream().map(Role::getName).toList();

        return roleNames.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }


    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
