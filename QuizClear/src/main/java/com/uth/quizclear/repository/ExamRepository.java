package com.uth.quizclear.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.uth.quizclear.model.Exam;


public interface ExamRepository extends JpaRepository<Exam, Integer> {

    // Tìm kiếm đề thi theo tiêu đề (không phân biệt chữ hoa, chữ thường)
    List<Exam> findByExamTitleContainingIgnoreCase(String examTitle);

    // Đếm số lượng đề thi theo trạng thái
    long countByStatus(Exam.ExamStatus status);

    // Tìm kiếm đề thi theo trạng thái
    List<Exam> findByStatus(Exam.ExamStatus status);

    // Tìm kiếm đề thi theo courseId
    List<Exam> findByCourseId(Long courseId);

    // Tìm kiếm đề thi theo createdBy
    List<Exam> findByCreatedBy(Long createdBy);
    
}
