package com.example.TaskManagement.Security;

import com.example.TaskManagement.API.Service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Component responsible for generating, validating, and refreshing JWT tokens.
 */
@Component
public class JWTToken {

    @Autowired
    UserService userService; // Service to interact with user-related data

    // Predefined list of roles for the token, typically can be expanded as needed
    List<String> roles = List.of("USER");

    // Secret key used for signing the JWT token
    public static final String secretKey = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    /**
     * Generates a JWT token for the specified username.
     *
     * @param userName The username for which the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(String userName) {
        return Jwts.builder()
                .setClaims(new HashMap<>()) // Optional claims for the token
                .claim("roles", roles) // Add roles claim to the token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Token expiry time (30 minutes)
                .setIssuedAt(new Date()) // Set the issue time
                .setSubject(userName) // Set the username as subject
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // Sign the token with the secret key
                .compact(); // Generate the token
    }

    /**
     * Retrieves the signing key from the secret key for JWT token generation/validation.
     *
     * @return The signing key.
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decode the secret key
        return Keys.hmacShaKeyFor(keyBytes); // Generate a secret key for signing the JWT
    }

    /**
     * Checks if the provided JWT token is expired.
     *
     * @param token The JWT token.
     * @return true if the token is not expired, false otherwise.
     */
    public boolean isNotExpired(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token).getBody(); // Parse and extract claims from the token
        return claims.getExpiration().after(new Date()); // Check if expiration is after current time
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token The JWT token.
     * @return The username (subject) from the token.
     */
    public String FetchUserName(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build()
                .parseClaimsJws(token).getBody()
                .getSubject(); // Extract the subject (username) from the token
    }

    /**
     * Refreshes the JWT token if the provided token is valid and the user exists.
     *
     * @param token The JWT token.
     * @param username The username to validate.
     * @return The new JWT token or an error message.
     */
    public String RefreshToken(String token, String username) {
        if (ValidateToken(token, username) && userService.LoadByUserName(username) != null) {
            return generateToken(username); // Return a new token if valid
        }
        return "Incorrect Token"; // Return an error message if the token is invalid
    }

    /**
     * Validates the JWT token by checking if the username matches and if the token is not expired.
     *
     * @param token The JWT token.
     * @param username The username to check.
     * @return true if the token is valid, false otherwise.
     */
    public boolean ValidateToken(String token, String username) {
        String usernameFetched = FetchUserName(token); // Fetch username from the token
        if (usernameFetched.equals(username) && isNotExpired(token)) {
            return true; // Token is valid
        } else {
            return false; // Token is invalid
        }
    }

    /**
     * Extracts the roles from the JWT token.
     *
     * @param token The JWT token.
     * @return The list of roles from the token.
     */
    public List<String> extractRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token).getBody(); // Extract claims from the token
        return (List<String>) claims.get("roles"); // Return the roles from the claims
    }
}
