package com.example.taskmanagement.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class TaskRequest {
    @NotBlank(message = "Tên task không được để trống")
    @Size(min = 5, max = 100, message = "Tiêu đề phải từ 5 đến 100 ký tự")
    private String title;

    @Size(max = 500, message = "Mô tả không được quá 500 ký tự")
    private String description;

    @NotNull(message = "Phải có deadline")
    @Future(message = "Deadline phải là ngày trong tương lai")
    private Date deadline;

    private String status;

    @NotNull(message = "Task phải thuộc về một Project")
    private Long projectId;

    private Integer userId;
}