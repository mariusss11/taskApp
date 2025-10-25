package com.app.group.controller;

import com.app.group.model.CreateGroupRequest;
import com.app.group.model.Group;
import com.app.group.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GroupController}.
 */
class GroupControllerTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    private Group mockGroup;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockGroup = new Group();
        mockGroup.setId(1);
        mockGroup.setName("Dev Team");
    }

    // -------------------------------------------------------------------------
    // 🔹 getAllGroups()
    // -------------------------------------------------------------------------
    @Test
    void getAllGroups_ShouldReturnListOfGroups() {
        when(groupService.getAllGroups()).thenReturn(List.of(mockGroup));

        ResponseEntity<List<Group>> response = groupController.getAllGroups();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Dev Team", response.getBody().get(0).getName());
        verify(groupService).getAllGroups();
    }

    // -------------------------------------------------------------------------
    // 🔹 getUsersGroups()
    // -------------------------------------------------------------------------
    @Test
    void getUsersGroups_ShouldReturnGroupsForUser() {
        when(groupService.getAllUsersGroups(1)).thenReturn(List.of(mockGroup));

        ResponseEntity<List<Group>> response = groupController.getUsersGroups(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Dev Team", response.getBody().get(0).getName());
        verify(groupService).getAllUsersGroups(1);
    }

    // -------------------------------------------------------------------------
    // 🔹 createGroup()
    // -------------------------------------------------------------------------
    @Test
    void createGroup_ShouldReturnCreatedGroup() {
        CreateGroupRequest request = new CreateGroupRequest();
        request.setName("QA Team");

        Group created = new Group();
        created.setId(2);
        created.setName("QA Team");

        when(groupService.createGroup(any(CreateGroupRequest.class))).thenReturn(created);

        ResponseEntity<Group> response = groupController.createGroup(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("QA Team", response.getBody().getName());
        verify(groupService).createGroup(request);
    }
}
