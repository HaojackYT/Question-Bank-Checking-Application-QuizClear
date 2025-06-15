package com.uth.quizclear.model.dto;

public class TaskAssignmentDTO {
    private Long taskId;
    private String title;
    private String subjectName;
    private Integer totalQuestionsRequired;
    private String assignedLecturerName;
    private Integer completedQuestions;
    private String status;

    // Constructor rõ ràng với 7 tham số
    public TaskAssignmentDTO(Long taskId, String title, String subjectName, 
                            Integer totalQuestionsRequired, String assignedLecturerName, 
                            Integer completedQuestions, String status) {
        this.taskId = taskId;
        this.title = title;
        this.subjectName = subjectName;
        this.totalQuestionsRequired = totalQuestionsRequired;
        this.assignedLecturerName = assignedLecturerName;
        this.completedQuestions = completedQuestions;
        this.status = status;
    }

    // Constructor không tham số
    public TaskAssignmentDTO() {
    }

    // Getters and setters
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getTotalQuestionsRequired() {
        return totalQuestionsRequired;
    }

    public void setTotalQuestionsRequired(Integer totalQuestionsRequired) {
        this.totalQuestionsRequired = totalQuestionsRequired;
    }

    public String getAssignedLecturerName() {
        return assignedLecturerName;
    }

    public void setAssignedLecturerName(String assignedLecturerName) {
        this.assignedLecturerName = assignedLecturerName;
    }

    public Integer getCompletedQuestions() {
        return completedQuestions;
    }

    public void setCompletedQuestions(Integer completedQuestions) {
        this.completedQuestions = completedQuestions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
