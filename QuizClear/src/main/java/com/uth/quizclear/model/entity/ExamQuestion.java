package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "exam_questions")
public class ExamQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_question_id")
    private Long examQuestionId;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "question_order", nullable = false)
    private Integer questionOrder;

    @Column(name = "marks", nullable = false, precision = 4, scale = 2)
    private BigDecimal marks = BigDecimal.valueOf(1.00);

    // Constructor mặc định
    public ExamQuestion() {}

    // Constructor đầy đủ
    public ExamQuestion(Long examQuestionId, Exam exam, Question question, Integer questionOrder, BigDecimal marks) {
        this.examQuestionId = examQuestionId;
        this.exam = exam;
        this.question = question;
        this.questionOrder = questionOrder;
        this.marks = marks;
    }

    // Getters
    public Long getExamQuestionId() {
        return examQuestionId;
    }

    public Exam getExam() {
        return exam;
    }

    public Question getQuestion() {
        return question;
    }

    public Integer getQuestionOrder() {
        return questionOrder;
    }

    public BigDecimal getMarks() {
        return marks;
    }

    // Setters
    public void setExamQuestionId(Long examQuestionId) {
        this.examQuestionId = examQuestionId;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setQuestionOrder(Integer questionOrder) {
        this.questionOrder = questionOrder;
    }

    public void setMarks(BigDecimal marks) {
        this.marks = marks;
    }

    // Utility methods
    public String getQuestionContent() {
        return question != null ? question.getContent() : "No content";
    }

    public String getDifficultyTag() {
        if (question != null && question.getDifficultyLevel() != null) {
            switch (question.getDifficultyLevel().getValue().toLowerCase()) {
                case "recognition":
                    return "remember";
                case "comprehension":
                    return "understand";
                case "basic application":
                    return "apply-basic";
                case "advanced application":
                    return "apply-advance";
                default:
                    return "unknown";
            }
        }
        return "unknown";
    }

    public String getCloTag() {
        if (question != null && question.getClo() != null) {
            return question.getClo().getCloCode().split("\\.")[0];
        }
        return "Unknown";
    }

    public String getCorrectAnswer() {
        return question != null ? question.getAnswerKey() : "N/A";
    }

    public String getIncorrectAnswer1() {
        return question != null ? question.getAnswerF1() : "N/A";
    }

    public String getIncorrectAnswer2() {
        return question != null ? question.getAnswerF2() : "N/A";
    }

    public String getIncorrectAnswer3() {
        return question != null ? question.getAnswerF3() : "N/A";
    }
}