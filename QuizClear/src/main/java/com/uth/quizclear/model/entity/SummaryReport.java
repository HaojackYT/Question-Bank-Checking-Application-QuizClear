package com.uth.quizclear.model.entity;

import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "summaryReport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SummaryQuestion> summaryQuestions;

    
    public Long getSumId() {
        return sumId;
    }

    public void setSumId(Long sumId) {
        this.sumId = sumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public User getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(User assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public FeedbackStatus getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(FeedbackStatus feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public SumStatus getStatus() {
        return status;
    }

    public void setStatus(SumStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<SummaryQuestion> getSummaryQuestions() {
        return summaryQuestions;
    }

    public void setSummaryQuestions(List<SummaryQuestion> summaryQuestions) {
        this.summaryQuestions = summaryQuestions;
    }
}
