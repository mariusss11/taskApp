package com.app.group.controller;

import com.app.group.model.CreateGroupRequest;
import com.app.group.model.Group;
import com.app.group.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing groups.
 * <p>
 * Provides endpoints to create a new group and retrieve all existing groups.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    /**
     * Constructs a GroupController with the required GroupService.
     *
     * @param groupService Service layer for managing groups
     */
    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // -------------------------------------------------------------------------
    // 🔹 GET GROUPS
    // -------------------------------------------------------------------------

    /**
     * Retrieves all groups.
     *
     * @return ResponseEntity containing a list of all Group objects
     */
    @GetMapping("/all")
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    // -------------------------------------------------------------------------
    // 🔹 CREATE GROUP
    // -------------------------------------------------------------------------

    /**
     * Creates a new group.
     *
     * @param request DTO containing group creation details
     * @return ResponseEntity containing the created Group
     */
    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody CreateGroupRequest request) {
        return ResponseEntity.ok(groupService.createGroup(request));
    }
}
