package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.DuplicateDetection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DuplicateDetectionRepository extends JpaRepository<DuplicateDetection, Long> {
    
    // Find by status
    Page<DuplicateDetection> findByStatus(DuplicateDetection.Status status, Pageable pageable);
    
    // Count by status
    long countByStatus(DuplicateDetection.Status status);
    
    // Find processed detections
    Page<DuplicateDetection> findByProcessedAtIsNotNull(Pageable pageable);
    
    // Find pending detections
    Page<DuplicateDetection> findByStatus(String status, Pageable pageable);
    
    // Find by date range
    Page<DuplicateDetection> findByDetectedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Find by similarity score range
    @Query("SELECT dd FROM DuplicateDetection dd WHERE dd.similarityScore >= :minScore AND dd.similarityScore <= :maxScore")
    Page<DuplicateDetection> findBySimilarityScoreBetween(@Param("minScore") Double minScore, 
                                                          @Param("maxScore") Double maxScore, 
                                                          Pageable pageable);
    
    // Find by new question id
    List<DuplicateDetection> findByNewQuestionId(Long questionId);
    
    // Find by similar question id
    List<DuplicateDetection> findBySimilarQuestionId(Long questionId);
    
    // Find high similarity detections
    @Query("SELECT dd FROM DuplicateDetection dd WHERE dd.similarityScore >= :threshold")
    Page<DuplicateDetection> findHighSimilarityDetections(@Param("threshold") Double threshold, Pageable pageable);
    
    // Custom query with joins for filtering by subject and submitter
    @Query(value = "SELECT dd.* FROM duplicate_detections dd " +
                   "JOIN questions nq ON dd.new_question_id = nq.question_id " +
                   "JOIN courses c ON nq.course_id = c.course_id " +
                   "JOIN users u ON nq.created_by = u.user_id " +
                   "WHERE (:subject IS NULL OR c.course_name LIKE %:subject%) " +
                   "AND (:submitter IS NULL OR u.full_name LIKE %:submitter%)",
           countQuery = "SELECT COUNT(*) FROM duplicate_detections dd " +
                       "JOIN questions nq ON dd.new_question_id = nq.question_id " +
                       "JOIN courses c ON nq.course_id = c.course_id " +
                       "JOIN users u ON nq.created_by = u.user_id " +
                       "WHERE (:subject IS NULL OR c.course_name LIKE %:subject%) " +
                       "AND (:submitter IS NULL OR u.full_name LIKE %:submitter%)",
           nativeQuery = true)
    Page<DuplicateDetection> findBySubjectAndSubmitter(@Param("subject") String subject, 
                                                       @Param("submitter") String submitter, 
                                                       Pageable pageable);
    
    // Statistics queries
    @Query("SELECT COUNT(dd) FROM DuplicateDetection dd WHERE dd.detectedAt >= :startDate AND dd.detectedAt < :endDate")
    long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT dd.status, COUNT(dd) FROM DuplicateDetection dd GROUP BY dd.status")
    List<Object[]> getStatusStatistics();
    
    // Find recent detections
    @Query("SELECT dd FROM DuplicateDetection dd WHERE dd.detectedAt >= :since ORDER BY dd.detectedAt DESC")
    List<DuplicateDetection> findRecentDetections(@Param("since") LocalDateTime since);
    
    // Check if detection exists for question pair
    @Query("SELECT COUNT(dd) > 0 FROM DuplicateDetection dd WHERE " +
           "(dd.newQuestionId = :q1 AND dd.similarQuestionId = :q2) OR " +
           "(dd.newQuestionId = :q2 AND d