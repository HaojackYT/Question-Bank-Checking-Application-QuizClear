package com.uth.quizclear.model.enums;

/**
 * AI check status for questions
 */
public enum AiCheckStatus {
    PENDING("pending", "Waiting for AI analysis"),
    IN_PROGRESS("in_progress", "AI analysis in progress"),
    COMPLETED("completed", "AI analysis completed"),
    FAILED("failed", "AI analysis failed"),
    CANCELLED("cancelled", "AI analysis cancelled");

    private final String value;
    private final String description;

    AiCheckStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if status indicates processing is complete
     */
    public boolean isComplete() {
        return this == COMPLETED || this == FAILED || this == CANCELLED;
    }

    /**
     * Check if status indicates active processing
     */
    public boolean isActive() {
        return this == PENDING || this == IN_PROGRESS;
    }

    /**
     * Check if status indicates success
     */
    public boolean isSuccessful() {
        return this == COMPLETED;
    }
}
