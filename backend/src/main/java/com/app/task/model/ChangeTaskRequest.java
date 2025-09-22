package com.app.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeTaskRequest {
    private int taskId;
    private String newStatus;
}
