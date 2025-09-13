package com.app.task.service;

import com.app.auth.model.User;
import com.app.auth.repository.UserRepository;
import com.app.auth.service.UserService;
import com.app.group.model.Group;
import com.app.security.jwt.JwtUtils;
import com.app.task.dto.CreateTaskRequest;
import com.app.task.exception.InvalidTaskException;
import com.app.task.exception.TaskNotFoundException;
import com.app.task.model.Task;
import com.app.task.model.TaskStatus;
import com.app.task.repository.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final HttpServletRequest httpRequest;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService, JwtUtils jwtUtils, HttpServletRequest httpRequest) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.httpRequest = httpRequest;
    }

//    public Task getItem(GetTaskRequest request) {
//        checkItemType(request.getItemType());
//        return getItemByTitleAndTypeAndAuthor(request.getTitle(), request.getItemType(), request.getAuthorName());
//    }

    public Task getItem(int taskId) {
        log.info("Returning the item with the id: {}", taskId);
        return getById(taskId);
    }

    /**
     * Method just for the admin and librarian
     * @return all the items
     */
    public List<Task> getAllTasks() {
        log.info("Returning all tasks");
        return taskRepository.findAll();
    }

    /**
     * Method for the user
     * @return all the enabled items
     */
    public List<Task> getAllEnabledTasks() {
        return taskRepository.findAllByIsEnabledTrue();
    }

    /**
     * Method to update the item
     * @param item the item that got updated
     */
    public void saveItemVoid(Task item) {
        taskRepository.save(item);
    }

    public Task createTask(CreateTaskRequest request) {
        if (existsTask(request.getTitle()))
            throw new InvalidTaskException("The item is already in the library");

        User user = userService.getCurrentLoggedInUser();

        Task newTask = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .deadline(request.getDeadline())
                .group(Group.builder().id(request.getGroupId()).build())
                .user(user)
                .status(TaskStatus.NOT_DONE.dbValue())
                .build();

        log.info("Saving the new task: {}", newTask);
        // save the item
        return taskRepository.save(newTask);
    }

    private boolean existsTask(String title) {
        return taskRepository.findByTitle(title).isPresent();
    }

//    public String removeItemFromLibrary(DisableItemRequest request) {
//        log.info("Trying removing item with title: {}", request.getTitle());
//        Task itemToDisable =
//                getItemByTitleAndTypeAndAuthor(request.getTitle(), request.getItemType(), request.getAuthor());
//        // check if the item is borrowed
//        if (!itemToDisable.isAvailable())
//            throw new InvalidItemException("Cannot remove a borrowed item: " + itemToDisable);
//
//        itemToDisable.setEnabled(false);
//        taskRepository.save(itemToDisable);
//        // send email to the admin
//        producer.sendRequestToDeleteItemFromBorrowTable(TaskDTO.builder()
//                .taskId(itemToDisable.getId())
//                .title(itemToDisable.getTitle())
//                .itemType(itemToDisable.getItemType())
//                .author(itemToDisable.getAuthor())
//                .yearPublished(itemToDisable.getYearPublished())
//                .build());
//        return "The item was disabled successfully";
//    }

    public Task getById(int taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Item was not found"));
    }


    String getAuthToken() {
        String token = jwtUtils.getToken(httpRequest.getHeader("Authorization"));
        if (token == null)
            throw new IllegalStateException("Authorization token is missing or invalid");
        return token;
    }


    public Task getTask(int taskId) {
        return getTaskById(taskId);
    }

    private Task getTaskById(int taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }
}
