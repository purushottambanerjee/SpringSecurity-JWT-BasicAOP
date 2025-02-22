package com.example.TaskManagement.API.Service;

import com.example.TaskManagement.API.Model.User;
import com.example.TaskManagement.API.Repository.UserRepo;
import com.example.TaskManagement.Filter.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class that handles user-related operations, such as registration, login, and user retrieval.
 */
@Service
public class UserService {

    @Autowired
    UserRepo userRepo; // Injecting the User repository for database operations

    /**
     * Registers a new user if the username is not already taken.
     *
     * @param user The user to be registered.
     * @return The user if registration is successful, or null if the username already exists.
     */
    @Loggable
    public User RegisterUser(User user) {
        if (CheckPresent(user.getUsername())) { // Check if the user already exists
            return null; // Return null if user exists
        } else {
            // Hash the password before saving the user
            user.setPassword(String.valueOf(user.getPassword().hashCode()));
            userRepo.save(user); // Save the user to the repository
            return user; // Return the saved user
        }
    }

    /**
     * Validates the login credentials of a user.
     *
     * @param username The username of the user trying to log in.
     * @param password The password entered by the user.
     * @return An Optional containing the user if the credentials are valid, otherwise null.
     */
    @Loggable
    public Optional<User> LoginUser(String username, String password) {
        if (CheckPresent(username)) { // Check if the user exists
            Optional<User> user = userRepo.findByUsername(username);
            // Compare hashed password with stored password
            if (String.valueOf(password.hashCode()).equals(user.get().getPassword())) {
                return user; // Return the user if credentials match
            }
        }
        return Optional.empty(); // Return empty if credentials are invalid
    }

    /**
     * Loads a user by their username.
     *
     * @param username The username of the user to be loaded.
     * @return The user object associated with the given username.
     */
    public User LoadByUserName(String username) {
        return userRepo.findByUsername(username).orElse(null); // Return user if found, otherwise null
    }

    /**
     * Checks if a user already exists by their username.
     *
     * @param username The username to check.
     * @return true if the user exists, otherwise false.
     */
    public boolean CheckPresent(String username) {
        return userRepo.findByUsername(username).isPresent(); // Check if the username is present in the repository
    }
}
