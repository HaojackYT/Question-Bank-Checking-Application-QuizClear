package com.uth.quizclear.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateDetectionDTO {
    private Integer detectionId;
    private String newQuestion;
    private String similarQuestion;
    private Double similarityScore;
    private String similarityLabel;
    private String courseName;
    private String submitterName;
    private String status;
}