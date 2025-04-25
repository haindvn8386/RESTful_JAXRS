package restful.jr.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import restful.jr.enums.TokenType;
import restful.jr.exception.JwtAuthenticationException;
import restful.jr.exception.JwtTokenExpiredException;
import restful.jr.service.UserService;
import restful.jr.service.auth.JwtService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomizeRequestFilter extends OncePerRequestFilter {

    protected final Log logger = LogFactory.getLog(getClass());

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Danh sách endpoint permitAll
            List<String> permitAllEndpoints = List.of("/api/v1/auth/**");
            if (isPermitAllEndpoint(request, permitAllEndpoints)) {
                filterChain.doFilter(request, response);
                return;
            }

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new JwtAuthenticationException("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            String username;
            List<String> authorities;
            try {
                username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
                Claims claims = jwtService.extractAllClaims(token, TokenType.ACCESS_TOKEN);
                authorities = (List<String>) claims.get("role");
                if (authorities == null) {
                    authorities = Collections.emptyList();
                }

            } catch (JwtTokenExpiredException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new JwtAuthenticationException("Invalid token: " + ex.getMessage());
            }

            UserDetailsService userDetailsService = userService.UserServiceDetail();
            UserDetails userDetails;
            try {
                userDetails = userDetailsService.loadUserByUsername(username);
            } catch (Exception ex) {
                throw new JwtAuthenticationException("Failed to authenticate user: " + ex.getMessage());
            }

            // Ánh xạ quyền từ token
            List<GrantedAuthority> tokenAuthorities;
            try {
                tokenAuthorities = authorities.stream()
                        .map(auth -> auth.replaceAll("[\\[\\]]", ""))
                        .filter(auth -> !auth.isBlank())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

            } catch (Exception ex) {
                throw new JwtAuthenticationException("Invalid authorities in token: " + ex.getMessage());
            }

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, tokenAuthorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);


            filterChain.doFilter(request, response);
        } catch (AccessDeniedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new JwtAuthenticationException("Authentication failed: " + ex.getMessage());
        }
    }

    private boolean isPermitAllEndpoint(HttpServletRequest request, List<String> permitAllEndpoints) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String requestUri = request.getRequestURI();
        boolean isPermitAll = permitAllEndpoints.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestUri));

        return isPermitAll;
    }
}