package com.example.taskmanagement.dto.request;

import com.example.taskmanagement.util.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String role;
}