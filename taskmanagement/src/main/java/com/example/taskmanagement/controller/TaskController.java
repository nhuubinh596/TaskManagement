package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.response.ApiResponse;
import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.dto.request.TaskRequest;
import com.example.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ApiResponse<List<Task>> getAllTasks() {
        return ApiResponse.<List<Task>>builder()
                .result(taskService.getMyTasks())
                .message("Lấy danh sách task của bạn thành công!")
                .build();
    }

    @PostMapping("/add")
    public ApiResponse<Task> add(@RequestBody @Valid TaskRequest request) {
        Task task = taskService.createTask(request);

        ApiResponse<Task> response = new ApiResponse<>();
        response.setMessage("Thêm thành công!");
        response.setResult(task);

        return response;
    }

    @PutMapping("/assign")
    public ApiResponse<Task> assign(@RequestParam Long taskId, @RequestParam Integer userId) {
        Task task = taskService.assignTask(taskId, userId);

        return ApiResponse.<Task>builder()
                .message("Giao việc thành công!")
                .result(task)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ApiResponse<>(1000, "Xóa thành công!", null);
    }

    @GetMapping("/project/{id}")
    public List<Task> getTasksByProject(@PathVariable("id") Long projectId) {
        return taskService.getTasksByProject(projectId);
    }

    @GetMapping("/user/{id}")
    public List<Task> getTasksByUser(@PathVariable("id") Integer userId) {
        return taskService.getTasksByUser(userId);
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Task> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return new ApiResponse<>(1000, "Cập nhật trạng thái thành công", taskService.updateTaskStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ApiResponse<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ApiResponse.<String>builder()
                .result("Xóa task thành công")
                .build();
    }
}