package com.uth.quizclear.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

@Entity
@Table(name = "ExamReviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_type", nullable = false)
    private ReviewType reviewType; 

    // TODO: DEFAULT 'PENDING'
    @Enumerated(EnumType.STRING)
    private ExamReviewStatus status;

    private Double score;

    private String comments;

    private String suggestions;

    // TODO: DEFAULT CURRENT_TIMESTAMP
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // TODO: DEFAULT NULL
    // TODO: ON UPDATE CURRENT_TIMESTAMP
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
