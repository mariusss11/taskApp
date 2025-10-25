package com.app.group.service;

import com.app.auth.model.User;
import com.app.auth.service.UserService;
import com.app.group.model.CreateGroupRequest;
import com.app.group.model.Group;
import com.app.group.repository.GroupRepository;
import com.app.security.jwt.JwtUtils;
import com.app.task.model.Task;
import com.app.task.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ✅ Unit tests for {@link GroupService}.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GroupServiceTest {

    @Mock private GroupRepository groupRepository;
    @Mock private TaskService taskService;
    @Mock private UserService userService;
    @Mock private JwtUtils jwtUtils;

    @InjectMocks
    private GroupService groupService;

    private static final String AUTH_TOKEN = "valid.token";

    // Mock objects
    private Group mockGroup;
    private Task mockTask;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        mockGroup = new Group();
        mockGroup.setId(1);
        mockGroup.setName("Dev Team");

        mockTask = new Task();
        mockTask.setId(10);
        mockTask.setTitle("Setup CI Pipeline");
        mockTask.setDescription("Configure GitHub Actions for CI/CD");

        when(jwtUtils.getToken(anyString())).thenReturn(AUTH_TOKEN);
    }

    // -------------------------------------------------------------------------
    // 🔹 getAllGroups()
    // -------------------------------------------------------------------------
    @Test
    void getAllGroups_ShouldReturnAllGroups() {
        when(groupRepository.findAll()).thenReturn(List.of(mockGroup));

        List<Group> result = groupService.getAllGroups();

        assertEquals(1, result.size());
        assertEquals("Dev Team", result.get(0).getName());
        verify(groupRepository).findAll();
    }

    // -------------------------------------------------------------------------
    // 🔹 createGroup()
    // -------------------------------------------------------------------------
    @Test
    void createGroup_ShouldSaveAndReturnNewGroup() {
        CreateGroupRequest request = new CreateGroupRequest();
        request.setName("QA Team");

        Group saved = new Group();
        saved.setId(2);
        saved.setName("QA Team");

        when(userService.getCurrentLoggedInUser()).thenReturn(new User());

        when(groupRepository.save(any(Group.class))).thenReturn(saved);

        Group result = groupService.createGroup(request);

        assertNotNull(result);
        assertEquals("QA Team", result.getName());
        verify(groupRepository).save(any(Group.class));
    }

    // -------------------------------------------------------------------------
    // 🔹 getGroup()
    // -------------------------------------------------------------------------
    @Test
    void getGroup_ShouldReturnGroup_WhenExists() {
        when(groupRepository.findById(1)).thenReturn(Optional.of(mockGroup));

        Group result = groupService.getGroup(1);

        assertEquals("Dev Team", result.getName());
        verify(groupRepository).findById(1);
    }

    @Test
    void getGroup_ShouldThrow_WhenNotFound() {
        when(groupRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> groupService.getGroup(99));
    }

    // -------------------------------------------------------------------------
    // 🔹 getAllGroupsWithTasks()
    // -------------------------------------------------------------------------
    @Test
    void getAllGroupsWithTasks_ShouldAttachTasksToEachGroup() {
        when(groupRepository.findAll()).thenReturn(List.of(mockGroup));
        when(taskService.getTasksByGroupId(1)).thenReturn(List.of(mockTask));

        List<Group> result = groupService.getAllGroupsWithTasks();

        assertEquals(1, result.size());
        assertEquals("Dev Team", result.get(0).getName());
        assertEquals(1, result.get(0).getTasks().size());
        assertEquals("Setup CI Pipeline", result.get(0).getTasks().get(0).getTitle());

        verify(groupRepository).findAll();
        verify(taskService).getTasksByGroupId(1);
    }

    @Test
    void getAllGroupsWithTasks_ShouldHandleEmptyTaskLists() {
        when(groupRepository.findAll()).thenReturn(List.of(mockGroup));
        when(taskService.getTasksByGroupId(1)).thenReturn(List.of());

        List<Group> result = groupService.getAllGroupsWithTasks();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getTasks().isEmpty());
        verify(taskService).getTasksByGroupId(1);
    }

    // -------------------------------------------------------------------------
    // 🔹 getTasksByGroupId()
    // -------------------------------------------------------------------------
    @Test
    void getTasksByGroupId_ShouldReturnTasksFromRepository() {
        when(taskService.getTasksByGroupId(1)).thenReturn(List.of(mockTask));

        List<Task> tasks = taskService.getTasksByGroupId(1);

        assertEquals(1, tasks.size());
        assertEquals("Setup CI Pipeline", tasks.get(0).getTitle());
    }
}
