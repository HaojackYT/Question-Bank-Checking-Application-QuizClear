package com.uth.quizclear.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessDetectionRequest {
    private String action;
    private String feedback;
    private Long processedBy; // Chỉ cần ID của người xử lý
}