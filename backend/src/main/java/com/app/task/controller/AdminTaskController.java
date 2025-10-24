package com.app.task.controller;

import com.app.task.dto.TaskDTO;
import com.app.task.model.Task;
import com.app.task.repository.TaskRepository;
import com.app.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for administrative task management operations.
 * <p>
 * Provides endpoints to:
 * - Retrieve all tasks (including disabled)
 * - Disable or enable a task
 * - Permanently delete a task
 * </p>
 *
 * Accessible only by users with administrative privileges.
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/tasks")
@RequiredArgsConstructor
public class AdminTaskController {

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    // -------------------------------------------------------------------------
    // 🔹 GET ALL TASKS (including disabled)
    // -------------------------------------------------------------------------

    /**
     * Retrieves all tasks in the system, including disabled ones.
     *
     * @return ResponseEntity containing a list of all TaskDTOs
     */
    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        log.info("[ADMIN] Fetching all tasks (including disabled)");
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // -------------------------------------------------------------------------
    // 🔹 DISABLE / ENABLE TASK
    // -------------------------------------------------------------------------

    /**
     * Enables or disables a specific task.
     *
     * @param taskId ID of the task to update
     * @param enabled true to enable, false to disable
     * @return ResponseEntity with the updated TaskDTO
     */
    @PutMapping("/{taskId}/enable")
    public ResponseEntity<TaskDTO> setTaskEnabled(
            @PathVariable int taskId,
            @RequestParam boolean enabled
    ) {
        log.info("[ADMIN] Changing enabled state for task ID {} -> {}", taskId, enabled);
        Task updatedTask = taskService.setTaskEnabled(taskId, enabled);

        updatedTask.setUpdatedAt(LocalDateTime.now());

        // save the updated time for the task
        taskRepository.save(updatedTask);

        return ResponseEntity.ok(TaskDTO.toDto(updatedTask));
    }

    // -------------------------------------------------------------------------
    // 🔹 DELETE TASK
    // -------------------------------------------------------------------------

    /**
     * Permanently deletes a task by its ID.
     *
     * @param taskId ID of the task to delete
     * @return ResponseEntity with a confirmation message
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable int taskId) {
        log.info("[ADMIN] Deleting task ID {}", taskId);
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }
}
