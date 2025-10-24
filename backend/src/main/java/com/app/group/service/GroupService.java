package com.app.group.service;

import com.app.auth.model.User;
import com.app.auth.service.UserService;
import com.app.group.exception.GroupNotFoundException;
import com.app.group.exception.InvalidGroupException;
import com.app.group.model.CreateGroupRequest;
import com.app.group.model.Group;
import com.app.group.repository.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for managing {@link Group} entities.
 * <p>
 * Handles business logic for creating and retrieving groups.
 * </p>
 */
@Slf4j
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;

    /**
     * Constructs a {@link GroupService} with required dependencies.
     *
     * @param groupRepository repository for group persistence
     * @param userService     service for retrieving the current logged-in user
     */
    public GroupService(GroupRepository groupRepository, UserService userService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }

    // -------------------------------------------------------------------------
    // 🔹 RETRIEVE GROUPS
    // -------------------------------------------------------------------------

    /**
     * Retrieves all groups from the database.
     *
     * @return list of all {@link Group} entities
     */
    public List<Group> getAllGroups() {
        log.debug("Fetching all groups");
        return groupRepository.findAll();
    }

    public Group getGroup(int groupId) {
        log.debug("Fetching group with ID: {}", groupId);
        return findGroupById(groupId);
    }

    // -------------------------------------------------------------------------
    // 🔹 CREATE GROUP
    // -------------------------------------------------------------------------

    /**
     * Creates a new group for the currently logged-in user.
     *
     * @param request DTO containing group creation details (name, description)
     * @return the newly created {@link Group}
     * @throws InvalidGroupException if a group with the same name already exists
     */
    @Transactional
    public Group createGroup(CreateGroupRequest request) {
        validateGroupRequest(request);

        User user = userService.getCurrentLoggedInUser();

        Group newGroup = Group.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        Group savedGroup = groupRepository.save(newGroup);
        log.info("Created new group '{}' (ID: {}) by user ID: {}", savedGroup.getName(), savedGroup.getId(), user.getId());

        return savedGroup;
    }

    // -------------------------------------------------------------------------
    // 🔹 INTERNAL HELPERS
    // -------------------------------------------------------------------------

    /**
     * Validates the group creation request.
     *
     * @param request the request object
     */
    private void validateGroupRequest(CreateGroupRequest request) {
        if (request == null) {
            throw new InvalidGroupException("Request cannot be null");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new InvalidGroupException("Group name cannot be empty");
        }

        if (existsGroup(request.getName().trim())) {
            throw new InvalidGroupException("A group with this name already exists");
        }
    }

    /**
     * Checks if a group with the given name already exists.
     *
     * @param name name of the group
     * @return true if a group with the same name exists, false otherwise
     */
    public boolean existsGroup(String name) {
        return groupRepository.findByName(name).isPresent();
    }

    /**
     * Retrieves a group by ID or throws {@link GroupNotFoundException}.
     */
    private Group findGroupById(int groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found with ID: " + groupId));
    }


    public Group setGroupEnabled(int groupId, boolean enabled) {
        Group group = getGroup(groupId);

        group.setEnabled(enabled);
        groupRepository.save(group);

        log.info("Group ID {} is now {}", groupId, enabled ? "ENABLED" : "DISABLED");
        return group;
    }

    public void deleteGroup(int groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found with ID: " + groupId));

        groupRepository.delete(group);
        log.info("Deleted group ID {}", groupId);
    }

}
