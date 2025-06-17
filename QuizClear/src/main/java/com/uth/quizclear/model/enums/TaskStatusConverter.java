package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TaskStatusConverter implements AttributeConverter<TaskStatus, String> {
    @Override
    public String convertToDatabaseColumn(TaskStatus attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public TaskStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TaskStatus.valueOf(dbData);
    }
}