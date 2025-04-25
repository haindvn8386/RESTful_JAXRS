package restful.jr.service.auth;

import org.springframework.security.core.GrantedAuthority;
import restful.jr.enums.TokenType;

import java.util.Collection;
import java.util.List;

public interface JwtInterface {
    String generateAccessToken(Long userId, String userName, List<String> authorities);
    String generateRefreshToken(Long userId, String userName, List<String> authorities);
    String extractUsername(String token, TokenType tokenType);
}
