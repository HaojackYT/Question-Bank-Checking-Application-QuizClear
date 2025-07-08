package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;
import com.uth.quizclear.model.enums.DifficultyLevel;

public class SendQuesDTO {

    private Long questionId;
    private String courseName;
    private String content;
    private String answerKey;
    private String answerF1;
    private String answerF2;
    private String answerF3;
    private DifficultyLevel difficultyLevel;
    private LocalDateTime createdAt;

    // Getters / Setters
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    public String getAnswerF1() {
        return answerF1;
    }

    public void setAnswerF1(String answerF1) {
        this.answerF1 = answerF1;
    }

    public String getAnswerF2() {
        return answerF2;
    }

    public void setAnswerF2(String answerF2) {
        this.answerF2 = answerF2;
    }

    public String getAnswerF3() {
        return answerF3;
    }

    public void setAnswerF3(String answerF3) {
        this.answerF3 = answerF3;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    // Contructor
    public SendQuesDTO() {
    }

    public SendQuesDTO(Long questionId, String courseName, String content, String answerKey, String answerF1,
            String answerF2, String answerF3, DifficultyLevel difficultyLevel, LocalDateTime createdAt) {
        this.questionId = questionId;
        this.courseName = courseName;
        this.content = content;
        this.answerKey = answerKey;
        this.answerF1 = answerF1;
        this.answerF2 = answerF2;
        this.answerF3 = answerF3;
        this.difficultyLevel = difficultyLevel;
        this.createdAt = createdAt;
    }

    // toString
    @Override
    public String toString() {
        return "SendQuesDTO [questionId=" + questionId + ", courseName=" + courseName + ", content=" + content
                + ", answerKey=" + answerKey + ", answerF1=" + answerF1 + ", answerF2=" + answerF2 + ", answerF3="
                + answerF3 + ", difficultyLevel=" + difficultyLevel + ", createdAt=" + createdAt + "]";
    }

}
