package com.uth.quizclear.model.enums;

import com.uth.quizclear.model.entity.Plan.PlanStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PlanStatusConverter implements AttributeConverter<PlanStatus, String> {
    @Override
    public String convertToDatabaseColumn(PlanStatus attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public PlanStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (PlanStatus s : PlanStatus.values()) {
            if (s.getValue().equalsIgnoreCase(dbData)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown PlanStatus: " + dbData);
    }
}