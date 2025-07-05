package com.uth.quizclear.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uth.quizclear.model.entity.ExamQuestion;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
    List<ExamQuestion> findByExam_ExamIdOrderByQuestionOrder(Long examId);
    List<ExamQuestion> findByExamExamId(Long examId);

    List<ExamQuestion> findByExamOrderByQuestionOrderAsc(com.uth.quizclear.model.entity.Exam exam);
}
