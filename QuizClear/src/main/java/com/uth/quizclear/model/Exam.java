package com.uth.quizclear.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "EXAMS")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "exam_id")
    private Integer examId; // ID đề thi

    @Column(name = "course_id", nullable = false)
    private Integer courseId; // Khóa học liên quan

    @Column(name = "plan_id")
    private Integer planId; // Kế hoạch đề thi

    @Column(name = "exam_title", nullable = false)
    private String examTitle; // Tiêu đề đề thi

    @Column(name = "exam_code", unique = true)
    private String examCode; // Mã đề thi

    @Column(name = "duration_minutes")
    private Integer durationMinutes; // Thời gian làm bài (phút)

    @JdbcTypeCode(SqlTypes.JSON) // Ánh xạ kiểu JSON từ MySQL
    @Column(name = "difficulty_distribution", columnDefinition = "json")
    private String difficultyDistribution; // Phân bố độ khó (dạng JSON string)

    @Enumerated(EnumType.STRING) // Ánh xạ Enum sang String trong cơ sở dữ liệu
    @Column(name = "status", nullable = false)
    private ExamStatus status = ExamStatus.DRAFT; // Trạng thái đề thi

    @Column(name = "created_by")
    private Integer createdBy; // Người tạo đề thi

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now(); // Ngày tạo

    @Column(name = "hidden", nullable = false)
    private Boolean hidden = true; // Ẩn/hiện đề thi

    // Enum cho trạng thái đề thi
    public enum ExamStatus {
        DRAFT,      // Bản nháp
        SUBMITTED,  // Đã nộp
        APPROVED,   // Đã duyệt
        FINALIZED   // Đã hoàn tất
    }

    public Integer getExamId() { return examId; }
    public void setExamId(Integer examId) { this.examId = examId; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public Integer getPlanId() { return planId; }
    public void setPlanId(Integer planId) { this.planId = planId; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public String getExamCode() { return examCode; }
    public void setExamCode(String examCode) { this.examCode = examCode; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getDifficultyDistribution() { return difficultyDistribution; }
    public void setDifficultyDistribution(String difficultyDistribution) { this.difficultyDistribution = difficultyDistribution; }

    public ExamStatus getStatus() { return status; }
    public void setStatus(ExamStatus status) { this.status = status; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean getHidden() { return hidden; }
    public void setHidden(Boolean hidden) { this.hidden = hidden; }
}