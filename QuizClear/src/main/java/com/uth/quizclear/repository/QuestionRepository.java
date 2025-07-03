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
    // Find by content containing (case-insensitive)
  List<Question> findByContentContainingIgnoreCase(String content);
  
  // Find by subject/course name containing (case-insensitive)
  @Query("SELECT q FROM Question q JOIN q.course c WHERE LOWER(c.courseName) LIKE LOWER(CONCAT('%', :subject, '%'))")
  List<Question> findBySubjectContainingIgnoreCase(@Param("subject") String subject);
    // Find by correct answer containing (case-insensitive)
  List<Question> findByAnswerKeyContainingIgnoreCase(String answerKey);

  // Find by CLO
  List<Question> findByClo_CloId(Long cloId);

  Integer countByClo_CloId(Long cloId); // Find by creator

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

  // Methods for Lecturer Dashboard
  long countByCreatedByAndStatus(com.uth.quizclear.model.entity.User createdBy, QuestionStatus status);

  List<Question> findTop10ByCreatedByOrderByCreatedAtDesc(com.uth.quizclear.model.entity.User createdBy);

  // New methods for department and subject scope filtering
  @Query("SELECT q FROM Question q JOIN q.course c WHERE c.department = :departmentName")
  List<Question> findByDepartmentScope(@Param("departmentName") String departmentName);

  // Note: No direct relation between Course and Subject, so we query by subject code/name in course department
  @Query("SELECT q FROM Question q JOIN q.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%')")
  List<Question> findBySubjectScope(@Param("subjectName") String subjectName);

  @Query("SELECT q FROM Question q JOIN q.course c WHERE c.department = :departmentName AND q.status = :status")
  List<Question> findByDepartmentScopeAndStatus(@Param("departmentName") String departmentName, @Param("status") QuestionStatus status);

  @Query("SELECT q FROM Question q JOIN q.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%') AND q.status = :status")
  List<Question> findBySubjectScopeAndStatus(@Param("subjectName") String subjectName, @Param("status") QuestionStatus status);

  // Enhanced queries for permission-based access using department name
  @Query("SELECT q FROM Question q JOIN q.course c WHERE c.department = :departmentName")
  List<Question> findQuestionsAccessibleByDepartment(@Param("departmentName") String departmentName);

  @Query("SELECT q FROM Question q JOIN q.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%')")
  List<Question> findQuestionsAccessibleBySubject(@Param("subjectName") String subjectName);

  // Duplicate check scope queries
  @Query("SELECT q FROM Question q JOIN q.course c WHERE c.department = :departmentName AND q.questionId != :excludeId AND q.status = 'APPROVED'")
  List<Question> findQuestionsForDepartmentDuplicateCheck(@Param("departmentName") String departmentName, @Param("excludeId") Long excludeId);

  @Query("SELECT q FROM Question q JOIN q.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%') AND q.questionId != :excludeId AND q.status = 'APPROVED'")
  List<Question> findQuestionsForSubjectDuplicateCheck(@Param("subjectName") String subjectName, @Param("excludeId") Long excludeId);

  // Global duplicate check (all approved questions)
  @Query("SELECT q FROM Question q WHERE q.questionId != :excludeId AND q.status = 'APPROVED'")
  List<Question> findQuestionsForGlobalDuplicateCheck(@Param("excludeId") Long excludeId);

  // Statistics for scope-based access
  @Query("SELECT COUNT(q) FROM Question q JOIN q.course c WHERE c.department = :departmentName")
  long countQuestionsByDepartmentScope(@Param("departmentName") String departmentName);

  @Query("SELECT COUNT(q) FROM Question q JOIN q.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%')")
  long countQuestionsBySubjectScope(@Param("subjectName") String subjectName);

  // Find questions by difficulty level within scope
  @Query("SELECT q FROM Question q JOIN q.course c WHERE c.department = :departmentName AND q.difficultyLevel = :difficulty")
  List<Question> findByDepartmentScopeAndDifficulty(@Param("departmentName") String departmentName, @Param("difficulty") DifficultyLevel difficulty);

  @Query("SELECT q FROM Question q JOIN q.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%') AND q.difficultyLevel = :difficulty")
  List<Question> findBySubjectScopeAndDifficulty(@Param("subjectName") String subjectName, @Param("difficulty") DifficultyLevel difficulty);

  long countByTaskIdAndStatus(Integer taskId, QuestionStatus status);
}
