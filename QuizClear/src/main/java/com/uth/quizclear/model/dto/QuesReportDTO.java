package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

public class QuesReportDTO {

    private Long id;
    private String contributor;
    private String difficulty;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public QuesReportDTO() {

    }

    public QuesReportDTO(Long id, String contributor, String difficulty, LocalDateTime createdAt, String status) {
        this.id = id;
        this.contributor = contributor;
        this.difficulty = difficulty;
        this.createdAt = createdAt;
        this.status = status;
    }

    @Override
    public String toString() {
        return "QuesReportDTO [id=" + id + ", contributor=" + contributor + ", difficulty=" + difficulty
                + ", updatedAt=" + updatedAt + ", createdAt=" + createdAt + ", status=" + status + "]";
    }

}
