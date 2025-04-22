package restful.jr.service.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import restful.jr.enums.TokenType;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService implements JwtInterface {

    @Value("${jwt.expirationMinutes}")
    private Long expirationMinutes;

    @Value("${jwt.expirationDay}")
    private Long expirationDay;

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;


    @Override
    public String generateAccessToken(Long userId, String userName, Collection<? extends GrantedAuthority> authorities) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authorities);
        return generateAccessToken(claims, userName);
    }

    @Override
    public String generateRefreshToken(Long userId, String userName, Collection<? extends GrantedAuthority> authorities) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authorities);
        return generateRefreshToken(claims, userName);
    }

    @Override
    public String extractUsername(String token, TokenType tokenType) {

        return getClaimsFromToken(tokenType, token, Claims::getSubject);
    }


    private <T> T getClaimsFromToken(TokenType tokenType, String token, Function<Claims, T> claimsRxtractor) {
        final Claims claims = extractClaim(token, tokenType);
        return claimsRxtractor.apply(claims);
    }

    private Claims extractClaim(String token, TokenType tokenType) {
        try {
            //return Jwts.parser().setSigningKey(accessKey).parseClaimsJws(token).getBody();
            return Jwts.parser()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException | ExpiredJwtException e) {
            throw new AccessDeniedException("Access denied! : " + e.getMessage());
        }
    }


    private String generateAccessToken(Map<String, Object> claims, String username) {
        // Tạo JWT
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expirationMinutes))
                .signWith(getSecretKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, String username) {

        // Tạo JWT
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expirationDay))
                .signWith(getSecretKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key getSecretKey(TokenType tokenType) {
        switch (tokenType) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + tokenType);
        }

    }
}


