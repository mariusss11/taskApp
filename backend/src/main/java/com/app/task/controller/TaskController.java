package com.app.task.controller;

import com.app.task.dto.CreateTaskRequest;
import com.app.task.model.Task;
import com.app.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Task> getItemById(@PathVariable int itemId) {
        return ResponseEntity.ok(taskService.getTask(itemId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
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


    @GetMapping("/allEnabled")
    public ResponseEntity<List<Task>> getAllEnabledTasks() {
        return ResponseEntity.ok(taskService.getAllEnabledTasks());
    }


}
