package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.response.ApiResponse;
import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.dto.request.TaskRequest;
import com.example.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ApiResponse<List<Task>> getAll() {
        ApiResponse<List<Task>> response = new ApiResponse<>();
        response.setResult(taskService.getAllTasks());
        return response;
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
    public ApiResponse<Task> assign(@RequestParam Integer taskId, @RequestParam Integer userId) {
        Task task = taskService.assignTask(taskId, userId);

        return ApiResponse.<Task>builder()
                .message("Giao việc thành công!")
                .result(task)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> delete(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return new ApiResponse<>(1000, "Xóa thành công!", null);
    }

    @GetMapping("/project/{id}")
    public List<Task> getTasksByProject(@PathVariable("id") Integer projectId) {
        return taskService.getTasksByProject(projectId);
    }

    @GetMapping("/user/{id}")
    public List<Task> getTasksByUser(@PathVariable("id") Integer userId) {
        return taskService.getTasksByUser(userId);
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Task> updateStatus(@PathVariable Integer id, @RequestParam String status) {
        return new ApiResponse<>(1000, "Cập nhật trạng thái thành công", taskService.updateTaskStatus(id, status));
    }
}