package com.uth.quizclear.model.enums;

public enum FeedbackStatus {
    NOT_RECEIVED("Not received"),
    RECEIVED("Received");

    private final String value;

    FeedbackStatus(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public static FeedbackStatus fromValue(String value) {
        for (FeedbackStatus status : FeedbackStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown Status: " + value);
    }
}
