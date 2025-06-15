/*package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.DuplicateDetection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DuplicateDetectionRepository extends JpaRepository<DuplicateDetection, Long> {
    
    // Find by status
    List<DuplicateDetection> findByStatus(DuplicateDetection.Status status);
    
    // Find by similarity score threshold
    List<DuplicateDetection> findBySimilarityScoreGreaterThanEqual(BigDecimal threshold);
    
    // Find by question IDs
    List<DuplicateDetection> findByNewQuestionId(Long questionId);
    List<DuplicateDetection> findBySimilarQuestionId(Long questionId);
    
    // Find by AI check ID
    List<DuplicateDetection> findByAiCheckId(Long aiCheckId);
    
    // Find by detected user
    List<DuplicateDetection> findByDetectedBy(Long userId);
    
    // Find by processed user
    List<DuplicateDetection> findByProcessedBy(Long userId);
    
    // Find by date range
    List<DuplicateDetection> findByDetectedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // Find pending detections for a specific question
    @Query("SELECT d FROM DuplicateDetection d WHERE (d.newQuestionId = :questionId OR d.similarQuestionId = :questionId) AND d.status = 'PENDING'")
    List<DuplicateDetection> findPendingDetectionsForQuestion(@Param("questionId") Long questionId);
    
    // Count by status
    long countByStatus(DuplicateDetection.Status status);
    
    // Find high similarity detections (above threshold)
    @Query("SELECT d FROM DuplicateDetection d WHERE d.similarityScore >= :threshold ORDER BY d.similarityScore DESC")
    List<DuplicateDetection> findHighSimilarityDetections(@Param("threshold") BigDecimal threshold);
    
    // Find recent detections
    @Query("SELECT d FROM DuplicateDetection d WHERE d.detectedAt >= :since ORDER BY d.detectedAt DESC")
    List<DuplicateDetection> findRecentDetections(@Param("since") LocalDateTime since);
    
    // Find detections requiring review
    @Query("SELECT d FROM DuplicateDetection d WHERE d.status IN ('PENDING', 'NEEDS_REVIEW') ORDER BY d.similarityScore DESC, d.detectedAt ASC")
    List<DuplicateDetection> findDetectionsNeedingReview();
    
    // Statistics queries
    @Query("SELECT d.status as status, COUNT(d) as count FROM DuplicateDetection d GROUP BY d.status")
    List<Object[]> getStatusStatistics();
    
    @Query("SELECT DATE(d.detectedAt) as date, COUNT(d) as count FROM DuplicateDetection d WHERE d.detectedAt >= :since GROUP BY DATE(d.detectedAt) ORDER BY DATE(d.detectedAt)")
    List<Object[]> getDailyDetectionCounts(@Param("since") LocalDateTime since);
}
    */