package com.app.task.controller;

import com.app.auth.service.CustomUserDetailsService;
import com.app.task.dto.TaskDTO;
import com.app.task.model.ChangeTaskRequest;
import com.app.task.model.CreateTaskRequest;
import com.app.task.model.Task;
import com.app.task.service.TaskService;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getItemById(@PathVariable int taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<Task>> getTasksByQuery(@RequestParam(name = "query") String searchQuery) {
//        return ResponseEntity.ok(taskService.getTasksByQuery(searchQuery));
//    }

//    @GetMapping("/search/pagination")
//    public PagedResponse<Task> getAllTasksPaginated(Pageable pageable, @RequestParam String query) {
//        return itemService.getAllEnabledTasksPaginatedBySearch(pageable, query);
//    }

    @GetMapping("/users/{userId}/tasks")
    public ResponseEntity<List<TaskDTO>> getUserTasks(@PathVariable int userId) {
        return ResponseEntity.ok(taskService.getUsersTasks(userId));
    }

    @GetMapping("/allEnabled")
    public ResponseEntity<List<TaskDTO>> getAllEnabledTasks() {
        return ResponseEntity.ok(taskService.getAllEnabledTasks());
    }

    @PutMapping("/changeStatus")
    public ResponseEntity<Task> changeTaskStatus(@RequestBody ChangeTaskRequest request) {
        return ResponseEntity.ok(taskService.changeTaskStatus(request));
    }


}
