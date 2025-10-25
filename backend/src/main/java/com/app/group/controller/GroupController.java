package com.app.group.controller;

import com.app.group.model.CreateGroupRequest;
import com.app.group.model.Group;
import com.app.group.service.GroupService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing {@link Group} entities.
 * <p>
 * Provides endpoints to create and retrieve groups.
 * </p>
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // -------------------------------------------------------------------------
    // 🔹 RETRIEVE GROUPS
    // -------------------------------------------------------------------------

    /**
     * Retrieves all groups.
     *
     * @return ResponseEntity containing a list of all {@link Group} objects
     */
    @GetMapping("/all")
    public ResponseEntity<List<Group>> getAllGroups() {
        log.debug("Fetching all groups");
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<List<Group>> getUsersGroups(
            @PathVariable int userId
    ) {
        log.debug("Fetching all user's with id {} groups", userId);
        return ResponseEntity.ok(groupService.getAllUsersGroups(userId));
    }

    // -------------------------------------------------------------------------
    // 🔹 CREATE GROUP
    // -------------------------------------------------------------------------

    /**
     * Creates a new group.
     *
     * @param request DTO containing group creation details
     * @return ResponseEntity containing the created {@link Group}
     */
    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        log.info("Received request to create group '{}'", request.getName());
        return ResponseEntity.ok(groupService.createGroup(request));
    }
}
