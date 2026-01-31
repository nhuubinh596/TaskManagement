package com.example.taskmanagement.service;

import com.example.taskmanagement.entity.Project;
import com.example.taskmanagement.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Integer id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
}