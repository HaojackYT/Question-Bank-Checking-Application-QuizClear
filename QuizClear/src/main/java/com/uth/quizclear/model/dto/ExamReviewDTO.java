package com.uth.quizclear.model.dto;

import java.util.List;

public class ExamReviewDTO {
    private Long examId;
    private String examTitle;
    private String examCode;
    private String courseName;
    private int totalQuestions;
    private int reviewedCount;
    private int approvedCount;
    private int rejectedCount;
    private int remainingCount;
    private List<ExamQuestionDTO> questions;
    
    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public String getExamCode() { return examCode; }
    public void setExamCode(String examCode) { this.examCode = examCode; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    
    public int getReviewedCount() { return reviewedCount; }
    public void setReviewedCount(int reviewedCount) { this.reviewedCount = reviewedCount; }
    
    public int getApprovedCount() { return approvedCount; }
    public void setApprovedCount(int approvedCount) { this.approvedCount = approvedCount; }
    
    public int getRejectedCount() { return rejectedCount; }
    public void setRejectedCount(int rejectedCount) { this.rejectedCount = rejectedCount; }
    
    public int getRemainingCount() { return remainingCount; }
    public void setRemainingCount(int remainingCount) { this.remainingCount = remainingCount; }
    
    public List<ExamQuestionDTO> getQuestions() { return questions; }
    public void setQuestions(List<ExamQuestionDTO> questions) { this.questions = questions; }
}
