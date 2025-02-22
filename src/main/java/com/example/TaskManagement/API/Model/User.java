package com.example.TaskManagement.API.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;  // Changed from UserId to userId
    @Column(name = "user_name")
    String username;  // Maps to 'user_name' in the database
    String password;  // Changed from Password to password
    String role;  // Changed from Role to role

    public Long getUserId() {
        return userId;  // Updated to match the camel case of userId
    }

    public void setUserId(Long userId) {
        this.userId = userId;  // Updated to match the camel case of userId
    }

    public String getUsername() {
        return username;  // Updated to match the camel case of username
    }

    public void setUsername(String username) {
        this.username = username;  // Updated to match the camel case of username
    }

    public String getPassword() {
        return password;  // Updated to match the camel case of password
    }

    public void setPassword(String password) {
        this.password = password;  // Updated to match the camel case of password
    }

    public String getRole() {
        return role;  // Updated to match the camel case of role
    }

    public void setRole(String role) {
        this.role = role;  // Updated to match the camel case of role
    }
}
