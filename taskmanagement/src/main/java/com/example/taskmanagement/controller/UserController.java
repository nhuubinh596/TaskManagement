package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.response.ApiResponse;
import com.example.taskmanagement.dto.response.UserResponse;
import com.example.taskmanagement.entity.Role;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        List<UserResponse> result = users.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());

        return ApiResponse.<List<UserResponse>>builder()
                .result(result)
                .build();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Lỗi: Username '" + user.getUsername() + "' đã tồn tại!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Lỗi: Email '" + user.getEmail() + "' đã được sử dụng!");
        }

        return userRepository.save(user);
    }
}
