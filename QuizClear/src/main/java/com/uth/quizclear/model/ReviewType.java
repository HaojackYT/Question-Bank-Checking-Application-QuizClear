package com.uth.quizclear.model;

public enum ReviewType {
    SUBJECT_LEADER("subject_leader"),
    DEPARTMENT_HEAD("department_head"), 
    EXAMINATION_DEPARTMENT("examination_department");
    
    private final String value;
    
    ReviewType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static ReviewType fromValue(String value) {
        for (ReviewType type : ReviewType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ReviewType: " + value);
    }
}