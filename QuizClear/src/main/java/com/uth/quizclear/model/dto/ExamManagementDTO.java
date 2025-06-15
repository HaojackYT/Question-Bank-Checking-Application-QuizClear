package com.uth.quizclear.model.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.time.LocalDateTime;

import com.uth.quizclear.model.enums.ExamReviewStatus;
import com.uth.quizclear.model.enums.ExamStatus;

public class ExamManagementDTO {
    private Long examId;
    private String examTitle;
    private String examCode;
    private String courseName;
    private Integer durationMinutes;
    private BigDecimal totalMarks;
    private Map<String, Integer> difficultyDistribution;
    private ExamStatus examStatus;
    private ExamReviewStatus reviewStatus;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private String reviewedBy;
    private LocalDateTime approvedAt;
    private String approvedBy;
    private Boolean hidden;
    private String examType;
    private String instructions;
    private LocalDateTime examDate;
    private String semester;
    private String academicYear;
    private String feedback;
    // Potentially include a list of question DTOs if needed for detail view
    
    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }
    
    public String getExamCode() { return examCode; }
    public void setExamCode(String examCode) { this.examCode = examCode; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public BigDecimal getTotalMarks() { return totalMarks; }
    public void setTotalMarks(BigDecimal totalMarks) { this.totalMarks = totalMarks; }
    
    public Map<String, Integer> getDifficultyDistribution() { return difficultyDistribution; }
    public void setDifficultyDistribution(Map<String, Integer> difficultyDistribution) { this.difficultyDistribution = difficultyDistribution; }
    
    public ExamStatus getExamStatus() { return examStatus; }
    public void setExamStatus(ExamStatus examStatus) { this.examStatus = examStatus; }
    
    public ExamReviewStatus getReviewStatus() { return reviewStatus; }
    public void setReviewStatus(ExamReviewStatus reviewStatus) { this.reviewStatus = reviewStatus; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    
    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }
    
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    
    public Boolean getHidden() { return hidden; }
    public void setHidden(Boolean hidden) { this.hidden = hidden; }
    
    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
    
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    
    public LocalDateTime getExamDate() { return examDate; }
    public void setExamDate(LocalDateTime examDate) { this.examDate = examDate; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
