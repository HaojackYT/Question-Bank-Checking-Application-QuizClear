package com.uth.quizclear.model.enums;

public enum commentType {
    QUESTION("question"),
    EXAM("exam"),
    TASK("task"),
    PLAN("plan"),
    SUBMISSION("submission");

    private final String value;

    commentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
