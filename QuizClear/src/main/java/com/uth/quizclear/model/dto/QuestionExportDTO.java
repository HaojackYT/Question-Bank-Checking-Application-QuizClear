package com.uth.quizclear.model.dto;

import java.util.List;

public class QuestionExportDTO {
    private int number;
    private String content;
    private List<String> options; // A, B, C, D
    private String correctAnswer;
    
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
}
