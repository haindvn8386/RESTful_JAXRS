package restful.jr.service.auth;

import org.springframework.security.core.GrantedAuthority;
import restful.jr.enums.TokenType;

import java.util.Collection;

public interface JwtInterface {
    String generateAccessToken(Long userId, String userName, Collection<?extends GrantedAuthority> authorities);
    String generateRefreshToken(Long userId, String userName, Collection<?extends GrantedAuthority> authorities);
    String extractUsername(String token, TokenType tokenType);
}
