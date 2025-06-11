package com.uth.quizclear.model.enums;

/**
 * Block status for questions
 */
public enum BlockStatus {
    ACTIVE("active"),
    BLOCK("block");

    private final String value;

    BlockStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
