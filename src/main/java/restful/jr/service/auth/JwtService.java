package restful.jr.service.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import restful.jr.enums.TokenType;
import restful.jr.exception.JwtAuthenticationException;
import restful.jr.exception.JwtTokenExpiredException;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    public String generateAccessToken(Long userId, String userName, List<String> authorities) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authorities);
        return generateAccessToken(claims, userName);
    }

    @Override
    public String generateRefreshToken(Long userId, String userName, List<String> authorities) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authorities);
        return generateRefreshToken(claims, userName);
    }

    @Override
    public String extractUsername(String token, TokenType tokenType) {
        return getClaimsFromToken(tokenType, token, Claims::getSubject);
    }

    public <T> T getClaimsFromToken(TokenType tokenType, String token, Function<Claims, T> claimsRxtractor) {
        final Claims claims = extractAllClaims(token, tokenType);
        return claimsRxtractor.apply(claims);
    }

    public Claims extractAllClaims(String token, TokenType tokenType) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSecretKey(tokenType))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException("JWT Token has expired");
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Invalid JWT token");
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

    public Claims validateRefreshToken(String refreshToken) {
        try {
            // Parse and validate the refresh token
            return Jwts.parser()
                    .setSigningKey(getSecretKey(TokenType.REFRESH_TOKEN))
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();
        } catch (JwtException e) {
            throw new JwtException("Invalid or expired refresh token: " + e.getMessage());
        }
    }

}


