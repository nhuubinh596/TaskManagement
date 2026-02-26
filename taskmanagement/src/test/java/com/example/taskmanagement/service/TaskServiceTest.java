package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.request.TaskRequest;
import com.example.taskmanagement.entity.Project;
import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.repository.ProjectRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private Project sharedProject;
    private TaskRequest sharedRequest;

    @BeforeEach
    void setUp() {
        sharedProject = new Project();
        sharedProject.setId(1L);
        sharedProject.setName("Project Final");
        sharedProject.setMembers(new HashSet<>());

        sharedRequest = new TaskRequest();
        sharedRequest.setTitle("API Refactored");
        sharedRequest.setProjectId(1L);
    }

    @Test
    void createTask_ValidInput_ReturnsSavedTask() {
        Task mockSavedTask = new Task();
        mockSavedTask.setId(100L);
        mockSavedTask.setTitle("API Refactored");
        mockSavedTask.setProject(sharedProject);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(sharedProject));
        when(taskRepository.save(any(Task.class))).thenReturn(mockSavedTask);

        Task result = taskService.createTask(sharedRequest);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Project Final", result.getProject().getName());
    }

    @Test
    void createTask_ProjectNotFound_ThrowsException() {
        sharedRequest.setProjectId(99L);
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.createTask(sharedRequest));
    }

    @Test
    void createTask_VerifyInteractions() {
        Task mockSavedTask = new Task();
        mockSavedTask.setId(100L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(sharedProject));
        when(taskRepository.save(any(Task.class))).thenReturn(mockSavedTask);

        taskService.createTask(sharedRequest);

        verify(projectRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void assignTask_UserNotInProject_ThrowsException() {
        Task existingTask = new Task();
        existingTask.setId(5L);
        existingTask.setProject(sharedProject);

        User outsiderUser = new User();
        outsiderUser.setId(2);

        when(taskRepository.findById(5L)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(2)).thenReturn(Optional.of(outsiderUser));

        assertThrows(RuntimeException.class, () -> taskService.assignTask(5L, 2));
    }
}