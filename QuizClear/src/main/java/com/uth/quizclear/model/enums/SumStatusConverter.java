package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SumStatusConverter implements AttributeConverter<SumStatus, String> {

    @Override
    public String convertToDatabaseColumn(SumStatus attribute) {
        if (attribute == null) return null;
        return attribute.getValue();
    }

    @Override
    public SumStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return SumStatus.fromValue(dbData);
    }
}
