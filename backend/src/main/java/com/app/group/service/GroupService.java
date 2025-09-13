package com.app.group.service;

import com.app.auth.model.User;
import com.app.auth.service.impl.UserServiceImpl;
import com.app.group.exception.InvalidGroupException;
import com.app.group.model.CreateGroupRequest;
import com.app.group.model.Group;
import com.app.group.repository.GroupRepository;
import com.app.task.exception.InvalidTaskException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserServiceImpl userService;

    public GroupService(GroupRepository groupRepository, UserServiceImpl userService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }


    public List<Group> getAllGroups() {
        log.info("Returning all groups");
        return groupRepository.findAll();
    }

    public Group createGroup(CreateGroupRequest request) {
        log.info("Creating a new group with the request: {}", request);
        if (existsGroup(request.getName()))
            throw new InvalidGroupException("There is already a group with this name");

        User user = userService.getCurrentLoggedInUser();

        Group newGroup = Group.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        return groupRepository.save(newGroup);
    }


    private boolean existsGroup(String name) {
        return groupRepository.findByName(name).isPresent();
    }

}
