package com.example.taskmanagement.controller;

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
    public List<Task> getAll() {
        return taskService.getAllTasks();
    }

    @PostMapping("/add")
    public String add(@RequestBody @Valid TaskRequest request) {
        taskService.createTask(request);
        return "Thêm thành công!";
    }

    @PutMapping("/assign")
    public String assign(@RequestParam Integer taskId, @RequestParam Integer userId) {
        taskService.assignTask(taskId, userId);
        return "Giao việc thành công!";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return "Xóa thành công!";
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
    public Task updateStatus(@PathVariable Integer id, @RequestParam String status) {
        return taskService.updateTaskStatus(id, status);
    }
}