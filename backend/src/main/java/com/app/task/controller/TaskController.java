package com.app.task.controller;

import com.app.task.dto.TaskDTO;
import com.app.task.model.ChangeTaskRequest;
import com.app.task.model.CreateTaskRequest;
import com.app.task.model.Task;
import com.app.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that manages task-related operations such as
 * creating tasks, retrieving tasks, updating status, and filtering
 * tasks by user or enabled state.
 */
@Slf4j
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Constructs a TaskController with the required TaskService.
     *
     * @param taskService Service layer for managing tasks
     */
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Creates a new task.
     *
     * @param request DTO containing task creation details
     * @return ResponseEntity with the created Task
     */
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param taskId ID of the task to retrieve
     * @return ResponseEntity containing the requested TaskDTO
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getItemById(@PathVariable int taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    /**
     * Retrieves all tasks.
     *
     * @return ResponseEntity containing a list of TaskDTOs
     */
    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * Retrieves all enabled tasks (active tasks).
     *
     * @return ResponseEntity containing a list of enabled TaskDTOs
     */
    @GetMapping("/allEnabled")
    public ResponseEntity<List<TaskDTO>> getAllEnabledTasks() {
        return ResponseEntity.ok(taskService.getAllEnabledTasks());
    }

    /**
     * Retrieves all tasks assigned to a specific user.
     *
     * @param userId ID of the user
     * @return ResponseEntity containing a list of the user's TaskDTOs
     */
    @GetMapping("/users/{userId}/tasks")
    public ResponseEntity<List<TaskDTO>> getUserTasks(@PathVariable int userId) {
        return ResponseEntity.ok(taskService.getUsersTasks(userId));
    }

    /**
     * Changes the status of a task (e.g., enabled/disabled, completed/pending).
     *
     * @param request DTO containing task ID and the new status
     * @return ResponseEntity containing the updated Task
     */
    @PutMapping("/changeStatus")
    public ResponseEntity<Task> changeTaskStatus(@RequestBody ChangeTaskRequest request) {
        return ResponseEntity.ok(taskService.changeTaskStatus(request));
    }

//    /**
//     * Searches for tasks by a query string.
//     *
//     * @param searchQuery The search keyword
//     * @return ResponseEntity containing a list of matching tasks
//     */
//    @GetMapping("/search")
//    public ResponseEntity<List<Task>> getTasksByQuery(@RequestParam(name = "query") String searchQuery) {
//        return ResponseEntity.ok(taskService.getTasksByQuery(searchQuery));
//    }
//
//    /**
//     * Retrieves paginated tasks filtered by a search query.
//     *
//     * @param pageable Pagination parameters
//     * @param query    Search query string
//     * @return PagedResponse of enabled tasks matching the query
//     */
//    @GetMapping("/search/pagination")
//    public PagedResponse<Task> getAllTasksPaginated(Pageable pageable, @RequestParam String query) {
//        return itemService.getAllEnabledTasksPaginatedBySearch(pageable, query);
//    }

}
