package com.uth.quizclear.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "duplicate_detections")
public class DuplicateDetection {

    public enum Status {
        pending, accepted, rejected, sent_back, merged
    }

    public enum Action {
        accept, reject, send_back, merge  // Sửa từ 'merged' thành 'merge' theo DB
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detection_id")
    private Integer detectionId;

    @ManyToOne
    @JoinColumn(name = "new_question_id", nullable = false)
    private Question newQuestion;

    @ManyToOne
    @JoinColumn(name = "similar_question_id", nullable = false)
    private Question similarQuestion;

    @Column(name = "similarity_score", nullable = false, precision = 5, scale = 4)
    private BigDecimal similarityScore;

    // Thêm trường ai_check_id từ DB
    @ManyToOne
    @JoinColumn(name = "ai_check_id")
    private AiDuplicateCheck aiCheck;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.pending;  // Default value từ DB

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private Action action;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "detected_by")
    private User detectedBy;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @Column(name = "detected_at")
    private LocalDateTime detectedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    // Constructors
    public DuplicateDetection() {
        this.detectedAt = LocalDateTime.now();
        this.status = Status.pending;
    }

    // ====== GETTERS ======
    public Integer getDetectionId() {
        return detectionId;
    }

    public Question getNewQuestion() {
        return newQuestion;
    }

    public Question getSimilarQuestion() {
        return similarQuestion;
    }

    public BigDecimal getSimilarityScore() {
        return similarityScore;
    }

    public AiDuplicateCheck getAiCheck() {
        return aiCheck;
    }

    public Status getStatus() {
        return status;
    }

    public Action getAction() {
        return action;
    }

    public String getFeedback() {
        return feedback;
    }

    public User getDetectedBy() {
        return detectedBy;
    }

    public User getProcessedBy() {
        return processedBy;
    }

    public LocalDateTime getDetectedAt() {
        return detectedAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    // ====== SETTERS ======
    public void setDetectionId(Integer detectionId) {
        this.detectionId = detectionId;
    }

    public void setNewQuestion(Question newQuestion) {
        this.newQuestion = newQuestion;
    }

    public void setSimilarQuestion(Question similarQuestion) {
        this.similarQuestion = similarQuestion;
    }

    public void setSimilarityScore(BigDecimal similarityScore) {
        this.similarityScore = similarityScore;
    }

    public void setAiCheck(AiDuplicateCheck aiCheck) {
        this.aiCheck = aiCheck;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setDetectedBy(User detectedBy) {
        this.detectedBy = detectedBy;
    }

    public void setProcessedBy(User processedBy) {
        this.processedBy = processedBy;
    }

    public void setDetectedAt(LocalDateTime detectedAt) {
        this.detectedAt = detectedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    // ====== UTILITY METHODS ======
    @PrePersist
    protected void onCreate() {
        if (detectedAt == null) {
            detectedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.pending;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (status != Status.pending && processedAt == null) {
            processedAt = LocalDateTime.now();
        }
    }
}