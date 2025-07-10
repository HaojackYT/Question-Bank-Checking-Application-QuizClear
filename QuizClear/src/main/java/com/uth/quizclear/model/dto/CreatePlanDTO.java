package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

public class CreatePlanDTO {
    private Long courseId;
    private String planTitle;
    private String planDescription;
    private Integer totalQuestions;
    private Integer totalRecognition;
    private Integer totalComprehension;
    private Integer totalBasicApplication;
    private Integer totalAdvancedApplication;
    private Long assignedTo;
    private LocalDateTime dueDate;
    
    // CLO Distribution
    private String clo1;
    private String clo2;
    private String clo3;
    private String clo4;

    // Constructors
    public CreatePlanDTO() {}

    public CreatePlanDTO(Long courseId, String planTitle, String planDescription, 
                        Integer totalQuestions, Integer totalRecognition, 
                        Integer totalComprehension, Integer totalBasicApplication, 
                        Integer totalAdvancedApplication, Long assignedTo, 
                        LocalDateTime dueDate, String clo1, String clo2, 
                        String clo3, String clo4) {
        this.courseId = courseId;
        this.planTitle = planTitle;
        this.planDescription = planDescription;
        this.totalQuestions = totalQuestions;
        this.totalRecognition = totalRecognition;
        this.totalComprehension = totalComprehension;
        this.totalBasicApplication = totalBasicApplication;
        this.totalAdvancedApplication = totalAdvancedApplication;
        this.assignedTo = assignedTo;
        this.dueDate = dueDate;
        this.clo1 = clo1;
        this.clo2 = clo2;
        this.clo3 = clo3;
        this.clo4 = clo4;
    }

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
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

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getClo1() {
        return clo1;
    }

    public void setClo1(String clo1) {
        this.clo1 = clo1;
    }

    public String getClo2() {
        return clo2;
    }

    public void setClo2(String clo2) {
        this.clo2 = clo2;
    }

    public String getClo3() {
        return clo3;
    }

    public void setClo3(String clo3) {
        this.clo3 = clo3;
    }

    public String getClo4() {
        return clo4;
    }

    public void setClo4(String clo4) {
        this.clo4 = clo4;
    }

    @Override
    public String toString() {
        return "CreatePlanDTO{" +
                "courseId=" + courseId +
                ", planTitle='" + planTitle + '\'' +
                ", planDescription='" + planDescription + '\'' +
                ", totalQuestions=" + totalQuestions +
                ", totalRecognition=" + totalRecognition +
                ", totalComprehension=" + totalComprehension +
                ", totalBasicApplication=" + totalBasicApplication +
                ", totalAdvancedApplication=" + totalAdvancedApplication +
                ", assignedTo=" + assignedTo +
                ", dueDate=" + dueDate +
                ", clo1='" + clo1 + '\'' +
                ", clo2='" + clo2 + '\'' +
                ", clo3='" + clo3 + '\'' +
                ", clo4='" + clo4 + '\'' +
                '}';
    }
}