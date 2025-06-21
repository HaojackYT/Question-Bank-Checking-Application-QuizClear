package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.enums.ExamStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByExamStatus(ExamStatus examStatus);

    // Methods for Subject Leader feedback management
    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department = :department AND e.feedback IS NOT NULL AND e.feedback != '' ORDER BY e.submittedAt DESC")
    List<Exam> findExamsWithFeedbackByDepartment(@Param("department") String department);
    
    @Query("SELECT e FROM Exam e WHERE e.feedback IS NOT NULL AND e.feedback != '' ORDER BY e.submittedAt DESC")
    List<Exam> findAllExamsWithFeedback();

}