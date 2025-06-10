package com.uth.quizclear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.uth.quizclear.model.Exam;
import com.uth.quizclear.model.Exam.ExamStatus;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> { // Đổi từ Integer thành Long
    List<Exam> findByStatus(ExamStatus status);
}