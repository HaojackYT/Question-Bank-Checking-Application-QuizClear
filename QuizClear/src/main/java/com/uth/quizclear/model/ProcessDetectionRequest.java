package com.uth.quizclear.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessDetectionRequest {
    private String action;
    private String feedback;
    private Integer processedBy; // Chỉ cần ID của người xử lý
}