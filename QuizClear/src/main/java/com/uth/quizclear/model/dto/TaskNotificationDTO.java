package com.uth.quizclear.model.dto;

import com.uth.quizclear.model.enums.TaskStatus;

import java.time.LocalDateTime;

public class TaskNotificationDTO {
    private Integer taskId;
    private String title;
    private String courseName;
    private LocalDateTime dueDate;
    private TaskStatus status;

    // Constructors
    public TaskNotificationDTO() {}

    public TaskNotificationDTO(Integer taskId, String title, String courseName, LocalDateTime dueDate, TaskStatus status) {
        this.taskId = taskId;
        this.title = title;
        this.courseName = courseName;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters and Setters
    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
}