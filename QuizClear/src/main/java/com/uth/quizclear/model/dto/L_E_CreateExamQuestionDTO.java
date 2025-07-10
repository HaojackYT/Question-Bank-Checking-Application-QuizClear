package com.uth.quizclear.model.dto;

public class L_E_CreateExamQuestionDTO {
    private Long questionId;
    private String content;
    private String difficultyTag;
    private String cloTag;

    public L_E_CreateExamQuestionDTO() {}

    public L_E_CreateExamQuestionDTO(Long questionId, String content, String difficultyTag, String cloTag) {
        this.questionId = questionId;
        this.content = content;
        this.difficultyTag = difficultyTag;
        this.cloTag = cloTag;
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
}