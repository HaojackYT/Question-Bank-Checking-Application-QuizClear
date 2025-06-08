package com.uth.quizclear.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "duplicate_detections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateDetection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detection_id")
    private Integer detectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_question_id")
    private Question newQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "similar_question_id")
    private Question similarQuestion;

    @Column(name = "similarity_score")
    private Double similarityScore;

    @Column(name = "status")
    private String status; // pending, accepted, rejected, sent_back, merged

    @Column(name = "action")
    private String action; // accept, reject, send_back, merged

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detected_by")
    private User detectedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @Column(name = "detected_at")
    private LocalDateTime detectedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;
}