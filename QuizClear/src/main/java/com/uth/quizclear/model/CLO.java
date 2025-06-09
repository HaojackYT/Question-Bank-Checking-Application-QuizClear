package com.uth.quizclear.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CLO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clo_id")
    private Integer cloId;

    @Column(name = "clo_name", nullable = false)
    private String cloName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "type")
    private String type;

    @Column(name = "percentage")
    private Double percentage;

    @Transient
    private Integer questionsCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status; // Hoặc CLOStatus nếu bạn quyết định tạo enum riêng

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}