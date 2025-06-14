package com.uth.quizclear.model.dto;

import java.util.List;

public class ExamQuestionDTO {
    private Long examQuestionId;
    private int questionOrder;
    private String content;
    private List<String> options;
    private int correctIndex;
    private String difficulty;
    private String status;
    private String feedback;

    public Long getExamQuestionId() { return examQuestionId; }
    public void setExamQuestionId(Long examQuestionId) { this.examQuestionId = examQuestionId; }
    
    public int getQuestionOrder() { return questionOrder; }
    public void setQuestionOrder(int questionOrder) { this.questionOrder = questionOrder; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    
    public int getCorrectIndex() { return correctIndex; }
    public void setCorrectIndex(int correctIndex) { this.correctIndex = correctIndex; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
