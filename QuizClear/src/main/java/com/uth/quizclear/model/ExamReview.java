package com.uth.quizclear.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_type", nullable = false)
    private ReviewType reviewType; 

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ExamReviewStatus status = ExamReviewStatus.PENDING;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Enum definitions
    public enum ReviewType {
        SUBJECT_LEADER("subject_leader"),
        DEPARTMENT_HEAD("department_head"),
        EXAMINATION_DEPARTMENT("examination_department");

        private final String value;

        ReviewType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ExamReviewStatus {
        PENDING("pending"),
        APPROVED("approved"),
        REJECTED("rejected"),
        NEEDS_REVISION("needs_revision");

        private final String value;

        ExamReviewStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}