package com.uth.quizclear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.uth.quizclear.model.Exam;
import com.uth.quizclear.model.ExamStatus;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {
    List<Exam> findByStatus(ExamStatus status);
}
