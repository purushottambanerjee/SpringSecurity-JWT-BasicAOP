package com.example.TaskManagement.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class to set up authentication and authorization for the application.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    JWTFilter jwtFilter; // Injecting the JWT filter for token-based authentication

    // Define public endpoints for POST (e.g., register, login) and GET (e.g., healthcheck)
    public static final String[] PUBLIC_POST_END_POINTS = {"/user/register", "/user/login"};
    public static final String[] PUBLIC_READ_END_POINTS = {"/tasks/healthcheck"};

    /**
     * Configures the security filter chain for the application.
     *
     * @param httpSecurity The HttpSecurity configuration object.
     * @return The security filter chain for the application.
     * @throws Exception If there is a problem with the configuration.
     */
    @Bean
    public DefaultSecurityFilterChain securityConfig(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session management
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST_END_POINTS).permitAll() // Allow public access to POST endpoints (e.g., register, login)
                        .requestMatchers(HttpMethod.GET, PUBLIC_READ_END_POINTS).permitAll() // Allow public access to GET endpoints (e.g., healthcheck)
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter before authentication filter
                .build();
    }

    /**
     * Configures the authentication manager for the application.
     *
     * @param configuration The authentication configuration object.
     * @return The authentication manager for the application.
     * @throws Exception If there is a problem with the configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); // Return the configured authentication manager
    }
}
