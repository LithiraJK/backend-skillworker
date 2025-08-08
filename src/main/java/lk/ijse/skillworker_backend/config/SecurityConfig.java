package lk.ijse.skillworker_backend.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // This annotation enables method-level security, allowing you to use annotations like @PreAuthorize and @Secured on methods.
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JWTAuthConfigFilter JWTAuthConfigFilter;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { //http request eke dewal check kranw
        http.csrf(AbstractHttpConfigurer::disable) // Cross-Site Request Forgery (CSRF) protection is disabled
                .cors(Customizer.withDefaults()) // CORS (Cross-Origin Resource Sharing) is enabled with default settings
                .authorizeHttpRequests(
                        auth->
                                auth.requestMatchers("api/v1/auth/**").permitAll() // Allow unauthenticated access to /auth/** endpoints
                                        .anyRequest().authenticated()) // All other requests require authentication
                .sessionManagement(
                        session->
                                session.sessionCreationPolicy
                                        (SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(JWTAuthConfigFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider
                = new DaoAuthenticationProvider();
        daoAuthenticationProvider
                .setUserDetailsService(userDetailsService);
        daoAuthenticationProvider
                .setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;

    }


}
