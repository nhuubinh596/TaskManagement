package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.request.RegisterRequest;
import com.example.taskmanagement.dto.response.ApiResponse;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.taskmanagement.dto.request.LoginRequest;
import com.example.taskmanagement.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.taskmanagement.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest request) {
        User result = userService.register(request);

        return ApiResponse.<User>builder()
                .result(result)
                .message("Đăng ký thành công!")
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return ApiResponse.<String>builder()
                .result(token)
                .message("Đăng nhập thành công!")
                .build();
    }
}