package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.uth.quizclear.model.enums.ReviewType;
import com.uth.quizclear.model.enums.ReviewTypeConverter;
import com.uth.quizclear.model.enums.ExamReviewStatus;
import com.uth.quizclear.model.enums.ExamReviewStatusConverter;

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

    @Convert(converter = ReviewTypeConverter.class)
    @Column(name = "review_type", nullable = false)
    private ReviewType reviewType;

    @Convert(converter = ExamReviewStatusConverter.class)
    @Column(name = "status")
    private ExamReviewStatus status = ExamReviewStatus.PENDING;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "due_date")
    private LocalDateTime dueDate;
}
