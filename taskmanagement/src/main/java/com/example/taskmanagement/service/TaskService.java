package com.example.taskmanagement.service;

import com.example.taskmanagement.entity.Project;
import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.exception.ResourceNotFoundException;
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

    public Task createTask(TaskRequest request) {
        Task task = new Task();
        BeanUtils.copyProperties(request, task);

        Project project = projectRepo.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi: Không tìm thấy Project với ID = " + request.getProjectId()));
        task.setProject(project);

        if (request.getUserId() != null) {
            User user = userRepo.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lỗi: Không tìm thấy User với ID = " + request.getUserId()));
            task.setUser(user);
        }
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }

        return taskRepo.save(task);
    }

    public Task assignTask(Integer taskId, Integer userId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi: Không tìm thấy Task ID = " + taskId));

        if (task.getStatus() == TaskStatus.DONE) {
            throw new RuntimeException("Lỗi: Task đã hoàn thành (DONE), không thể giao lại!");
        }

        Integer projectId = task.getProject().getId();
        boolean isMember = projectRepo.existsByIdAndUsersId(projectId, userId);

        if (!isMember) {
            throw new RuntimeException("Lỗi: User ID " + userId + " không phải thành viên của dự án này!");
        }

        // SỬA 4: Dùng ResourceNotFoundException
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi: Không tìm thấy User ID = " + userId));

        task.setUser(user);
        return taskRepo.save(task);
    }

    public void deleteTask(Integer id) {
        if (!taskRepo.existsById(id)) {
            throw new ResourceNotFoundException("Không thể xóa. Không tìm thấy Task ID = " + id);
        }
        taskRepo.deleteById(id);
    }

    public List<Task> getTasksByProject(Integer projectId) {
        if (!projectRepo.existsById(projectId)) {
            throw new ResourceNotFoundException("Lỗi: Không tìm thấy Project ID = " + projectId);
        }

        return taskRepo.findByProjectId(projectId);
    }

    public List<Task> getTasksByUser(Integer userId) {
        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException("Lỗi: Không tìm thấy User ID = " + userId);
        }
        return taskRepo.findByUserId(userId);
    }

    public Task updateTaskStatus(Integer taskId, String newStatus) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi: Không tìm thấy Task ID = " + taskId));

        if (task.getStatus() == TaskStatus.DONE) {
            throw new RuntimeException("Lỗi: Task đã đóng (DONE), không thể thay đổi trạng thái nữa!");
        }

        try {
            TaskStatus status = TaskStatus.valueOf(newStatus);
            task.setStatus(status);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Lỗi: Trạng thái không hợp lệ! Chỉ chấp nhận: TODO, IN_PROGRESS, DONE");
        }

        return taskRepo.save(task);
    }
}