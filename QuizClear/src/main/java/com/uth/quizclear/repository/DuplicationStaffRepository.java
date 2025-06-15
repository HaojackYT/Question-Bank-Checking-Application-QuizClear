package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.DuplicateDetection;
import com.uth.quizclear.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
      // TODO: Add when Course entity is available
    @Query("SELECT c FROM Course c")
    List<Course> findAllCourses();
}