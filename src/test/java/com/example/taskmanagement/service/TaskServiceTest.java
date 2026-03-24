package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.request.TaskRequest;
import com.example.taskmanagement.entity.Project;
import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.exception.ResourceNotFoundException;
import com.example.taskmanagement.repository.ProjectRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.util.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    private static final Long TASK_ID = 1L;
    private static final Integer USER_ID = 2;
    private static final Long PROJECT_ID = 1L;
    private static final String CURRENT_USERNAME = "testuser";

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @AfterEach
    public void cleanSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // ======================== getMyTasks & getAllTasks ========================

    @Test
    public void getMyTasks_success() {
        setUserAuthentication(CURRENT_USERNAME);
        when(taskRepository.findByUser_Username(CURRENT_USERNAME)).thenReturn(List.of(new Task(), new Task()));

        List<Task> tasks = taskService.getMyTasks();

        Assertions.assertEquals(2, tasks.size());
        verify(taskRepository).findByUser_Username(CURRENT_USERNAME);
    }

    @Test
    public void getMyTasks_emptyList() {
        setUserAuthentication(CURRENT_USERNAME);
        when(taskRepository.findByUser_Username(CURRENT_USERNAME)).thenReturn(List.of());

        List<Task> tasks = taskService.getMyTasks();

        Assertions.assertTrue(tasks.isEmpty());
        verify(taskRepository).findByUser_Username(CURRENT_USERNAME);
    }

    @Test
    public void getAllTasks_success() {
        when(taskRepository.findAll()).thenReturn(List.of(new Task(), new Task(), new Task()));

        List<Task> tasks = taskService.getAllTasks();

        Assertions.assertEquals(3, tasks.size());
        verify(taskRepository).findAll();
    }

    // ======================== createTask ========================

    @Test
    public void createTask_withUser_success() {
        stubProjectFound(projectWithId(PROJECT_ID));
        stubUserFound(userWithId(USER_ID));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);

        TaskRequest request = createTaskRequest(PROJECT_ID, USER_ID);
        Task task = taskService.createTask(request);

        Assertions.assertNotNull(task);
        Assertions.assertEquals(TaskStatus.TODO, task.getStatus());
        Assertions.assertNotNull(task.getUser());
        verify(projectRepository).findById(PROJECT_ID);
        verify(userRepository).findById(USER_ID);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void createTask_withoutUser_success() {
        stubProjectFound(projectWithId(PROJECT_ID));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);

        TaskRequest request = createTaskRequest(PROJECT_ID, null); // Không có user
        Task task = taskService.createTask(request);

        Assertions.assertNotNull(task);
        Assertions.assertEquals(TaskStatus.TODO, task.getStatus());
        Assertions.assertNull(task.getUser());
        verify(projectRepository).findById(PROJECT_ID);
        verify(userRepository, never()).findById(anyInt());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void createTask_projectNotFound_throwException() {
        stubProjectNotFound();
        TaskRequest request = createTaskRequest(PROJECT_ID, USER_ID);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> taskService.createTask(request));

        verify(projectRepository).findById(PROJECT_ID);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void createTask_userNotFound_throwException() {
        stubProjectFound(projectWithId(PROJECT_ID));
        stubUserNotFound();
        TaskRequest request = createTaskRequest(PROJECT_ID, USER_ID);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> taskService.createTask(request));

        verify(taskRepository, never()).save(any(Task.class));
    }

    // ======================== assignTask ========================

    @Test
    public void assignTask_success() {
        Task task = taskWithProject(PROJECT_ID);
        User user = userWithId(USER_ID);
        task.getProject().getMembers().add(user); // Thêm user vào dự án

        stubTaskFound(task);
        stubUserFound(user);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task response = taskService.assignTask(TASK_ID, USER_ID);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(user, response.getUser());
        verifyTaskAndUserLookupCalled();
    }

    @Test
    public void assignTask_taskNotFound_throwException() {
        stubTaskNotFound();

        Assertions.assertThrows(RuntimeException.class, () -> taskService.assignTask(TASK_ID, USER_ID));

        verify(taskRepository).findById(TASK_ID);
        verify(userRepository, never()).findById(anyInt());
    }

    @Test
    public void assignTask_userNotFound_throwException() {
        stubTaskFound(taskWithProject(PROJECT_ID));
        stubUserNotFound();

        Assertions.assertThrows(RuntimeException.class, () -> taskService.assignTask(TASK_ID, USER_ID));

        verifyTaskAndUserLookupCalled();
    }

    @Test
    public void assignTask_userNotInProject_throwException() {
        Task task = taskWithProject(PROJECT_ID);
        User user = userWithId(USER_ID);
        // Không add user vào project để test văng lỗi

        stubTaskFound(task);
        stubUserFound(user);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> taskService.assignTask(TASK_ID, USER_ID));

        Assertions.assertEquals("User không thuộc dự án này", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    // ======================== getTasksByProject & User ========================

    @Test
    public void getTasksByProject_success() {
        when(projectRepository.existsById(PROJECT_ID)).thenReturn(true);
        when(taskRepository.findByProjectId(PROJECT_ID)).thenReturn(List.of(new Task()));

        List<Task> tasks = taskService.getTasksByProject(PROJECT_ID);

        Assertions.assertEquals(1, tasks.size());
        verify(projectRepository).existsById(PROJECT_ID);
    }

    @Test
    public void getTasksByProject_notFound_throwException() {
        when(projectRepository.existsById(PROJECT_ID)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> taskService.getTasksByProject(PROJECT_ID));
    }

    @Test
    public void getTasksByUser_success() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(taskRepository.findByUserId(USER_ID)).thenReturn(List.of(new Task(), new Task()));

        List<Task> tasks = taskService.getTasksByUser(USER_ID);

        Assertions.assertEquals(2, tasks.size());
        verify(userRepository).existsById(USER_ID);
    }

    @Test
    public void getTasksByUser_notFound_throwException() {
        when(userRepository.existsById(USER_ID)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> taskService.getTasksByUser(USER_ID));
    }

    // ======================== updateTaskStatus ========================

    @Test
    public void updateTaskStatus_toInProgress_success() {
        Task task = new Task();
        task.setId(TASK_ID);
        task.setStatus(TaskStatus.TODO);
        stubTaskFound(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task response = taskService.updateTaskStatus(TASK_ID, "IN_PROGRESS");

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, response.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    public void updateTaskStatus_toDone_success() {
        Task task = new Task();
        task.setId(TASK_ID);
        task.setStatus(TaskStatus.IN_PROGRESS);
        stubTaskFound(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task response = taskService.updateTaskStatus(TASK_ID, "DONE");

        Assertions.assertEquals(TaskStatus.DONE, response.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    public void updateTaskStatus_taskAlreadyDone_throwException() {
        Task task = new Task();
        task.setId(TASK_ID);
        task.setStatus(TaskStatus.DONE);
        stubTaskFound(task);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> taskService.updateTaskStatus(TASK_ID, "TODO"));

        Assertions.assertEquals("Lỗi: Task đã đóng (DONE), không thể thay đổi trạng thái nữa!", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void updateTaskStatus_invalidStatus_throwException() {
        Task task = new Task();
        task.setId(TASK_ID);
        task.setStatus(TaskStatus.TODO);
        stubTaskFound(task);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> taskService.updateTaskStatus(TASK_ID, "INVALID_STATUS"));

        Assertions.assertEquals("Lỗi: Trạng thái không hợp lệ! Chỉ chấp nhận: TODO, IN_PROGRESS, DONE", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void updateTaskStatus_taskNotFound_throwException() {
        stubTaskNotFound();
        Assertions.assertThrows(ResourceNotFoundException.class, () -> taskService.updateTaskStatus(TASK_ID, "DONE"));
    }

    // ======================== deleteTask ========================

    @Test
    public void deleteTask_success() {
        when(taskRepository.existsById(TASK_ID)).thenReturn(true);
        taskService.deleteTask(TASK_ID);
        verify(taskRepository).deleteById(TASK_ID);
    }

    @Test
    public void deleteTask_taskNotFound_throwException() {
        when(taskRepository.existsById(TASK_ID)).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(TASK_ID));
        verify(taskRepository, never()).deleteById(anyLong());
    }

    // ======================== HELPER METHODS ========================

    private TaskRequest createTaskRequest(Long projectId, Integer userId) {
        TaskRequest request = new TaskRequest();
        request.setTitle("New Task");
        request.setDeadline(new Date());
        request.setProjectId(projectId);
        request.setUserId(userId);
        return request;
    }

    private Task taskWithProject(Long projectId) {
        Task task = new Task();
        task.setId(TASK_ID);
        task.setProject(projectWithId(projectId));
        return task;
    }

    private Project projectWithId(Long id) {
        Project project = new Project();
        project.setId(id);
        project.setMembers(new HashSet<>());
        return project;
    }

    private User userWithId(Integer id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private void stubProjectFound(Project project) {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
    }

    private void stubProjectNotFound() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());
    }

    private void stubTaskFound(Task task) {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
    }

    private void stubTaskNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());
    }

    private void stubUserFound(User user) {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
    }

    private void stubUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
    }

    private void verifyTaskAndUserLookupCalled() {
        verify(taskRepository).findById(TASK_ID);
        verify(userRepository).findById(USER_ID);
    }

    private void setUserAuthentication(String username) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("USER")))
        );
    }
}