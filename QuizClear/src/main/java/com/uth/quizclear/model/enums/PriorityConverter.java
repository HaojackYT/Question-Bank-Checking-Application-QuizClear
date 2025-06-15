package com.uth.quizclear.model.enums;

import com.uth.quizclear.model.entity.Plan.Priority;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PriorityConverter implements AttributeConverter<Priority, String> {
    @Override
    public String convertToDatabaseColumn(Priority attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public Priority convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (Priority p : Priority.values()) {
            if (p.getValue().equalsIgnoreCase(dbData)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown Priority: " + dbData);
    }
}