package com.uth.quizclear.model.enums;

public enum ExamReviewStatus {
    PENDING("pending", "Pending"),
    APPROVED("approved", "Approved"),
    REJECTED("rejected", "Rejected"),
    NEEDS_REVISION("needs_revision", "Needs revision");

    private final String value;
    private final String displayName;

    ExamReviewStatus(String value, String displayName) {
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
    public static ExamReviewStatus fromValue(String value) {
        for (ExamReviewStatus reviewStatus : ExamReviewStatus.values()) {
            if (reviewStatus.value.equals(value)) {
                return reviewStatus;
            }
        }
        throw new IllegalArgumentException("Unknown ExamStatus: " + value);
    }
}