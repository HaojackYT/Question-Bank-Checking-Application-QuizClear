package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.AiDuplicateCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AiDuplicateCheckRepository extends JpaRepository<AiDuplicateCheck, Long> {
    
    // Find by status
    List<AiDuplicateCheck> findByStatus(AiDuplicateCheck.Status status);
    
    // Find by question content and course
    Optional<AiDuplicateCheck> findByQuestionContentAndCourse_CourseId(String questionContent, Long courseId);
    
    // Find by course
    List<AiDuplicateCheck> findByCourse_CourseId(Long courseId);
    
    // Find by checked by user
    List<AiDuplicateCheck> findByCheckedBy_UserId(Long userId);
    
    // Find by duplicate found flag
    List<AiDuplicateCheck> findByDuplicateFound(Boolean duplicateFound);
    
    // Find by model used
    List<AiDuplicateCheck> findByModelUsed(String modelUsed);
    
    // Find recent checks
    @Query("SELECT a FROM AiDuplicateCheck a WHERE a.checkedAt >= :since ORDER BY a.checkedAt DESC")
    List<AiDuplicateCheck> findRecentChecks(@Param("since") LocalDateTime since);
    
    // Find checks with duplicates found
    @Query("SELECT a FROM AiDuplicateCheck a WHERE a.duplicateFound = true ORDER BY a.maxSimilarityScore DESC")
    List<AiDuplicateCheck> findChecksWithDuplicates();
    
    // Find pending checks
    @Query("SELECT a FROM AiDuplicateCheck a WHERE a.status = 'pending' ORDER BY a.checkedAt ASC")
    List<AiDuplicateCheck> findPendingChecks();
    
    // Find completed checks
    @Query("SELECT a FROM AiDuplicateCheck a WHERE a.status = 'completed' ORDER BY a.checkedAt DESC")
    List<AiDuplicateCheck> findCompletedChecks();
    
    // Find error checks
    @Query("SELECT a FROM AiDuplicateCheck a WHERE a.status = 'error' ORDER BY a.checkedAt DESC")
    List<AiDuplicateCheck> findErrorChecks();
    
    // Count by status
    long countByStatus(AiDuplicateCheck.Status status);
    
    // Count by course
    long countByCourse_CourseId(Long courseId);
    
    // Statistics queries
    @Query("SELECT a.status as status, COUNT(a) as count FROM AiDuplicateCheck a GROUP BY a.status")
    List<Object[]> getStatusStatistics();
    
    @Query("SELECT c.courseName as courseName, COUNT(a) as count FROM AiDuplicateCheck a JOIN a.course c GROUP BY c.courseName ORDER BY COUNT(a) DESC")
    List<Object[]> getChecksByCourse();
    
    @Query("SELECT DATE(a.checkedAt) as date, COUNT(a) as count FROM AiDuplicateCheck a WHERE a.checkedAt >= :since GROUP BY DATE(a.checkedAt) ORDER BY DATE(a.checkedAt)")
    List<Object[]> getDailyCheckCounts(@Param("since") LocalDateTime since);
    
    // Performance queries
    @Query("SELECT AVG(a.maxSimilarityScore) FROM AiDuplicateCheck a WHERE a.maxSimilarityScore IS NOT NULL")
    Double getAverageSimilarityScore();
    
    @Query("SELECT a.modelUsed as model, COUNT(a) as count, AVG(a.maxSimilarityScore) as avgScore FROM AiDuplicateCheck a GROUP BY a.modelUsed")
    List<Object[]> getModelPerformance();
}
