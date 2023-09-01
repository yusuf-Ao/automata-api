package io.aycodes.automataapi.common.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.aycodes.automataapi.common.dtos.CustomResponse;
import io.aycodes.automataapi.common.dtos.auth.AuthenticationTokenDetails;
import io.aycodes.automataapi.users.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService                jwtService;
    private final UserDetailsService        userDetailsService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (SecurityConfig.isApiSecured().test(request)) {
            log.info("Checking for authorization token");
            try {
                log.info("Checking if authorization type is Bearer Token");
                final String authHeader = Objects.requireNonNull(request.getHeader("AUTHORIZATION"));
                final String[] parts = authHeader.split(" ");
                if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                    final String message = "Incorrect Auth Structure";
                    response.setHeader("error", message);
                    response.setStatus(UNAUTHORIZED.value());
                    CustomResponse error = CustomResponse.builder()
                            .statusCode(HttpStatus.UNAUTHORIZED.value()).status(HttpStatus.UNAUTHORIZED)
                            .message(message).success(false)
                            .build();
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                    return;

                }
                log.info("Forwarding token for parsing and validation");
                final String jwtToken = parts[1];
                final AuthenticationTokenDetails authenticationTokenDetails = jwtService.parseToken(jwtToken);
                final String username = authenticationTokenDetails.getSubject();

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    log.info("Setting the authentication for the bearer token");
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            null
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (final Exception e) {
                final String message = "Invalid Credentials";
                response.setHeader("error", message);
                response.setStatus(UNAUTHORIZED.value());
                CustomResponse error = CustomResponse.builder()
                        .statusCode(HttpStatus.UNAUTHORIZED.value()).status(HttpStatus.UNAUTHORIZED)
                        .message(message).success(false)
                        .build();
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }

        }
        filterChain.doFilter(request, response);
    }
}
