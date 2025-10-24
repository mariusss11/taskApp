package com.app.config;

import com.app.group.exception.GroupNotFoundException;
import com.app.task.exception.InvalidTaskException;
import com.app.task.exception.TaskNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleGroupNotFound(GroupNotFoundException ex) {
        log.error("Group not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Group not found", ex.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTaskNotFound(TaskNotFoundException ex) {
        log.error("Task not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Task Not Found", ex.getMessage());
    }

    @ExceptionHandler(InvalidTaskException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTask(InvalidTaskException ex) {
        log.error("Invalid task: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Task", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("Unexpected error: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error", "An unexpected error occurred");
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", error,
                "message", message
        );
        return ResponseEntity.status(status).body(body);
    }
}
