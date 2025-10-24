package com.app.group.controller;

import com.app.group.exception.GroupNotFoundException;
import com.app.group.model.Group;
import com.app.group.repository.GroupRepository;
import com.app.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for administrative group management operations.
 *
 * Provides endpoints to:
 * - Retrieve all groups (including disabled)
 * - Enable or disable a group
 * - Permanently delete a group
 *
 * Accessible only by users with administrative privileges.
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/groups")
@RequiredArgsConstructor
public class AdminGroupController {

    private final GroupService groupService;
    private final GroupRepository groupRepository;

    // -------------------------------------------------------------------------
    // 🔹 GET ALL GROUPS (including disabled)
    // -------------------------------------------------------------------------

    /**
     * Retrieves all groups in the system, including disabled ones.
     *
     * @return ResponseEntity containing a list of all Groups
     */
    @GetMapping("/all")
    public ResponseEntity<List<Group>> getAllGroups() {
        log.info("[ADMIN] Fetching all groups (including disabled)");
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    // -------------------------------------------------------------------------
    // 🔹 DISABLE / ENABLE GROUP
    // -------------------------------------------------------------------------

    /**
     * Enables or disables a specific group.
     *
     * @param groupId ID of the group to update
     * @param enabled true to enable, false to disable
     * @return ResponseEntity with the updated GroupDTO
     */
    @PutMapping("/{groupId}/enable")
    public ResponseEntity<Group> setGroupEnabled(
            @PathVariable int groupId,
            @RequestParam boolean enabled
    ) {
        log.info("[ADMIN] Changing enabled state for group ID {} -> {}", groupId, enabled);
        Group updatedGroup = groupService.setGroupEnabled(groupId, enabled);
        return ResponseEntity.ok(updatedGroup);
    }

    // -------------------------------------------------------------------------
    // 🔹 DELETE GROUP
    // -------------------------------------------------------------------------

    /**
     * Permanently deletes a group by its ID.
     *
     * @param groupId ID of the group to delete
     * @return ResponseEntity with a confirmation message
     */
    @DeleteMapping("/{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable int groupId) {
        log.info("[ADMIN] Deleting group ID {}", groupId);
        groupService.deleteGroup(groupId);
        return ResponseEntity.ok("Group deleted successfully");
    }
}
