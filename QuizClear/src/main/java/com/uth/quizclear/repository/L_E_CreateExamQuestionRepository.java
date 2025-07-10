package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface L_E_CreateExamQuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q WHERE q.course.courseId = :courseId AND q.status = 'APPROVED'")
    List<Question> findApprovedQuestionsByCourseId(Long courseId);
}