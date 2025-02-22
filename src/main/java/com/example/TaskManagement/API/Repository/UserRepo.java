package com.example.TaskManagement.API.Repository;

import com.example.TaskManagement.API.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    // Changed 'Username' to 'username' to follow Java naming conventions
    Optional<User> findByUsername(String username);

}
