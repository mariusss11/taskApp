package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    private String name;
    private String email;
    private String password;

    private String role;

    public CreateUserRequest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}