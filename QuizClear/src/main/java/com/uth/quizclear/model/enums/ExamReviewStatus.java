package com.uth.quizclear.model.enums;

public enum ExamReviewStatus {
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected"),
    NEEDS_REVISION("needs_revision");

    private final String value;

    ExamReviewStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}