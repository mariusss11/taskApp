package com.app.task.controller;

import com.app.task.dto.TaskDTO;
import com.app.task.model.ChangeTaskRequest;
import com.app.task.model.CreateTaskRequest;
import com.app.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing {@link com.app.task.model.Task} entities.
 * Provides endpoints for creating, retrieving, and updating tasks.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // -------------------------------------------------------------------------
    // 🔹 CREATE
    // -------------------------------------------------------------------------

    /**
     * Creates a new task for the currently authenticated user.
     *
     * @param request DTO containing task creation details
     * @return ResponseEntity with the created {@link TaskDTO}
     */
    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody CreateTaskRequest request) {
        log.info("Received request to create a new task with title: {}", request.getTitle());
        return ResponseEntity.ok(taskService.createTask(request));
    }

    // -------------------------------------------------------------------------
    // 🔹 RETRIEVE
    // -------------------------------------------------------------------------

    /**
     * Retrieves a task by its ID.
     *
     * @param taskId ID of the task to retrieve
     * @return ResponseEntity containing the requested {@link TaskDTO}
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable int taskId) {
        log.debug("Fetching task with ID: {}", taskId);
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    /**
     * Retrieves all tasks.
     *
     * @return ResponseEntity containing a list of all {@link TaskDTO}s
     */
    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        log.debug("Fetching all tasks");
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * Retrieves all enabled (active) tasks.
     *
     * @return ResponseEntity containing a list of enabled {@link TaskDTO}s
     */
    @GetMapping("/enabled")
    public ResponseEntity<List<TaskDTO>> getAllEnabledTasks() {
        log.debug("Fetching all enabled tasks");
        return ResponseEntity.ok(taskService.getAllEnabledTasks());
    }

    /**
     * Retrieves all tasks created by a specific user.
     *
     * @param userId ID of the user
     * @return ResponseEntity containing a list of the user's {@link TaskDTO}s
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskDTO>> getUserTasks(@PathVariable int userId) {
        log.debug("Fetching tasks for user ID: {}", userId);
        return ResponseEntity.ok(taskService.getUserTasks(userId));
    }

    // -------------------------------------------------------------------------
    // 🔹 UPDATE
    // -------------------------------------------------------------------------

    /**
     * Updates the status of a task.
     *
     * @param taskId  ID of the task to update
     * @param request DTO containing the new status
     * @return ResponseEntity containing the updated {@link TaskDTO}
     */
    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskDTO> changeTaskStatus(
            @PathVariable int taskId,
            @Valid @RequestBody ChangeTaskRequest request) {

        log.info("Updating status of task ID: {} to {}", taskId, request.getNewStatus());
        request.setTaskId(taskId); // ensure path variable consistency
        TaskDTO updatedTask = taskService.changeTaskStatus(request);
        return ResponseEntity.ok(updatedTask);
    }

    // -------------------------------------------------------------------------
    // 🔹 (OPTIONAL FUTURE) SEARCH
    // -------------------------------------------------------------------------
    // If you plan to reintroduce search later:
    //
    // @GetMapping("/search")
    // public ResponseEntity<List<TaskDTO>> searchTasks(@RequestParam String query) {
    //     log.debug("Searching tasks with query: {}", query);
    //     return ResponseEntity.ok(taskService.searchTasks(query));
    // }
}
