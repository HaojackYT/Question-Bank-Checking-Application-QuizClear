package com.uth.quizclear.dto;

import java.time.LocalDateTime;

/**
 * DTO for task assignment information in HED join task process
 */
public class TaskAssignmentDTO {
    private Long id;
    private String subjectName;
    private LocalDateTime deadline;
    private Integer totalQuestions;
    private String assignedStaff;
    private String status;
    private String description;
    private String priority;
    private LocalDateTime createdAt;
    private LocalDateTime assignedAt;
    private Long subjectId;
    private Long assignedById;
    private Long assignedToId;

    // Constructors
    public TaskAssignmentDTO() {}

    public TaskAssignmentDTO(Long id, String subjectName, LocalDateTime deadline, 
                            Integer totalQuestions, String assignedStaff, String status) {
        this.id = id;
        this.subjectName = subjectName;
        this.deadline = deadline;
        this.totalQuestions = totalQuestions;
        this.assignedStaff = assignedStaff;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getAssignedStaff() {
        return assignedStaff;
    }

    public void setAssignedStaff(String assignedStaff) {
        this.assignedStaff = assignedStaff;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(Long assignedById) {
        this.assignedById = assignedById;
    }

    public Long getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Long assignedToId) {
        this.assignedToId = assignedToId;
    }

    @Override
    public String toString() {
        return "TaskAssignmentDTO{" +
                "id=" + id +
                ", subjectName='" + subjectName + '\'' +
                ", deadline=" + deadline +
                ", totalQuestions=" + totalQuestions +
                ", assignedStaff='" + assignedStaff + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
