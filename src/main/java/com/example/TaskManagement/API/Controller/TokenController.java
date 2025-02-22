package com.example.TaskManagement.API.Controller;

import com.example.TaskManagement.Security.JWTToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller class for managing token-related operations such as checking and refreshing tokens.
 */
@RestController
@RequestMapping("/Token")
public class TokenController {

    @Autowired
    JWTToken jwtToken; // Injecting JWT token service for token-related operations

    /**
     * Endpoint to check the token validity (or service status).
     *
     * @return A message confirming that the token is fine.
     */
    @GetMapping("/check")
    public String checkToken() {
        return "Token is Fine"; // Simple health check for token validation
    }

    /**
     * Refreshes the token using the provided username and the token from the Authorization header.
     *
     * @param request The HTTP request used to extract the Authorization header.
     * @param req The request body containing the username for token refresh.
     * @return The refreshed JWT token.
     * @throws IllegalArgumentException If the Authorization header or username is invalid.
     */
    @PostMapping("/refresh")
    public String refreshToken(HttpServletRequest request, @RequestBody Map<String, String> req) {

        // Extract the token from the Authorization header
        String token = request.getHeader("Authorization");

        // Check if the token is present and valid
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }

        // Extract the actual token by removing the "Bearer " prefix
        token = token.substring(7);

        // Extract the username from the request body
        String username = req.get("username");

        // Validate the username
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        // Refresh and return the token using the JWT service
        return jwtToken.RefreshToken(token, username);
    }
}
