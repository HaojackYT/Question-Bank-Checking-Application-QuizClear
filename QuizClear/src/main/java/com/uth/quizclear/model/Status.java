package com.uth.quizclear.model;

public enum Status {
    ACTIVE("active"),
    INACTIVE("inactive");
    
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