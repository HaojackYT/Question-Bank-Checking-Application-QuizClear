package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, String> {
    @Override
    public String convertToDatabaseColumn(Gender attribute) {
        return attribute != null ? attribute.toValue() : null;
    }

    @Override
    public Gender convertToEntityAttribute(String dbData) {
        return dbData != null ? Gender.fromString(dbData) : null;
    }
}