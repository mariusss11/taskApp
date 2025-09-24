package com.app.task.service;

import com.app.auth.model.User;
import com.app.auth.service.UserService;
import com.app.group.model.Group;
import com.app.security.jwt.JwtUtils;
import com.app.task.dto.TaskDTO;
import com.app.task.exception.InvalidTaskException;
import com.app.task.exception.TaskNotFoundException;
import com.app.task.model.ChangeTaskRequest;
import com.app.task.model.CreateTaskRequest;
import com.app.task.model.Task;
import com.app.task.model.TaskStatus;
import com.app.task.repository.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for managing {@link Task} entities.
 * <p>
 * Handles business logic for creating, retrieving, and updating tasks.
 * </p>
 */
@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final HttpServletRequest httpRequest;

    /**
     * Constructs a new {@link TaskService}.
     *
     * @param taskRepository the repository for task persistence
     * @param userService    the service for retrieving current users
     * @param jwtUtils       utility for working with JWT tokens
     * @param httpRequest    the current HTTP request
     */
    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService,
                       JwtUtils jwtUtils, HttpServletRequest httpRequest) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.httpRequest = httpRequest;
    }

    // -------------------------------------------------------------------------
    // 🔹 RETRIEVE TASKS
    // -------------------------------------------------------------------------

    /**
     * Retrieves a {@link Task} by ID.
     *
     * @param taskId the ID of the task
     * @return the corresponding {@link Task}
     * @throws TaskNotFoundException if no task is found
     */
    public Task getItem(int taskId) {
        log.info("Returning the item with the id: {}", taskId);
        return getById(taskId);
    }

    /**
     * Retrieves all tasks in the system (admin only).
     *
     * @return a list of all {@link TaskDTO} objects
     */
    public List<TaskDTO> getAllTasks() {
        log.info("Returning all tasks as DTOs");
        return TaskDTO.toDtos(taskRepository.findAll());
    }

    /**
     * Retrieves all enabled tasks.
     *
     * @return a list of enabled {@link TaskDTO} objects
     */
    public List<TaskDTO> getAllEnabledTasks() {
        log.info("Returning all enabled tasks as DTOs");
        return TaskDTO.toDtos(taskRepository.findAllByIsEnabledTrue());
    }

    /**
     * Retrieves all enabled tasks created by a specific user.
     *
     * @param userId the ID of the user
     * @return a list of {@link TaskDTO} objects created by the user
     */
    public List<TaskDTO> getUsersTasks(int userId) {
        log.info("Returning the tasks created by the user with id #{}", userId);
        List<Task> allByIsEnabledTrueAndUserId =
                taskRepository.findAllByIsEnabledTrueAndUserId(userId);
        return TaskDTO.toDtos(allByIsEnabledTrueAndUserId);
    }

    /**
     * Retrieves a task by ID and returns the DTO of it.
     *
     * @param taskId the ID of the task
     * @return the corresponding {@link TaskDTO}
     * @throws TaskNotFoundException if no task is found
     */
    public TaskDTO getTask(int taskId) {
        return TaskDTO.toDto(getTaskById(taskId));
    }

    // -------------------------------------------------------------------------
    // 🔹 CREATE & SAVE TASKS
    // -------------------------------------------------------------------------

    /**
     * Creates a new task for the currently logged-in user.
     *
     * @param request the request object containing task details
     * @return the newly created {@link Task}
     * @throws InvalidTaskException if a task with the same title already exists
     */
    public Task createTask(CreateTaskRequest request) {
        if (existsTask(request.getTitle())) {
            throw new InvalidTaskException("The item is already in the library");
        }

        User user = userService.getCurrentLoggedInUser();

        Task newTask = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .deadline(request.getDeadline())
                .group(Group.builder().id(request.getGroupId()).build())
                .user(user)
                .status(TaskStatus.NOT_DONE.dbValue())
                .isEnabled(true)
                .build();

        log.info("Saving the new task: {}", newTask);
        return taskRepository.save(newTask);
    }

    // -------------------------------------------------------------------------
    // 🔹 UPDATE TASKS
    // -------------------------------------------------------------------------

    /**
     * Changes the status of a task.
     *
     * @param request contains the task ID and the new status
     * @return the updated {@link Task}
     * @throws TaskNotFoundException   if the task does not exist
     * @throws IllegalArgumentException if the new status is invalid
     */
    public Task changeTaskStatus(ChangeTaskRequest request) {
        log.info("Changing task {} status to {}", request.getTaskId(), request.getNewStatus());

        Task taskToChange = getTaskById(request.getTaskId());
        TaskStatus.isValid(request.getNewStatus());

        taskToChange.setStatus(request.getNewStatus());
        return getById(request.getTaskId());
    }

    // -------------------------------------------------------------------------
    // 🔹 INTERNAL HELPERS
    // -------------------------------------------------------------------------

    /**
     * Checks if a task already exists by its title.
     *
     * @param title the title of the task
     * @return {@code true} if the task exists, {@code false} otherwise
     */
    private boolean existsTask(String title) {
        return taskRepository.findByTitle(title).isPresent();
    }

    /**
     * Retrieves a task by ID.
     *
     * @param taskId the ID of the task
     * @return the corresponding {@link Task}
     * @throws TaskNotFoundException if no task is found
     */
    public Task getById(int taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Item was not found"));
    }

    /**
     * Retrieves a {@link Task} entity by ID (internal usage).
     *
     * @param taskId the ID of the task
     * @return the {@link Task} entity
     * @throws TaskNotFoundException if the task does not exist
     */
    private Task getTaskById(int taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }
}
