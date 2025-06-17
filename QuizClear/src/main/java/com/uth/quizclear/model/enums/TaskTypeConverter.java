package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TaskTypeConverter implements AttributeConverter<TaskType, String> {
    @Override
    public String convertToDatabaseColumn(TaskType attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public TaskType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TaskType.valueOf(dbData);
    }
}