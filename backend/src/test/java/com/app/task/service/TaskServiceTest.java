package com.app.task.service;

import com.app.auth.model.User;
import com.app.auth.service.UserService;
import com.app.group.model.Group;
import com.app.group.service.GroupService;
import com.app.security.jwt.JwtUtils;
import com.app.task.dto.TaskDTO;
import com.app.task.exception.InvalidTaskException;
import com.app.task.exception.TaskNotFoundException;
import com.app.task.model.CreateTaskRequest;
import com.app.task.model.Task;
import com.app.task.model.TaskStatus;
import com.app.task.repository.TaskRepository;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ✅ Unit tests for {@link TaskService}.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private UserService userService;
    @Mock private GroupService groupService;
    @Mock private JwtUtils jwtUtils;
    @Mock private HttpServletRequest httpRequest;

    @InjectMocks
    private TaskService taskService;

    private User mockUser;
    private Group mockGroup;
    private Task mockTask;

    @BeforeEach
    void setUp() {
        // Initialize basic mock objects
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setName("testuser");

        mockGroup = new Group();
        mockGroup.setId(100);
        mockGroup.setName("Dev Team");

        mockTask = Task.builder()
                .id(1)
                .title("Test Task")
                .description("Some description")
                .createdAt(LocalDateTime.now())
                .deadline(LocalDate.now().plusDays(1))
                .group(mockGroup)
                .user(mockUser)
                .status(TaskStatus.NOT_DONE.dbValue())
                .isEnabled(true)
                .build();
    }

    // -------------------------------------------------------------------------
    // 🔹 getTask()
    // -------------------------------------------------------------------------
    @Test
    void getTask_ShouldReturnTask_WhenExists() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(mockTask));

        TaskDTO result = taskService.getTask(1);

        assertEquals("Test Task", result.getTitle());
        verify(taskRepository).findById(1);
    }

    @Test
    void getTask_ShouldThrow_WhenNotFound() {
        when(taskRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(99));
    }

    // -------------------------------------------------------------------------
    // 🔹 getAllEnabledTasks()
    // -------------------------------------------------------------------------
    @Test
    void getAllEnabledTasks_ShouldReturnListOfTasks() {
        when(taskRepository.findAllByIsEnabledTrue()).thenReturn(List.of(mockTask));

        var tasks = taskService.getAllEnabledTasks();

        assertEquals(1, tasks.size());
        verify(taskRepository).findAllByIsEnabledTrue();
    }

    // -------------------------------------------------------------------------
    // 🔹 createTask()
    // -------------------------------------------------------------------------
    @Test
    void createTask_ShouldCreateNewTask() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("New Task");
        request.setDescription("Task description");
        request.setDeadline(LocalDate.now().plusDays(3));
        request.setGroupId(100);

        when(userService.getCurrentLoggedInUser()).thenReturn(mockUser);
        when(groupService.getGroup(100)).thenReturn(mockGroup);
        when(taskRepository.findByTitle("New Task")).thenReturn(Optional.empty());
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        TaskDTO result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createTask_ShouldThrow_WhenTitleIsEmpty() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(" ");
        assertThrows(InvalidTaskException.class, () -> taskService.createTask(request));
    }

    @Test
    void createTask_ShouldThrow_WhenDuplicateTitle() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Test Task");

        when(taskRepository.findByTitle("Test Task")).thenReturn(Optional.of(mockTask));

        assertThrows(InvalidTaskException.class, () -> taskService.createTask(request));
    }

    // -------------------------------------------------------------------------
    // 🔹 setTaskEnabled()
    // -------------------------------------------------------------------------
    @Test
    void setTaskEnabled_ShouldUpdateEnabledFlag() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(mockTask));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        Task result = taskService.setTaskEnabled(1, false);

        assertFalse(result.isEnabled());
        verify(taskRepository).save(mockTask);
    }

    // -------------------------------------------------------------------------
    // 🔹 deleteTask()
    // -------------------------------------------------------------------------
    @Test
    void deleteTask_ShouldDelete_WhenExists() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(mockTask));

        taskService.deleteTask(1);

        verify(taskRepository).delete(mockTask);
    }

    @Test
    void deleteTask_ShouldThrow_WhenNotFound() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1));
    }
}
