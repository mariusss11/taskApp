package com.app.task.service;

import com.app.auth.model.User;
import com.app.auth.service.UserService;
import com.app.group.model.Group;
import com.app.group.service.GroupService;
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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for managing {@link Task} entities.
 * <p>
 * Provides business logic for creating, retrieving, and updating tasks.
 * </p>
 */
@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final GroupService groupService;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       UserService userService, GroupService groupService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.groupService = groupService;
    }

    // -------------------------------------------------------------------------
    // 🔹 RETRIEVE TASKS
    // -------------------------------------------------------------------------

    /**
     * Retrieves a {@link TaskDTO} by its ID.
     *
     * @param taskId the ID of the task
     * @return the task as a {@link TaskDTO}
     */
    public TaskDTO getTask(int taskId) {
        log.debug("Fetching task with ID: {}", taskId);
        return TaskDTO.toDto(findTaskById(taskId));
    }

    /**
     * Retrieves all tasks (admin only).
     *
     * @return a list of all {@link TaskDTO} objects
     */
    public List<TaskDTO> getAllTasks() {
        log.debug("Fetching all tasks");
        return TaskDTO.toDtos(taskRepository.findAll());
    }

    /**
     * Retrieves all enabled tasks.
     *
     * @return a list of enabled {@link TaskDTO} objects
     */
    public List<TaskDTO> getAllEnabledTasks() {
        log.debug("Fetching all enabled tasks");
        return TaskDTO.toDtos(taskRepository.findAllByIsEnabledTrue());
    }

    /**
     * Retrieves all enabled tasks created by a specific user.
     *
     * @param userId the ID of the user
     * @return a list of {@link TaskDTO} objects created by the user
     */
    public List<TaskDTO> getUserTasks(int userId) {
        log.debug("Fetching enabled tasks for user ID: {}", userId);
        return TaskDTO.toDtos(taskRepository.findAllByIsEnabledTrueAndUserId(userId));
    }

    // -------------------------------------------------------------------------
    // 🔹 CREATE TASKS
    // -------------------------------------------------------------------------

    /**
     * Creates a new task for the currently logged-in user.
     *
     * @param request the request containing task details
     * @return the newly created {@link TaskDTO}
     * @throws InvalidTaskException if a task with the same title already exists
     */
    @Transactional
    public TaskDTO createTask(CreateTaskRequest request) {
        validateCreateRequest(request);

        User user = userService.getCurrentLoggedInUser();


        Group group = groupService.getGroup(request.getGroupId());

        Task task = Task.builder()
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .deadline(request.getDeadline())
                .group(group)
                .user(user)
                .status(TaskStatus.NOT_DONE.dbValue())
                .isEnabled(true)
                .build();

        Task savedTask = taskRepository.save(task);
        log.info("Created new task with ID: {} for user: {}", savedTask.getId(), user.getId());

        return TaskDTO.toDto(savedTask);
    }

    // -------------------------------------------------------------------------
    // 🔹 UPDATE TASKS
    // -------------------------------------------------------------------------

    /**
     * Changes the status of a task.
     *
     * @param request contains the task ID and the new status
     * @return the updated {@link TaskDTO}
     */
    @Transactional
    public TaskDTO changeTaskStatus(ChangeTaskRequest request) {
        log.debug("Changing status of task {} to {}", request.getTaskId(), request.getNewStatus());

        Task task = findTaskById(request.getTaskId());
        TaskStatus.isValid(request.getNewStatus());

        task.setStatus(request.getNewStatus());
        taskRepository.save(task);

        log.info("Updated status of task {} to {}", task.getId(), task.getStatus());
        return TaskDTO.toDto(task);
    }

    // -------------------------------------------------------------------------
    // 🔹 INTERNAL HELPERS
    // -------------------------------------------------------------------------

    /**
     * Validates the create request and ensures title uniqueness.
     */
    private void validateCreateRequest(CreateTaskRequest request) {
        if (request == null) {
            throw new InvalidTaskException("Request cannot be null");
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new InvalidTaskException("Title cannot be empty");
        }

        if (existsTask(request.getTitle().trim())) {
            throw new InvalidTaskException("A task with the same title already exists");
        }
    }

    /**
     * Checks if a task already exists by its title.
     */
    private boolean existsTask(String title) {
        return taskRepository.findByTitle(title).isPresent();
    }

    /**
     * Retrieves a task by ID or throws {@link TaskNotFoundException}.
     */
    private Task findTaskById(int taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));
    }

// -------------------------------------------------------------------------
// 🔹 ADMIN METHODS
// -------------------------------------------------------------------------

    /**
     * Enables or disables a task by ID.
     *
     * @param taskId ID of the task
     * @param enabled true to enable, false to disable
     * @return the updated Task
     * @throws TaskNotFoundException if no task exists with the given ID
     */
    public Task setTaskEnabled(int taskId, boolean enabled) {
        Task task = findTaskById(taskId);
        task.setEnabled(enabled);
        return taskRepository.save(task);
    }

    /**
     * Permanently deletes a task by ID.
     *
     * @param taskId ID of the task
     * @throws TaskNotFoundException if no task exists with the given ID
     */
    public void deleteTask(int taskId) {
        Task task = findTaskById(taskId);
        taskRepository.delete(task);
        log.info("Deleted task with ID: {}", taskId);
    }

    public List<Task> getTasksByGroupId(int groupId) {
        return taskRepository.findByGroup_Id(groupId);
    }
}
