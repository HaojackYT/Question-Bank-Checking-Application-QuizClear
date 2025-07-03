package com.uth.quizclear.repository;

import com.uth.quizclear.model.dto.L_FB_revisionEditQuestionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uth.quizclear.model.entity.Question;

import java.util.Optional;

@Repository
public interface L_FB_revisionEditQuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT new com.uth.quizclear.model.dto.L_FB_revisionEditQuestionDTO(" +
           "q.questionId, c.courseName, CAST(q.difficultyLevel AS string), q.content, " +
           "q.answerKey, q.answerF1, q.answerF2, q.answerF3, q.explanation, com.content) " +
           "FROM Question q " +
           "JOIN q.course c " +
           "LEFT JOIN Comment com ON CAST(com.entityId AS long) = q.questionId AND com.entityType = 'question' " +
           "WHERE q.questionId = :questionId AND q.status = 'rejected'")
    Optional<L_FB_revisionEditQuestionDTO> findQuestionWithFeedbackById(Long questionId);
}