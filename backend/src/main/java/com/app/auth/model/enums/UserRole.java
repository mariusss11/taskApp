package com.app.auth.model.enums;

import com.app.auth.exception.InvalidRoleException;

public enum UserRole {
    ADMIN,
    USER;

    public static UserRole toRole(String role) {
        switch (role) {
            case "USER" -> {
                return UserRole.USER;
            }
            case "ADMIN" -> {
                return UserRole.ADMIN;
            }
            default -> throw new InvalidRoleException("Invalid role: " + role);
        }
    }
}