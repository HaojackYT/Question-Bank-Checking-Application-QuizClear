package com.uth.quizclear.model.dto;

public class L_E_CreateExamQuestionDetailDTO {
    private Long questionId;
    private String content;
    private String difficultyTag;
    private String cloTag;
    private String correctAnswer;
    private String incorrectAnswer1;
    private String incorrectAnswer2;
    private String incorrectAnswer3;

    public L_E_CreateExamQuestionDetailDTO() {}

    public L_E_CreateExamQuestionDetailDTO(Long questionId, String content, String difficultyTag, String cloTag,
                                          String correctAnswer, String incorrectAnswer1, String incorrectAnswer2, String incorrectAnswer3) {
        this.questionId = questionId;
        this.content = content;
        this.difficultyTag = difficultyTag;
        this.cloTag = cloTag;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswer1 = incorrectAnswer1;
        this.incorrectAnswer2 = incorrectAnswer2;
        this.incorrectAnswer3 = incorrectAnswer3;
    }

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

    public String getDifficultyTag() {
        return difficultyTag;
    }

    public void setDifficultyTag(String difficultyTag) {
        this.difficultyTag = difficultyTag;
    }

    public String getCloTag() {
        return cloTag;
    }

    public void setCloTag(String cloTag) {
        this.cloTag = cloTag;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getIncorrectAnswer1() {
        return incorrectAnswer1;
    }

    public void setIncorrectAnswer1(String incorrectAnswer1) {
        this.incorrectAnswer1 = incorrectAnswer1;
    }

    public String getIncorrectAnswer2() {
        return incorrectAnswer2;
    }

    public void setIncorrectAnswer2(String incorrectAnswer2) {
        this.incorrectAnswer2 = incorrectAnswer2;
    }

    public String getIncorrectAnswer3() {
        return incorrectAnswer3;
    }

    public void setIncorrectAnswer3(String incorrectAnswer3) {
        this.incorrectAnswer3 = incorrectAnswer3;
    }
}