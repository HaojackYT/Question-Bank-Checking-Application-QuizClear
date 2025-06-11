package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.uth.quizclear.model.enums.DifficultyLevel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "clos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CLO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clo_id")
    private Long cloId;    @NotNull(message = "Course is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotBlank(message = "CLO code is required")
    @Column(name = "clo_code", nullable = false)
    private String cloCode;

    @NotNull(message = "Difficulty level is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "clo_note")
    private String cloNote;

    @Column(name = "clo_description")
    private String cloDescription;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}