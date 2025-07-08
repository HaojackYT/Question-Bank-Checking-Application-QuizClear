package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.DuplicateDetection;
import com.uth.quizclear.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DuplicationStaffRepository extends JpaRepository<DuplicateDetection, Long> {
      // Use native query with String parameter to bypass converter
    @Query(value = "SELECT * FROM duplicate_detections WHERE status = ?1", nativeQuery = true)
    List<DuplicateDetection> findByStatus(String status);
    
    // Find active detections (excluding obsolete ones)
    @Query(value = "SELECT * FROM duplicate_detections WHERE status = ?1 AND status != 'obsolete' ORDER BY detected_at DESC", nativeQuery = true)
    List<DuplicateDetection> findActiveByStatus(String status);

    @Query(value = "SELECT * FROM duplicate_detections WHERE status = 'pending' AND status != 'obsolete' ORDER BY detected_at DESC", nativeQuery = true)
    List<DuplicateDetection> findActivePendingDetections();
    
    // Derived query based on detectedBy
    List<DuplicateDetection> findByDetectedBy(Long detectedBy);
      
    // Corrected query with JOIN to access course.id via newQuestionId
    @Query("SELECT dd FROM DuplicateDetection dd JOIN Question q ON dd.newQuestionId = q.questionId WHERE q.course.id = :courseId")
    List<DuplicateDetection> findByCourseId(@Param("courseId") Long courseId);
      
    // Get all courses
    @Query("SELECT c FROM Course c")
    List<Course> findAllCourses();
    
    // Get question info for notification (creator_id and content)
    @Query(value = "SELECT q.created_by, q.content FROM questions q WHERE q.question_id = :questionId", nativeQuery = true)
    Object[] getQuestionInfoForNotification(@Param("questionId") Long questionId);      // Delete question by ID with related data
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM questions WHERE question_id = :questionId", nativeQuery = true)
    void deleteQuestionById(@Param("questionId") Long questionId);    // Delete related ai_similarity_results before deleting question (safer approach)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ai_similarity_results WHERE existing_question_id = :questionId", nativeQuery = true)
    void deleteAiSimilarityResultsByExistingQuestionId(@Param("questionId") Long questionId);
    
    // Alternative: Just disable the foreign key constraint temporarily
    @Modifying
    @Transactional
    @Query(value = "SET FOREIGN_KEY_CHECKS=0", nativeQuery = true)
    void disableForeignKeyChecks();
    
    @Modifying
    @Transactional
    @Query(value = "SET FOREIGN_KEY_CHECKS=1", nativeQuery = true)
    void enableForeignKeyChecks();
    
    // Delete related duplicate_detections by question ID (for cleanup)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM duplicate_detections WHERE new_question_id = :questionId OR similar_question_id = :questionId", nativeQuery = true)
    void deleteDuplicateDetectionsByQuestionId(@Param("questionId") Long questionId);
      // Update question status - use enum value to ensure correct format
    @Modifying
    @Transactional
    @Query(value = "UPDATE questions SET status = ?1 WHERE question_id = ?2", nativeQuery = true)
    void updateQuestionStatus(String status, Long questionId);
    
    // Verification methods
    @Query(value = "SELECT COUNT(*) FROM duplicate_detections WHERE detection_id = :detectionId", nativeQuery = true)
    int countDetectionById(@Param("detectionId") Long detectionId);
    
    @Query(value = "SELECT COUNT(*) FROM questions WHERE question_id = :questionId", nativeQuery = true)
    int countQuestionById(@Param("questionId") Long questionId);
}
