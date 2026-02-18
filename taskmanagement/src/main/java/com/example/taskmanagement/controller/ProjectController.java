package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.request.ProjectRequest;
import com.example.taskmanagement.dto.response.ApiResponse;
import com.example.taskmanagement.entity.Project;
import com.example.taskmanagement.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ApiResponse<List<Project>> getAllProjects() {
        return ApiResponse.<List<Project>>builder()
                .result(projectService.getAllProjects())
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public ApiResponse<Project> createProject(@RequestBody ProjectRequest request) {
        return ApiResponse.<Project>builder()
                .result(projectService.createProject(request))
                .message("Tạo dự án thành công!")
                .build();
    }
}