package restful.jaxrs.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_logins")
@Data
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 450)
    private String userId;

    @Column(name = "device_id", nullable = false, length = 50)
    private String deviceId;

    @Column(name = "device_name", nullable = false, length = 255)
    private String deviceName;

    @Column(name = "login_time", nullable = false)
    private LocalDateTime loginTime;

    @Column(name = "ip_address", nullable = true, length = 50)
    private String ipAddress;

    @Column(name = "success", nullable = false)
    private int success;

    @Column(name = "login_provider", nullable = true, length = 500)
    private String loginProvider;

    @Column(name = "provider_key", nullable = true, length = 500)
    private String providerKey;

    @Column(name = "provider_display_name", nullable = true, length = 500)
    private String providerDisplayName;
}
