package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PriorityConverter implements AttributeConverter<Priority, String> {
    
    @Override
    public String convertToDatabaseColumn(Priority priority) {
        if (priority == null) {
            return null;
        }
        return priority.name().toLowerCase();
    }

    @Override
    public Priority convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
          try {
            // Convert to lowercase to match enum constants
            return Priority.valueOf(dbData.toLowerCase().trim());
        } catch (IllegalArgumentException e) {
            // If conversion fails, return default value
            return Priority.medium;
        }
    }
}