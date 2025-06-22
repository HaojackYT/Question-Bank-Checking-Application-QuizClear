package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.model.enums.QuestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; 


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
    
    // Basic finders
    Optional<Question> findByContent(String content);
    
    // Find by CLO
    List<Question> findByClo_CloId(Long cloId);
    Integer countByClo_CloId(Long cloId);      // Find by creator
    List<Question> findByCreatedBy_UserId(Long creatorId);
    
    // Find by course
    List<Question> findByCourse_CourseId(Long courseId);
    
    // Find by course and creator
    List<Question> findByCourse_CourseIdAndCreatedBy_UserId(Long courseId, Long creatorId);
    
    // Find by task
    List<Question> findByTaskId(Long taskId);
    
    // Find by status
    List<Question> findByStatus(QuestionStatus status);
    
    // Find by difficulty level
    List<Question> findByDifficultyLevel(DifficultyLevel difficultyLevel);
    
    // Find by date range
    List<Question> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // Advanced queries for duplicate detection
    @Query("SELECT q FROM Question q WHERE q.course.courseId = :courseId AND q.status = 'APPROVED' ORDER BY q.createdAt DESC")
    List<Question> findApprovedQuestionsByCourse(@Param("courseId") Long courseId);
      @Query("SELECT q FROM Question q WHERE q.createdBy.userId = :userId AND q.status = 'APPROVED' ORDER BY q.createdAt DESC")
    List<Question> findApprovedQuestionsByCreator(@Param("userId") Long userId);
    
    // Find questions for duplicate checking (excluding the question being checked)
    @Query("SELECT q FROM Question q WHERE q.questionId != :excludeId AND q.course.courseId = :courseId AND q.status = 'APPROVED'")
    List<Question> findQuestionsForDuplicateCheck(@Param("excludeId") Long excludeId, @Param("courseId") Long courseId);
    
    // Find similar content questions (for initial text-based filtering)
    @Query("SELECT q FROM Question q WHERE q.questionId != :excludeId AND LOWER(q.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Question> findQuestionsWithSimilarContent(@Param("excludeId") Long excludeId, @Param("keyword") String keyword);
      // Statistics queries
    @Query("SELECT COUNT(q) FROM Question q WHERE q.createdBy.userId = :userId")
    long countQuestionsByCreator(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(q) FROM Question q WHERE q.course.courseId = :courseId")
    long countQuestionsByCourse(@Param("courseId") Long courseId);
    
    @Query("SELECT q.status as status, COUNT(q) as count FROM Question q GROUP BY q.status")
    List<Object[]> getQuestionStatusStatistics();
    
    @Query("SELECT q.difficultyLevel as level, COUNT(q) as count FROM Question q GROUP BY q.difficultyLevel")
    List<Object[]> getQuestionDifficultyStatistics();
    
    @Query("SELECT c.courseName as courseName, COUNT(q) as count FROM Question q JOIN q.course c GROUP BY c.courseName")
    List<Object[]> getQuestionDistributionByCourse();
    
    @Query("SELECT q FROM Question q JOIN q.course c WHERE c.department = :department AND q.feedback IS NOT NULL")
    List<Question> findQuestionsWithFeedbackByDepartment(@Param("department") String department);
    
    // Recent questions
    @Query("SELECT q FROM Question q WHERE q.createdAt >= :since ORDER BY q.createdAt DESC")
    List<Question> findRecentQuestions(@Param("since") LocalDateTime since);
}