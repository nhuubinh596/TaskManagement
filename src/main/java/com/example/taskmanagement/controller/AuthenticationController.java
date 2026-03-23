package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.request.LoginRequest;
import com.example.taskmanagement.dto.request.RegisterRequest;
import com.example.taskmanagement.dto.response.ApiResponse;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.service.UserService;
import com.example.taskmanagement.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "1. Authentication", description = "Các API Đăng ký và Đăng nhập")
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
    @Operation(summary = "Đăng ký tài khoản mới", description = "Truyền vào username, email và password")
    public ApiResponse<User> register(@RequestBody RegisterRequest request) {
        User result = userService.register(request);
        return ApiResponse.<User>builder()
                .result(result)
                .message("Đăng ký thành công!")
                .build();
    }

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập hệ thống", description = "Trả về JWT token nếu thành công")
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