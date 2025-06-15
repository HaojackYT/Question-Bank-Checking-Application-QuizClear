package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.uth.quizclear.model.entity.Question.BlockStatus;

@Converter(autoApply = true)
public class BlockStatusConverter implements AttributeConverter<BlockStatus, String> {
    @Override
    public String convertToDatabaseColumn(BlockStatus attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public BlockStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (BlockStatus s : BlockStatus.values()) {
            if (s.getValue().equalsIgnoreCase(dbData)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown BlockStatus: " + dbData);
    }
}
