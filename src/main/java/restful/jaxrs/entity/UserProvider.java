package restful.jaxrs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

public class UserProvider extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, length = 50)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 450)
    private String userId;

    @Column(name = "provider", nullable = false, length = 200)
    private String provider;

    @Column(name = "provider_user_id", nullable = false, length = 150)
    private String providerUserId;

    @Column(name = "access_token", nullable = true)
    private String accessToken;

    @Column(name = "refresh_token", nullable = true)
    private String refreshToken;

    @Column(name = "token_expiration", nullable = true)
    private LocalDateTime tokenExpiration;
}
