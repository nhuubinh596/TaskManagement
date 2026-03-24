package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.request.ProjectRequest;
import com.example.taskmanagement.dto.response.ApiResponse;
import com.example.taskmanagement.entity.Project;
import com.example.taskmanagement.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "2. Project Management", description = "Quản lý thông tin Dự án")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả dự án")
    public ApiResponse<List<Project>> getAllProjects() {
        return ApiResponse.<List<Project>>builder()
                .result(projectService.getAllProjects())
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "Tạo dự án mới", description = "Yêu cầu quyền MANAGER")
    public ApiResponse<Project> createProject(@Valid @RequestBody ProjectRequest request) {
        return ApiResponse.<Project>builder()
                .result(projectService.createProject(request))
                .message("Create project successfully")
                .build();
    }
}