package com.example.taskmanagement.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectRequest {
    private String name;
    private String description;
    private LocalDate startDate;
    private Long projectId;
    private Integer userId;
}