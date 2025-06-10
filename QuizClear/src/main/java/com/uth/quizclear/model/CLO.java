package com.uth.quizclear.model;

import jakarta.persistence.*;
import lombok.*;

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
    private Integer cloId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "clo_code", nullable = false)
    private String cloCode;

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

    public enum DifficultyLevel {
        recognition, comprehension, Basic_Application, Advanced_Application
    }
}