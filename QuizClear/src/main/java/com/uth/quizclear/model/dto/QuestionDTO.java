package com.uth.quizclear.model.dto;

import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.model.enums.QuestionStatus;

public class QuestionDTO {
    private Long questionId;
    private Long taskId;
    private String content;
    private DifficultyLevel difficultyLevel;
    private String answerKey;
    private String answerF1;
    private String answerF2;
    private String answerF3;
    private String explanation;
    private QuestionStatus status;

    public QuestionDTO() {}

    public QuestionDTO(Long questionId, Long taskId, String content, DifficultyLevel difficultyLevel,
                      String answerKey, String answerF1, String answerF2, String answerF3,
                      String explanation, QuestionStatus status) {
        this.questionId = questionId;
        this.taskId = taskId;
        this.content = content;
        this.difficultyLevel = difficultyLevel;
        this.answerKey = answerKey;
        this.answerF1 = answerF1;
        this.answerF2 = answerF2;
        this.answerF3 = answerF3;
        this.explanation = explanation;
        this.status = status;
    }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    public String getAnswerKey() { return answerKey; }
    public void setAnswerKey(String answerKey) { this.answerKey = answerKey; }
    public String getAnswerF1() { return answerF1; }
    public void setAnswerF1(String answerF1) { this.answerF1 = answerF1; }
    public String getAnswerF2() { return answerF2; }
    public void setAnswerF2(String answerF2) { this.answerF2 = answerF2; }
    public String getAnswerF3() { return answerF3; }
    public void setAnswerF3(String answerF3) { this.answerF3 = answerF3; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public QuestionStatus getStatus() { return status; }
    public void setStatus(QuestionStatus status) { this.status = status; }

    // Hỗ trợ frontend nếu cần String
    public String getDifficultyLevelAsString() {
        return difficultyLevel != null ? difficultyLevel.name() : null;
    }

    public void setDifficultyLevelAsString(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel != null ? DifficultyLevel.valueOf(difficultyLevel.toUpperCase()) : null;
    }

    public String getStatusAsString() {
        return status != null ? status.name() : null;
    }

    public void setStatusAsString(String status) {
        this.status = status != null ? QuestionStatus.valueOf(status.toUpperCase()) : null;
    }
}