package com.uth.quizclear.dto;

import java.time.LocalDateTime;

/**
 * DTO for exam summary information in HED approval process
 */
public class ExamSummaryDTO {
    private Long id;
    private String subjectName;
    private String lecturerName;
    private Integer totalQuestions;
    private LocalDateTime deadline;
    private LocalDateTime submittedAt;
    private String status;
    private String departmentName;
    private Long subjectId;
    private Long lecturerId;

    // Constructors
    public ExamSummaryDTO() {}

    public ExamSummaryDTO(Long id, String subjectName, String lecturerName, 
                         Integer totalQuestions, LocalDateTime deadline, 
                         LocalDateTime submittedAt, String status) {
        this.id = id;
        this.subjectName = subjectName;
        this.lecturerName = lecturerName;
        this.totalQuestions = totalQuestions;
        this.deadline = deadline;
        this.submittedAt = submittedAt;
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

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Long lecturerId) {
        this.lecturerId = lecturerId;
    }

    @Override
    public String toString() {
        return "ExamSummaryDTO{" +
                "id=" + id +
                ", subjectName='" + subjectName + '\'' +
                ", lecturerName='" + lecturerName + '\'' +
                ", totalQuestions=" + totalQuestions +
                ", deadline=" + deadline +
                ", status='" + status + '\'' +
                '}';
    }
}
