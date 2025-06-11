package com.uth.quizclear.model.enums;

/**
 * Question workflow status
 */
public enum QuestionStatus {
    DRAFT("draft"),
    SUBMITTED("submitted"),
    APPROVED("approved"),
    REJECTED("rejected"),
    ARCHIVED("archived");

    private final String value;

    QuestionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isEditable() {
        return this == DRAFT || this == REJECTED;
    }

    public boolean canBeSubmitted() {
        return this == DRAFT || this == REJECTED;
    }

    public boolean isUnderReview() {
        return this == SUBMITTED;
    }

    public boolean isFinal() {
        return this == APPROVED || this == ARCHIVED;
    }
}
