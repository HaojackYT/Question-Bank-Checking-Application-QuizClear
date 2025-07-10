package com.uth.quizclear.model.dto;

public class TaskAssignmentDTO {
    private Long taskId;
    private String title;
    private String subjectName;
    private String courseName;
    private Integer totalQuestionsRequired;
    private Integer totalQuestions;
    private Integer completedQuestions;

    // New fields for difficulty breakdown from Plan
    private Integer recognitionQuestions = 0;
    private Integer comprehensionQuestions = 0;
    private Integer basicApplicationQuestions = 0;
    private Integer advancedApplicationQuestions = 0;

    private String assignedLecturerName;
    private String assignedToName;
    // Field for RD staff who created the original plan
    private String planCreatedByName;
    private String status;
    private String dueDate;
    private String description;
    private String feedback;

    // Constructor
    public TaskAssignmentDTO(Long taskId, String title, String subjectName, String courseName,
                             Integer totalQuestionsRequired, Integer totalQuestions, Integer completedQuestions,
                             String assignedLecturerName, String assignedToName, String status,
                             String dueDate, String description, String feedback) {
        this.taskId = taskId;
        this.title = title;
        this.subjectName = subjectName;
        this.courseName = courseName;
        this.totalQuestionsRequired = totalQuestionsRequired;
        this.totalQuestions = totalQuestions;
        this.completedQuestions = completedQuestions;
        this.assignedLecturerName = assignedLecturerName;
        this.assignedToName = assignedToName;
        this.status = status;
        this.dueDate = dueDate;
        this.description = description;
        this.feedback = feedback;
    }

    // Constructor không tham số
    public TaskAssignmentDTO() {}

    // Getters và setters
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Integer getTotalQuestionsRequired() { return totalQuestionsRequired; }
    public void setTotalQuestionsRequired(Integer totalQuestionsRequired) { this.totalQuestionsRequired = totalQuestionsRequired; }
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }
    public Integer getCompletedQuestions() { return completedQuestions; }
    public void setCompletedQuestions(Integer completedQuestions) { this.completedQuestions = completedQuestions; }
    public String getAssignedLecturerName() { return assignedLecturerName; }
    public void setAssignedLecturerName(String assignedLecturerName) { this.assignedLecturerName = assignedLecturerName; }
    public String getAssignedToName() { return assignedToName; }
    public void setAssignedToName(String assignedToName) { this.assignedToName = assignedToName; }
    
    // Getter and setter for plan created by name (RD staff)
    public String getPlanCreatedByName() { return planCreatedByName; }
    public void setPlanCreatedByName(String planCreatedByName) { this.planCreatedByName = planCreatedByName; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    
    // Getters and setters for new difficulty fields
    public Integer getRecognitionQuestions() { return recognitionQuestions; }
    public void setRecognitionQuestions(Integer recognitionQuestions) { this.recognitionQuestions = recognitionQuestions; }
    
    public Integer getComprehensionQuestions() { return comprehensionQuestions; }
    public void setComprehensionQuestions(Integer comprehensionQuestions) { this.comprehensionQuestions = comprehensionQuestions; }
    
    public Integer getBasicApplicationQuestions() { return basicApplicationQuestions; }
    public void setBasicApplicationQuestions(Integer basicApplicationQuestions) { this.basicApplicationQuestions = basicApplicationQuestions; }
    
    public Integer getAdvancedApplicationQuestions() { return advancedApplicationQuestions; }
    public void setAdvancedApplicationQuestions(Integer advancedApplicationQuestions) { this.advancedApplicationQuestions = advancedApplicationQuestions; }
}
