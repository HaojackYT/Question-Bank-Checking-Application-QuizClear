package com.uth.quizclear.model.dto;

public class L_ETaskExamDTO {
    private Integer taskId;
    private Integer planId;
    private String subject;
    private String description;
    private Integer totalQuestions;
    private String deadline;
    private String status;
    private String assignedByName;
    private Integer durationMinutes;
    private Integer totalRecognition;
    private Integer totalComprehension;
    private Integer totalBasicApplication;
    private Integer totalAdvancedApplication;

    // Getters and Setters
    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }
    public Integer getPlanId() { return planId; }
    public void setPlanId(Integer planId) { this.planId = planId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAssignedByName() { return assignedByName; }
    public void setAssignedByName(String assignedByName) { this.assignedByName = assignedByName; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public Integer getTotalRecognition() { return totalRecognition; }
    public void setTotalRecognition(Integer totalRecognition) { this.totalRecognition = totalRecognition; }
    public Integer getTotalComprehension() { return totalComprehension; }
    public void setTotalComprehension(Integer totalComprehension) { this.totalComprehension = totalComprehension; }
    public Integer getTotalBasicApplication() { return totalBasicApplication; }
    public void setTotalBasicApplication(Integer totalBasicApplication) { this.totalBasicApplication = totalBasicApplication; }
    public Integer getTotalAdvancedApplication() { return totalAdvancedApplication; }
    public void setTotalAdvancedApplication(Integer totalAdvancedApplication) { this.totalAdvancedApplication = totalAdvancedApplication; }
}