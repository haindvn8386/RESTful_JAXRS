package restful.jr.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/v1/fakes/**")  // Disable CSRF for `/fakes/**`
                        .ignoringRequestMatchers("/api/v1/users/**")   // Disable CSRF for `/users/**`
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/fakes/**").permitAll()  // No auth required
                        .requestMatchers("/api/v1/users/**").permitAll()  // No auth required
                        .anyRequest().authenticated()  // All other requests need auth
                )
                .formLogin(form -> form.permitAll())
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}
