package com.example.TaskManagement.API.Service;

import com.example.TaskManagement.API.Model.Task;
import com.example.TaskManagement.API.Model.User;
import com.example.TaskManagement.API.Repository.TaskRepo;
import com.example.TaskManagement.Filter.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class that handles task-related operations, such as creating, retrieving, updating, and deleting tasks.
 */
@Service
public class TaskService {

    @Autowired
    TaskRepo taskRepo; // Injecting the Task repository for database operations

    @Autowired
    UserService userService; // Injecting the User service to load user details

    /**
     * Creates a new task for the given user.
     *
     * @param task The task to be created.
     * @param username The username of the user creating the task.
     * @return true if the task is successfully created, otherwise false.
     */
    @Loggable
    public boolean CreateTask(Task task, String username) {
        // Load user by username
        Optional<User> user = Optional.ofNullable(userService.LoadByUserName(username));
        if (user.isPresent()) {
            task.setUser(user.get()); // Assign the user to the task
            task.setCreated_at(new Date()); // Set task creation time
            task.setUpdated_at(new Date()); // Set task update time
            // Save the task and return whether it was successful
            return taskRepo.save(task) != null;
        }
        return false; // Return false if user is not found
    }

    /**
     * Retrieves all tasks for the given user.
     *
     * @param username The username of the user whose tasks are to be retrieved.
     * @return A list of tasks for the specified user, or null if the user does not exist.
     */
    @Loggable
    public List<Task> getAlltask(String username) {
        User user = userService.LoadByUserName(username);
        if (user != null) {
            // Find and return all tasks assigned to the user
            return taskRepo.findByUser_UserId(user.getUserId());
        }
        return null; // Return null if user is not found
    }

    /**
     * Deletes a task with the specified ID for the given user.
     *
     * @param username The username of the user requesting the task deletion.
     * @param TaskId The ID of the task to be deleted.
     * @return true if the task was deleted, otherwise false.
     */
    @Loggable
    public boolean deleteTask(String username, Long TaskId) {
        User user = userService.LoadByUserName(username);
        if (user != null) {
            taskRepo.deleteById(TaskId); // Delete task by ID
            return true;
        }
        return false; // Return false if user is not found
    }

    /**
     * Marks a task as completed by updating its status.
     *
     * @param username The username of the user completing the task.
     * @param TaskId The ID of the task to be marked as completed.
     * @return true if the task was successfully updated, otherwise false.
     */
    @Loggable
    public boolean UpdateTask(String username, Long TaskId) {
        User user = userService.LoadByUserName(username);
        Optional<Task> task = taskRepo.findById(TaskId);
        if (user != null && task.isPresent()) {
            task.get().setStatus("COMPLETED"); // Mark task as completed
            taskRepo.save(task.get()); // Save the updated task
            return true;
        }
        return false; // Return false if task or user is not found
    }
}
