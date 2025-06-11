package com.uth.quizclear.model.enums;

/**
 * Duplicate detection workflow status
 */
public enum DuplicateStatus {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected"),
    SENT_BACK("sent_back"),
    MERGED("merged");

    private final String value;

    DuplicateStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isProcessed() {
        return this != PENDING;
    }

    public boolean requiresAction() {
        return this == PENDING;
    }
}
