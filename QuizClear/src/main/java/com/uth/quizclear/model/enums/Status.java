package com.uth.quizclear.model.enums;

public enum Status {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Status fromValue(String value) {
        for (Status status : Status.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown Status: " + value);
    }
}
