package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.DuplicateDetection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DuplicateDetectionRepository extends JpaRepository<DuplicateDetection, Long> {    // Find by similarity score threshold
    List<DuplicateDetection> findBySimilarityScoreGreaterThanEqual(BigDecimal threshold);

    // Find by question IDs
    List<DuplicateDetection> findByNewQuestionId(Long questionId);

    List<DuplicateDetection> findBySimilarQuestionId(Long questionId);

    // Find by AI check ID
    List<DuplicateDetection> findByAiCheckId(Long aiCheckId);

    // Find by detected user
    List<DuplicateDetection> findByDetectedBy(Long userId);

    // Lấy top 5 cảnh báo trùng lặp mới nhất của staff (detected_by)
    List<DuplicateDetection> findTop5ByDetectedByOrderByDetectedAtDesc(Long userId);

    // Find by processed user
    List<DuplicateDetection> findByProcessedBy(Long userId);

    // Find by date range
    List<DuplicateDetection> findByDetectedAtBetween(LocalDateTime start, LocalDateTime end);    // Find pending detections for a specific question
    @Query("SELECT d FROM DuplicateDetection d WHERE (d.newQuestionId = :questionId OR d.similarQuestionId = :questionId) AND d.statusString = 'pending'")
    List<DuplicateDetection> findPendingDetectionsForQuestion(@Param("questionId") Long questionId);

    // Count by status string
    @Query("SELECT COUNT(d) FROM DuplicateDetection d WHERE d.statusString = :status")
    long countByStatusString(@Param("status") String status);

    // Find high similarity detections (above threshold)
    @Query("SELECT d FROM DuplicateDetection d WHERE d.similarityScore >= :threshold ORDER BY d.similarityScore DESC")
    List<DuplicateDetection> findHighSimilarityDetections(@Param("threshold") BigDecimal threshold);

    // Find recent detections
    @Query("SELECT d FROM DuplicateDetection d WHERE d.detectedAt >= :since ORDER BY d.detectedAt DESC")
    List<DuplicateDetection> findRecentDetections(@Param("since") LocalDateTime since);

    // Find detections requiring review
    @Query("SELECT d FROM DuplicateDetection d WHERE d.statusString IN ('pending', 'needs_review') ORDER BY d.similarityScore DESC, d.detectedAt ASC")
    List<DuplicateDetection> findDetectionsNeedingReview();    // Statistics queries
    @Query("SELECT d.statusString as status, COUNT(d) as count FROM DuplicateDetection d GROUP BY d.statusString")
    List<Object[]> getStatusStatistics();    @Query("SELECT DATE(d.detectedAt) as date, COUNT(d) as count FROM DuplicateDetection d WHERE d.detectedAt >= :since GROUP BY DATE(d.detectedAt) ORDER BY DATE(d.detectedAt)")
    List<Object[]> getDailyDetectionCounts(@Param("since") LocalDateTime since);

    // Find by status string
    @Query("SELECT d FROM DuplicateDetection d WHERE d.statusString = :status")
    List<DuplicateDetection> findByStatusString(@Param("status") String status);    // Find detections for questions in scope
    @Query("SELECT dd FROM DuplicateDetection dd WHERE dd.newQuestionId IN :newIds OR dd.similarQuestionId IN :similarIds")
    List<DuplicateDetection> findByNewQuestionIdInOrSimilarQuestionIdIn(@Param("newIds") List<Long> newIds, @Param("similarIds") List<Long> similarIds);    // Cleanup methods for outdated detections
    @Modifying
    @Transactional
    @Query("DELETE FROM DuplicateDetection d WHERE d.aiCheckId = :aiCheckId AND d.statusString = 'pending'")
    void deleteByAiCheckIdAndStatusPending(@Param("aiCheckId") Long aiCheckId);

    @Modifying
    @Transactional
    @Query("UPDATE DuplicateDetection d SET d.statusString = 'obsolete', d.updatedAt = :now WHERE d.aiCheckId = :aiCheckId AND d.statusString = 'pending'")
    void markObsoleteByAiCheckIdAndStatusPending(@Param("aiCheckId") Long aiCheckId, @Param("now") LocalDateTime now);    // Find active detections (excluding obsolete ones)
    @Query("SELECT d FROM DuplicateDetection d WHERE d.statusString = :status AND d.statusString != 'obsolete' ORDER BY d.detectedAt DESC")
    List<DuplicateDetection> findActiveByStatus(@Param("status") String status);

    @Query("SELECT d FROM DuplicateDetection d WHERE d.statusString = 'pending' ORDER BY d.detectedAt DESC")
    List<DuplicateDetection> findActivePendingDetections();
}
