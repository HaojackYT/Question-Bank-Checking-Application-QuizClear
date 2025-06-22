package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

public class ExamRevisionDTO {

    private Long examId;
    private String examTitle;
    private String examCode;
    private String subject;
    private String courseName;
    private String requester;
    private String editor;
    private LocalDateTime submissionDate;
    private LocalDateTime dueDate;
    private String feedback;

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }
    
    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }
    
    public String getExamCode() { return examCode; }
    public void setExamCode(String examCode) { this.examCode = examCode; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getRequester() { return requester; }
    public void setRequester(String requester) { this.requester = requester; }
    
    public String getEditor() { return editor; }
    public void setEditor(String editor) { this.editor = editor; }
    
    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }
    
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    
}
