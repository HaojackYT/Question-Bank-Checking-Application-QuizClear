package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DifficultyLevelConverter implements AttributeConverter<DifficultyLevel, String> {
    @Override
    public String convertToDatabaseColumn(DifficultyLevel attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public DifficultyLevel convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (DifficultyLevel level : DifficultyLevel.values()) {
            if (level.getValue().equalsIgnoreCase(dbData)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown DifficultyLevel: " + dbData);
    }
}