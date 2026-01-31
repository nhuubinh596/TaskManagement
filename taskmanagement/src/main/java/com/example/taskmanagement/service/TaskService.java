package com.example.taskmanagement.service;

import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.repository.ProjectRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.dto.request.TaskRequest;
import com.example.taskmanagement.util.TaskStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private UserRepository userRepo;

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public void createTask(TaskRequest request) {
        Task task = new Task();
        BeanUtils.copyProperties(request, task);
        task.setProject(projectRepo.findById(request.getProjectId()).orElse(null));
        if (request.getUserId() != null) {
            task.setUser(userRepo.findById(request.getUserId()).orElse(null));
        }
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }
        taskRepo.save(task);
    }

    public void assignTask(Integer taskId, Integer userId) {
        Task task = taskRepo.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setUser(userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        taskRepo.save(task);
    }

    public void deleteTask(Integer id) {
        taskRepo.deleteById(id);
    }

    public List<Task> getTasksByProject(Integer projectId) {
        return taskRepo.findByProjectId(projectId);
    }

    public List<Task> getTasksByUser(Integer userId) {
        return taskRepo.findByUserId(userId);
    }
}