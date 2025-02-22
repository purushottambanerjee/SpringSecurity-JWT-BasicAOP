package com.example.TaskManagement.API.Controller;

import com.example.TaskManagement.API.Model.User;
import com.example.TaskManagement.API.Service.UserService;
import com.example.TaskManagement.Security.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * Controller class for handling user registration and login functionality.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService; // Injecting the UserService to handle user-related operations

    @Autowired
    JWTToken TokenService; // Injecting JWTToken service to generate tokens

    /**
     * Registers a new user.
     *
     * @param user The user to be registered.
     * @return ResponseEntity containing the result of the registration attempt.
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        // Check if the user already exists by attempting to register
        if (userService.RegisterUser(user) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Already Exists\n"); // Return bad request if user exists
        } else {
            // If registration is successful, generate a JWT token for the user
            String token = TokenService.generateToken(user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(user + "\n" + "Token: " + token); // Return user and token
        }
    }

    /**
     * Logs in an existing user.
     *
     * @param userDetails A map containing the username and password.
     * @return ResponseEntity with a JWT token if login is successful.
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> userDetails) {
        // Extract username and password from the request body
        Optional<User> user = userService.LoginUser(userDetails.get("username"), userDetails.get("password"));

        // If user is not found, return forbidden status
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            // If login is successful, generate and return a JWT token
            return ResponseEntity.status(HttpStatus.OK).body(TokenService.generateToken(userDetails.get("username")));
        }
    }
}
