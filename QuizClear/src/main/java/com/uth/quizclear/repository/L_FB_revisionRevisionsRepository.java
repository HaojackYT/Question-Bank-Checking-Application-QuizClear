package com.uth.quizclear.repository;

import com.uth.quizclear.model.dto.L_FB_revisionRevisionsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uth.quizclear.model.entity.Comment;

import java.util.List;

@Repository
public interface L_FB_revisionRevisionsRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT new com.uth.quizclear.model.dto.L_FB_revisionRevisionsDTO(" +
           "q.questionId, c.courseName, q.content, com.content, CAST(q.status AS string), CAST(q.difficultyLevel AS string), q.submittedAt) " +
           "FROM Question q " +
           "JOIN q.course c " +
           "JOIN Comment com ON CAST(com.entityId AS long) = q.questionId AND com.entityType = 'question' " +
           "WHERE q.status = 'rejected' AND q.createdBy.userId = :lecturerId")
    List<L_FB_revisionRevisionsDTO> findRejectedQuestionsWithCommentsByLecturer(Long lecturerId);
}