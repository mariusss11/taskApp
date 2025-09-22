package com.app.task.dto;

import com.app.auth.model.User;
import com.app.group.model.Group;
import com.app.task.model.Task;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Integer id;
    private String groupName;
    private String title;
    private String description;
    private LocalDate deadline;
    private String status;
    private boolean isEnabled;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public static TaskDTO toDto(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .groupName(task.getGroup().getName())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .status(task.getStatus())
                .isEnabled(task.isEnabled())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    public static List<TaskDTO> toDtos(List<Task> taskList) {
        List<TaskDTO> taskDTOList = new ArrayList<>();
        for (Task task : taskList) {
            taskDTOList.add(toDto(task));
        }
        return taskDTOList;
    }
}
