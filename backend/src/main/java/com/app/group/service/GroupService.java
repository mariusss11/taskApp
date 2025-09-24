package com.app.group.service;

import com.app.auth.model.User;
import com.app.auth.service.impl.UserServiceImpl;
import com.app.group.exception.InvalidGroupException;
import com.app.group.model.CreateGroupRequest;
import com.app.group.model.Group;
import com.app.group.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for managing {@link Group} entities.
 * <p>
 * Handles business logic for creating groups and retrieving existing groups.
 * </p>
 */
@Slf4j
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserServiceImpl userService;

    /**
     * Constructs a GroupService with the required dependencies.
     *
     * @param groupRepository Repository for group persistence
     * @param userService     Service for retrieving current user information
     */
    public GroupService(GroupRepository groupRepository, UserServiceImpl userService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }

    // -------------------------------------------------------------------------
    // 🔹 GET GROUPS
    // -------------------------------------------------------------------------

    /**
     * Retrieves all groups from the database.
     *
     * @return List of all Group objects
     */
    public List<Group> getAllGroups() {
        log.info("Returning all groups");
        return groupRepository.findAll();
    }

    // -------------------------------------------------------------------------
    // 🔹 CREATE GROUP
    // -------------------------------------------------------------------------

    /**
     * Creates a new group.
     *
     * @param request DTO containing group creation details (name, description)
     * @return The newly created Group object
     * @throws InvalidGroupException if a group with the same name already exists
     */
    public Group createGroup(CreateGroupRequest request) {
        log.info("Creating a new group with the request: {}", request);
        if (existsGroup(request.getName())) {
            throw new InvalidGroupException("There is already a group with this name");
        }

        User user = userService.getCurrentLoggedInUser();

        Group newGroup = Group.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        return groupRepository.save(newGroup);
    }

    // -------------------------------------------------------------------------
    // 🔹 INTERNAL HELPERS
    // -------------------------------------------------------------------------

    /**
     * Checks if a group with the given name already exists.
     *
     * @param name Name of the group
     * @return true if a group with the same name exists, false otherwise
     */
    private boolean existsGroup(String name) {
        return groupRepository.findByName(name).isPresent();
    }
}
