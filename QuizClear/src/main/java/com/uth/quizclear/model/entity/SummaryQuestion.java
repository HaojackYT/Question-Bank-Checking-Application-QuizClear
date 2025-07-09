package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "summary_question")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SummaryQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "summary_id", nullable = false)
    private SummaryReport summaryReport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public SummaryReport getSummaryReport() {
        return summaryReport;
    }
    public void setSummaryReport(SummaryReport summaryReport) {
        this.summaryReport = summaryReport;
    }
    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }
}
