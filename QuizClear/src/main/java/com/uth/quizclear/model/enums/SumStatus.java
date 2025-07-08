package com.uth.quizclear.model.enums;

public enum SumStatus {
    COMPLETED("Completed"),
    DRAFT("Draft"),
    PENDING("Pending");

    private final String value;

    SumStatus(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public static SumStatus fromValue(String value) {
        for (SumStatus status : SumStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown Status: " + value);
    }
}
