package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface L_E_CreateExamExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
}