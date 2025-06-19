package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.enums.ExamStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByExamStatus(ExamStatus examStatus);
    
}