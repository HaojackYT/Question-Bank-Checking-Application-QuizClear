package com.uth.quizclear.model.dto;

public class DuplicateDetectionDTO2 {
    private Long detectionId;
    private Long newQuestionId;
    private String newQuestionContent;
    private Long similarQuestionId;
    private String similarQuestionContent;
    private double similarityScore;
    private String submitterName;

    public DuplicateDetectionDTO2() { }

    public DuplicateDetectionDTO2(Long detectionId, Long newQuestionId, String newQuestionContent,
            Long similarQuestionId, String similarQuestionContent, double similarityScore, String submitterName) {
        this.detectionId = detectionId;
        this.newQuestionId = newQuestionId;
        this.newQuestionContent = newQuestionContent;
        this.similarQuestionId = similarQuestionId;
        this.similarQuestionContent = similarQuestionContent;
        this.similarityScore = similarityScore;
        this.submitterName = submitterName;
    }
    
    public Long getDetectionId() { return detectionId; }
    public void setDetectionId(Long detectionId) { this.detectionId = detectionId; }

    public Long getNewQuestionId() { return newQuestionId; }
    public void setNewQuestionId(Long newQuestionId) { this.newQuestionId = newQuestionId; }

    public String getNewQuestionContent() { return newQuestionContent; }
    public void setNewQuestionContent(String newQuestionContent) { this.newQuestionContent = newQuestionContent; }

    public Long getSimilarQuestionId() { return similarQuestionId; }
    public void setSimilarQuestionId(Long similarQuestionId) { this.similarQuestionId = similarQuestionId; }

    public String getSimilarQuestionContent() { return similarQuestionContent; }
    public void setSimilarQuestionContent(String similarQuestionContent) { this.similarQuestionContent = similarQuestionContent; }

    public double getSimilarityScore() { return similarityScore; }
    public void setSimilarityScore(double similarityScore) { this.similarityScore = similarityScore; }

    public String getSubmitterName() { return submitterName; }
    public void setSubmitterName(String submitterName) { this.submitterName = submitterName; }
}
