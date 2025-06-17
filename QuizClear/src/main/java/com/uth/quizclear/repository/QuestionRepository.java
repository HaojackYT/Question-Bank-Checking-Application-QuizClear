package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.model.enums.QuestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    // Basic finders
    Optional<Question> findByContent(String content);
    
    // Find by CLO
    List<Question> findByClo_CloId(Long cloId);
    Integer countByClo_CloId(Long cloId);
      // Find by creator
    List<Question> findByCreatedBy_UserId(Long creatorId);
    
    // Find by course
    List<Question> findByCourse_CourseId(Long courseId);
    
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
    
    // Recent questions
    @Query("SELECT q FROM Question q WHERE q.createdAt >= :since ORDER BY q.createdAt DESC")
    List<Question> findRecentQuestions(@Param("since") LocalDateTime since);
    
    // Questions pending approval
    @Query("SELECT q FROM Question q WHERE q.status = 'PENDING' ORDER BY q.createdAt ASC")
    List<Question> findPendingQuestions();
      // Top creators
    @Query("SELECT u.fullName as creatorName, COUNT(q) as questionCount FROM Question q JOIN q.createdBy u GROUP BY u.userId, u.fullName ORDER BY COUNT(q) DESC")
    List<Object[]> getTopQuestionCreators();
    
    // Course-wise question distribution
    @Query("SELECT c.courseName as courseName, COUNT(q) as questionCount FROM Question q JOIN q.course c GROUP BY c.courseId, c.courseName ORDER BY COUNT(q) DESC")
    List<Object[]> getQuestionDistributionByCourse();

    List<Question> findByTaskId(Long taskId);
}