package com.app.task.model;

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
}
