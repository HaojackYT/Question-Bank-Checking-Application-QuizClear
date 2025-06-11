package com.uth.quizclear.model.enums;

/**
 * Actions that can be taken on duplicate detections
 */
public enum DuplicateAction {
    ACCEPT("accept", "Accept as duplicate"),
    REJECT("reject", "Reject - not duplicate"),
    SEND_BACK("send_back", "Send back for revision"),
    MERGE("merge", "Merge questions");

    private final String value;
    private final String description;

    DuplicateAction(String value, String description) {
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
     * Check if action requires additional processing
     */
    public boolean requiresProcessing() {
        return this == MERGE || this == SEND_BACK;
    }

    /**
     * Check if action is final (no further action needed)
     */
    public boolean isFinal() {
        return this == ACCEPT || this == REJECT;
    }

    /**
     * Get next possible actions after this action
     */
    public DuplicateAction[] getNextPossibleActions() {
        return switch (this) {
            case SEND_BACK -> new DuplicateAction[]{ACCEPT, REJECT, MERGE};
            case MERGE -> new DuplicateAction[]{ACCEPT, REJECT};
            default -> new DuplicateAction[]{};
        };
    }
}
