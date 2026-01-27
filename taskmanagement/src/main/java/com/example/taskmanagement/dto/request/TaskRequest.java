package com.example.taskmanagement.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class TaskRequest {
    @NotBlank(message = "Tên task không được để trống")
    private String title;

    private String description;

    @NotNull(message = "Phải có deadline")
    @Future(message = "Deadline phải là ngày trong tương lai")
    private Date deadline;

    private String status;

    @NotNull(message = "Task phải thuộc về một Project")
    private Integer projectId;

    private Integer userId;
}