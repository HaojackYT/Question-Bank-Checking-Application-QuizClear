package com.uth.quizclear.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDetailDTO {
    private Integer questionId;
    private String content;
    private String courseName;
    private String cloName;
    private String difficultyLevel;
    private String creatorName;
    private String createdAt; // Format to String for DTO
}