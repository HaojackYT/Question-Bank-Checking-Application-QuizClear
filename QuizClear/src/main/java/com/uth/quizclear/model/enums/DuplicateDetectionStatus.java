package com.uth.quizclear.model.enums;

public enum DuplicateDetectionStatus {
    
    PENDING("pending", "Pending"),
    ACCEPTED("accepted", "Accepted"), 
    REJECTED("rejected", "Rejected"),
    SENT_BACK("sent_back", "Sent Back"),
    MERGED("merged", "Merged"),
    OBSOLETE("obsolete", "Obsolete"); // New status for outdated detections

    private final String value;
    private final String displayName;

    DuplicateDetectionStatus(String value, String displayName) {
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
     * Get DuplicateDetectionStatus from string value
     */
    public static DuplicateDetectionStatus fromValue(String value) {
        for (DuplicateDetectionStatus status : DuplicateDetectionStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown DuplicateDetectionStatus: " + value);
    }

    public boolean isProcessed() {
        return this == ACCEPTED || this == REJECTED || this == MERGED;
    }

    public boolean requiresAction() {
        return this == PENDING || this == SENT_BACK;
    }
}
