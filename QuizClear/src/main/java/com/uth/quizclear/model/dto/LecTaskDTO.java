package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.enums.TaskStatus;

public class LecTaskDTO {

    // Task
    private Integer taskId;
    private String cousreName;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus status;

    // Plan
    private Long planId;

    // For displaying task details
    private Integer totalQuestions = 0;
    private Integer totalRecognition = 0;
    private Integer totalComprehension = 0;
    private Integer totalBasicApplication = 0;
    private Integer totalAdvancedApplication = 0;

    // Getters/ Setters
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getCousres() {
        return cousreName;
    }

    public void setCousres(String cousreName) {
        this.cousreName = cousreName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getTotalRecognition() {
        return totalRecognition;
    }

    public void setTotalRecognition(Integer totalRecognition) {
        this.totalRecognition = totalRecognition;
    }

    public Integer getTotalComprehension() {
        return totalComprehension;
    }

    public void setTotalComprehension(Integer totalComprehension) {
        this.totalComprehension = totalComprehension;
    }

    public Integer getTotalBasicApplication() {
        return totalBasicApplication;
    }

    public void setTotalBasicApplication(Integer totalBasicApplication) {
        this.totalBasicApplication = totalBasicApplication;
    }

    public Integer getTotalAdvancedApplication() {
        return totalAdvancedApplication;
    }

    public void setTotalAdvancedApplication(Integer totalAdvancedApplication) {
        this.totalAdvancedApplication = totalAdvancedApplication;
    }

    // Constructor
    public LecTaskDTO() {
    }

    public LecTaskDTO(Integer taskId, String cousreName, String description, LocalDateTime dueDate, TaskStatus status,
            Long planId, Integer totalQuestions, Integer totalRecognition, Integer totalComprehension,
            Integer totalBasicApplication, Integer totalAdvancedApplication) {
        this.taskId = taskId;
        this.cousreName = cousreName;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.planId = planId;
        this.totalQuestions = totalQuestions;
        this.totalRecognition = totalRecognition;
        this.totalComprehension = totalComprehension;
        this.totalBasicApplication = totalBasicApplication;
        this.totalAdvancedApplication = totalAdvancedApplication;
    }

    public LecTaskDTO(String cousreName, String description, LocalDateTime dueDate, TaskStatus status, Long planId,
            Integer totalQuestions, Integer totalRecognition, Integer totalComprehension, Integer totalBasicApplication,
            Integer totalAdvancedApplication) {
        this.cousreName = cousreName;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.planId = planId;
        this.totalQuestions = totalQuestions;
        this.totalRecognition = totalRecognition;
        this.totalComprehension = totalComprehension;
        this.totalBasicApplication = totalBasicApplication;
        this.totalAdvancedApplication = totalAdvancedApplication;
    }

    // toString 
    @Override
    public String toString() {
        return "LecTaskDTO [taskId=" + taskId + ", cousreName=" + cousreName + ", description=" + description + ", dueDate="
                + dueDate + ", status=" + status + ", planId=" + planId + ", totalQuestions=" + totalQuestions
                + ", totalRecognition=" + totalRecognition + ", totalComprehension=" + totalComprehension
                + ", totalBasicApplication=" + totalBasicApplication + ", totalAdvancedApplication="
                + totalAdvancedApplication + "]";
    }
}
