package com.uth.quizclear.model.entity;

import com.uth.quizclear.model.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Answer entity representing question answer choices
 * Properly normalized with question relationship
 */
@Entity
@Table(name = "answers", 
       indexes = {
           @Index(name = "idx_answer_question", columnList = "question_id"),
           @Index(name = "idx_answer_correct", columnList = "is_correct")
       })
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"question"})
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Answer content is required")
    private String content;

    @Column(name = "is_correct", nullable = false)
    @NotNull(message = "Correct flag is required")
    private Boolean isCorrect;

    @Column(name = "order_index")
    @Min(value = 1, message = "Order index must be at least 1")
    private Integer orderIndex;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, foreignKey = @ForeignKey(name = "fk_answer_question"))
    @NotNull(message = "Question is required")
    private Question question;    @Override
    public Long getId() {
        return answerId;
    }

    // Business methods
    public String getQuestionContent() {
        return question != null ? question.getContent() : null;
    }

    public String getQuestionType() {
        return question != null && question.getQuestionType() != null ? 
               question.getQuestionType().name() : null;
    }

    public String getPreview(int maxLength) {
        if (content == null) return "";
        return content.length() <= maxLength ? content : content.substring(0, maxLength) + "...";
    }

    public String getDisplayText() {
        String prefix = isCorrect ? "✓ " : "✗ ";
        return prefix + getPreview(100);
    }
}
