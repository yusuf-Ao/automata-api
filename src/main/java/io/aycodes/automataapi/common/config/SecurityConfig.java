package io.aycodes.automataapi.common.config;

import io.aycodes.automataapi.users.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter       jwtAuthFilter;
    private final AuthenticationProvider        authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //.cors(cors -> cors.)
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/v1/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(openEndpoints().toArray(String[]::new)).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(smc -> smc
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public static List<String> openEndpoints() {
        return List.of(
                "/api/v1/users/register",
                "/api/v1/auth/login",
                "/api/v1/auth/email-availability",
                "/api/v1/auth/username-availability",
                "/api/v1/users/new-user",
                "/swagger-ui",
                "/v3/api-docs"
        );
    }

    public static Predicate<HttpServletRequest> isApiSecured() {
        return r -> openEndpoints().stream()
                .noneMatch(uri -> r.getRequestURI().startsWith(uri));
    }

    public static User extractUserDetailsFromSecurityContext(final SecurityContextHolder securityContextHolder) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return  (User) userDetails;
    }

    public static Collection<? extends GrantedAuthority> extractGrantedAuthoritiesFromSecurityContext(final SecurityContextHolder securityContextHolder) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities();
    }
}
