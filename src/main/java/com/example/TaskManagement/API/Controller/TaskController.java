package com.example.TaskManagement.API.Controller;

import com.example.TaskManagement.API.Model.Task;
import com.example.TaskManagement.API.Service.TaskService;
import com.example.TaskManagement.Filter.Loggable;
import com.example.TaskManagement.Security.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing task-related endpoints.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    JWTToken tokenService; // Injecting JWT token service

    @Autowired
    TaskService taskService; // Injecting task service to handle task operations

    /**
     * Fetch all tasks for a given username.
     *
     * @param username The username whose tasks are to be retrieved.
     * @return A list of tasks for the specified username.
     */
    @GetMapping("/all")
    public ResponseEntity getAllTasks(@RequestParam String username) {
        // Fetch tasks using the task service
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAlltask(username));
    }

    /**
     * Health check endpoint to verify if the service is up.
     *
     * @return A string indicating the service status.
     */
    @Loggable // Custom loggable annotation
    @GetMapping("/healthcheck")
    public String healthCheck() {
        return "Service is UP!!!!!";
    }

    /**
     * Create a new task for a given user.
     *
     * @param username The username who is creating the task.
     * @param task The task to be created.
     * @return HTTP response indicating the status of the creation.
     */
    @PostMapping("/createTask")
    public ResponseEntity createTask(@RequestParam String username, @RequestBody Task task) {
        // Attempt to create the task using the service
        if (taskService.CreateTask(task, username)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Delete a task by its ID for the specified user.
     *
     * @param username The username requesting the deletion.
     * @param id The ID of the task to be deleted.
     * @return HTTP response indicating the result of the deletion.
     */
    @DeleteMapping("/delete")
    public ResponseEntity deleteById(@RequestParam String username, @RequestBody Long id) {
        // Attempt to delete the task using the service
        if (taskService.deleteTask(username, id)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Mark a task as completed for the given user.
     *
     * @param id The ID of the task to be marked as completed.
     * @param username The username who is marking the task.
     * @return HTTP response indicating the result of the operation.
     */
    @PutMapping("/mark_completed")
    public ResponseEntity markAsCompleted(@RequestParam Long id, @RequestParam String username) {
        // Attempt to update the task status to 'completed'
        if (taskService.UpdateTask(username, id)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
