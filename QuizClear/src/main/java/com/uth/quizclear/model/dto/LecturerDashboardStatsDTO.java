package com.uth.quizclear.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturerDashboardStatsDTO {
    private long questionSubmitted;
    private long questionApproved;
    private long questionReturned;
}
