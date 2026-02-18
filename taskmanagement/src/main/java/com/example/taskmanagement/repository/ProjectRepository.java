package com.example.taskmanagement.repository;

import com.example.taskmanagement.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByIdAndUsersId(Long projectId, Integer userId);
}
