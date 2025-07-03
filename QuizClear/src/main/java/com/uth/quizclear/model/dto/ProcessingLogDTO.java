package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

public class ProcessingLogDTO {
    private Long logId;
    private String action;
    private String activity;
    private String processorName;
    private LocalDateTime processedAt;
    private String newQuestion;
    private String similarQuestion;
    private String similarity;
    private String feedback;
    private String newQuestionCourse;
    private String similarQuestionCourse;
    private String newQuestionCreator;
    private String similarQuestionCreator;
    
    // Default constructor
    public ProcessingLogDTO() {}
    
    // Constructor
    public ProcessingLogDTO(Long logId, String action, String activity, String processorName, 
                           LocalDateTime processedAt, String newQuestion, String similarQuestion, 
                           String similarity, String feedback) {
        this.logId = logId;
        this.action = action;
        this.activity = activity;
        this.processorName = processorName;
        this.processedAt = processedAt;
        this.newQuestion = newQuestion;
        this.similarQuestion = similarQuestion;
        this.similarity = similarity;
        this.feedback = feedback;
    }
    
    // Getters and Setters
    public Long getLogId() {
        return logId;
    }
    
    public void setLogId(Long logId) {
        this.logId = logId;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getActivity() {
        return activity;
    }
    
    public void setActivity(String activity) {
        this.activity = activity;
    }
    
    public String getProcessorName() {
        return processorName;
    }
    
    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }
    
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
    
    public String getNewQuestion() {
        return newQuestion;
    }
    
    public void setNewQuestion(String newQuestion) {
        this.newQuestion = newQuestion;
    }
    
    public String getSimilarQuestion() {
        return similarQuestion;
    }
    
    public void setSimilarQuestion(String similarQuestion) {
        this.similarQuestion = similarQuestion;
    }
    
    public String getSimilarity() {
        return similarity;
    }
    
    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    public String getNewQuestionCourse() {
        return newQuestionCourse;
    }
    
    public void setNewQuestionCourse(String newQuestionCourse) {
        this.newQuestionCourse = newQuestionCourse;
    }
    
    public String getSimilarQuestionCourse() {
        return similarQuestionCourse;
    }
    
    public void setSimilarQuestionCourse(String similarQuestionCourse) {
        this.similarQuestionCourse = similarQuestionCourse;
    }
    
    public String getNewQuestionCreator() {
        return newQuestionCreator;
    }
    
    public void setNewQuestionCreator(String newQuestionCreator) {
        this.newQuestionCreator = newQuestionCreator;
    }
    
    public String getSimilarQuestionCreator() {
        return similarQuestionCreator;
    }
    
    public void setSimilarQuestionCreator(String similarQuestionCreator) {
        this.similarQuestionCreator = similarQuestionCreator;
    }
}
