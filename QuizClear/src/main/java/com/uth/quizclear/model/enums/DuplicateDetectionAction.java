package com.uth.quizclear.model.enums;

public enum DuplicateDetectionAction {
    ACCEPT("accept", "Accept"),
    REJECT("reject", "Reject"),
    SEND_BACK("send_back", "Send Back"),
    MERGE("merge", "Merge"),
    KEEP_BOTH("keep_both", "Keep Both"),
    REMOVE_NEW("remove_new", "Remove New");

    private final String value;
    private final String displayName;

    DuplicateDetectionAction(String value, String displayName) {
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
     * Get DuplicateDetectionAction from string value
     */
    public static DuplicateDetectionAction fromValue(String value) {
        if (value == null) {
            return null; // Action can be null
        }

        for (DuplicateDetectionAction action : DuplicateDetectionAction.values()) {
            if (action.value.equals(value)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Unknown DuplicateDetectionAction: " + value);
    }

    String getContent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContent'");
    }
}
