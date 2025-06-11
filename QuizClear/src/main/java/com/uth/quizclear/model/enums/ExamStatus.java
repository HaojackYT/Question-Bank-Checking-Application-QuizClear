package com.uth.quizclear.model.enums;

/**
 * Enum representing the status of an exam in the workflow process.
 * This tracks the exam from initial draft through approval.
 */
public enum ExamStatus {
    DRAFT("draft", "Draft"),
    SUBMITTED("submitted", "Submitted for Review"),
    APPROVED("approved", "Approved"),
    FINALIZED("finalized", "Finalized"),
    REJECTED("rejected", "Rejected");

    private final String value;
    private final String displayName;

    ExamStatus(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get ExamStatus from string value
     */
    public static ExamStatus fromValue(String value) {
        for (ExamStatus status : ExamStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown ExamStatus: " + value);
    }

    /**
     * Check if the status allows modification
     */
    public boolean isModifiable() {
        return this == DRAFT || this == REJECTED;
    }

    /**
     * Check if the status requires review
     */
    public boolean requiresReview() {
        return this == SUBMITTED;
    }

    /**
     * Check if the status is final
     */
    public boolean isFinal() {
        return this == APPROVED || this == FINALIZED || this == REJECTED;
    }
}