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
    Object[] getQuestionInfoForNotification(@Param("questionId") Long questionId);
      // Delete question by ID
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM questions WHERE question_id = :questionId", nativeQuery = true)
    void deleteQuestionById(@Param("questionId") Long questionId);
    
    // Update question status
    @Modifying
    @Transactional
    @Query(value = "UPDATE questions SET status = :status WHERE question_id = :questionId", nativeQuery = true)
    void updateQuestionStatus(@Param("questionId") Long questionId, @Param("status") String status);
}