package restful.jr.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import restful.jr.dto.RefreshTokenRequestDTO;
import restful.jr.dto.SignInRequestDTO;
import restful.jr.dto.TokenResponseDTO;
import restful.jr.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationInterface {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;



    @Override
    public TokenResponseDTO getAccessToken(SignInRequestDTO signInRequestDTO) {
        // Validate input
        if (signInRequestDTO == null || signInRequestDTO.getUsername() == null || signInRequestDTO.getPassword() == null) {
            throw new IllegalArgumentException("Username and password must not be null");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequestDTO.getUsername(), signInRequestDTO.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.replaceAll("[\\[\\]]", ""))
                .collect(Collectors.toList());
        log.info("Authorities: {}", authorities);
        String accessToken = jwtService.generateAccessToken(1L, userDetails.getUsername(), authorities);
        String refreshToken = jwtService.generateRefreshToken(1L, userDetails.getUsername(), authorities);

        // Build and return response
        return TokenResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponseDTO getRefreshToken(RefreshTokenRequestDTO refreshTokenDTO) {
        //validate input
        if (refreshTokenDTO == null || refreshTokenDTO.getRefreshToken().isEmpty()) {
            throw new IllegalArgumentException("Refresh token must not be null");
        }
        //Validate and extract claims from refresh token
        Claims claims;
        try {
            claims = jwtService.validateRefreshToken(refreshTokenDTO.getRefreshToken());
        } catch (JwtException e) {
            log.error("JwtException occurred while parsing refresh token: {}", e.getMessage());
            throw new AccessDeniedException("JwtException occurred while parsing refresh token: " + e.getMessage());
        }

        // Extract user information from claims
        String username = claims.getSubject();
        Long userId =  claims.get("user_id", Long.class);

        //Verify user authorities
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("User not found: " + username));

        // Verify user authorities
        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(userId, username, authorities);
        String newRefreshToken = jwtService.generateRefreshToken(userId, username, authorities);


        // Build and return response
        return TokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
