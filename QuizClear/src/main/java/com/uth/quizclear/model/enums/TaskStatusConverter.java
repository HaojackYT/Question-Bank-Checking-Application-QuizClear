package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TaskStatusConverter implements AttributeConverter<TaskStatus, String> {

    @Override
    public String convertToDatabaseColumn(TaskStatus taskStatus) {
        if (taskStatus == null) {
            return null;
        }
        
        switch (taskStatus) {
            case PENDING:
                return "pending";
            case IN_PROGRESS:
                return "in_progress";
            case COMPLETED:
                return "completed";
            case CANCELLED:
                return "cancelled";
            default:
                throw new IllegalArgumentException("Unknown TaskStatus: " + taskStatus);
        }
    }

    @Override
    public TaskStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        
        switch (dbData.toLowerCase().trim()) {
            case "pending":
                return TaskStatus.PENDING;
            case "in_progress":
                return TaskStatus.IN_PROGRESS;
            case "completed":
                return TaskStatus.COMPLETED;
            case "cancelled":
                return TaskStatus.CANCELLED;
            default:
                throw new IllegalArgumentException("Unknown database TaskStatus value: " + dbData);
        }
    }
}
