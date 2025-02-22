package com.example.TaskManagement.Security;

import com.example.TaskManagement.API.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.List;

/**
 * Filter class responsible for intercepting requests and validating JWT tokens.
 * If the token is valid, it authenticates the user and sets the authentication context.
 */
@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    UserSecurity userService; // Injecting the user service to load user details by username

    @Autowired
    JWTToken tokenService; // Injecting the JWT token service to validate tokens

    /**
     * Intercepts incoming HTTP requests to check for a valid JWT token.
     * If valid, the user's authentication is set in the security context.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        // Check if the token is null or malformed (no "Bearer" prefix)
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Set 400 for bad request if the token is missing or malformed
            response.getWriter().write("Invalid token or missing token"); // Optionally add a message
            return; // Short-circuit the filter chain
        }

        token = token.substring(7); // Remove "Bearer " prefix

        String username = tokenService.FetchUserName(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user_from_DB = (User) userService.loadUserByUsername(username);

            if (tokenService.ValidateToken(token, user_from_DB.getUsername())) {
                // Valid token, continue with the filter chain
                List<String> roles = tokenService.extractRolesFromToken(token);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user_from_DB.getUsername(), null, user_from_DB.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                // Invalid token, set 401 Unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return; // Short-circuit the filter chain
            }
        }

        filterChain.doFilter(request, response); // Proceed with the filter chain if all checks pass
    }

}
