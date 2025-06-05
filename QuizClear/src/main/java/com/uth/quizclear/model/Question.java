package com.uth.quizclear.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String difficultyLevel;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String answerKey;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String blockQuestion;
    private Boolean hiddenQuestion;
}
