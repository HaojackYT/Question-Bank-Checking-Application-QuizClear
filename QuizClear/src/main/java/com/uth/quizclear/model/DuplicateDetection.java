package com.uth.quizclear.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "DuplicateDetections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateDetection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detectionId;

    @ManyToOne
    @JoinColumn(name = "new_question_id")
    private Question newQuestion;

    @ManyToOne
    @JoinColumn(name = "similar_question_id")
    private Question similarQuestion;

    private Double similarityScore;

    private String status;
    private String action;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "detected_by")
    private User detectedBy;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private User processedBy;

    private Date detectedAt;
    private Date processedAt;
}
