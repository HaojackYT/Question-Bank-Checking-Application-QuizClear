package com.uth.quizclear.model.entity;

import java.time.LocalDateTime;

import com.uth.quizclear.model.enums.FeedbackStatus;
import com.uth.quizclear.model.enums.FeedbackStatusConverter;
import com.uth.quizclear.model.enums.SumStatus;
import com.uth.quizclear.model.enums.SumStatusConverter;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "summary")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SummaryReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="summary_id")
    private Long sumId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Convert(converter = FeedbackStatusConverter.class)
    @Column(name = "feedback_status")
    private FeedbackStatus feedbackStatus;

    @Convert(converter = SumStatusConverter.class)
    @Column(name = "status")
    private SumStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
