package com.uth.quizclear.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturerDashboardActivityDTO {
    private Long questionId;
    private String activityText;
    private String activityCaption;
    private String status;
    private String statusClass;
    private String date;
}
