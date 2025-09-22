package com.app.task.model;

import com.app.task.exception.InvalidTaskStatusException;

import java.util.Arrays;

public enum TaskStatus {
    DONE,
    IN_PROCESS,
    NOT_DONE,
    ;


    /**
     * Converts the enum value to a lowercase string for database storage.
     * @return the lowercase name of the enum constant.
     */
    public String dbValue() {
        return this.name().toLowerCase();
    }

    public static boolean isValid(String taskStatus) {
        if (taskStatus == null) {
            throw new InvalidTaskStatusException("Task status cannot be null");
        }

        boolean exists = Arrays.stream(TaskStatus.values())
                .anyMatch(e -> e.name().equals(taskStatus));

        if (!exists) {
            throw new InvalidTaskStatusException("There is no status like this: " + taskStatus);
        }

        return true;
    }


}
