package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.AiSimilarityResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AiSimilarityResultRepository extends JpaRepository<AiSimilarityResult, Long> {
    
    // Find by AI check ID
    List<AiSimilarityResult> findByAiCheck_CheckId(Long checkId);
    
    // Find by duplicate flag
    List<AiSimilarityResult> findByIsDuplicateTrue();
    List<AiSimilarityResult> findByIsDuplicateFalse();
    
    // Find by existing question
    List<AiSimilarityResult> findByExistingQuestion_QuestionId(Long questionId);
    
    // Find by similarity score threshold
    List<AiSimilarityResult> findBySimilarityScoreGreaterThanEqual(BigDecimal threshold);
    List<AiSimilarityResult> findBySimilarityScoreLessThan(BigDecimal threshold);
    
    // Find high similarity results
    @Query("SELECT r FROM AiSimilarityResult r WHERE r.similarityScore >= :threshold ORDER BY r.similarityScore DESC")
    List<AiSimilarityResult> findHighSimilarityResults(@Param("threshold") BigDecimal threshold);
    
    // Find results for a specific AI check with threshold
    @Query("SELECT r FROM AiSimilarityResult r WHERE r.aiCheck.checkId = :checkId AND r.similarityScore >= :threshold ORDER BY r.similarityScore DESC")
    List<AiSimilarityResult> findResultsAboveThreshold(@Param("checkId") Long checkId, @Param("threshold") BigDecimal threshold);
    
    // Find duplicate results for a specific AI check
    @Query("SELECT r FROM AiSimilarityResult r WHERE r.aiCheck.checkId = :checkId AND r.isDuplicate = true ORDER BY r.similarityScore DESC")
    List<AiSimilarityResult> findDuplicateResults(@Param("checkId") Long checkId);
    
    // Find top N similar results for a check
    @Query("SELECT r FROM AiSimilarityResult r WHERE r.aiCheck.checkId = :checkId ORDER BY r.similarityScore DESC LIMIT :limit")
    List<AiSimilarityResult> findTopSimilarResults(@Param("checkId") Long checkId, @Param("limit") int limit);
    
    // Count duplicates for a check
    @Query("SELECT COUNT(r) FROM AiSimilarityResult r WHERE r.aiCheck.checkId = :checkId AND r.isDuplicate = true")
    long countDuplicatesForCheck(@Param("checkId") Long checkId);
    
    // Get max similarity score for a check
    @Query("SELECT MAX(r.similarityScore) FROM AiSimilarityResult r WHERE r.aiCheck.checkId = :checkId")
    BigDecimal getMaxSimilarityForCheck(@Param("checkId") Long checkId);
    
    // Get average similarity score for a check
    @Query("SELECT AVG(r.similarityScore) FROM AiSimilarityResult r WHERE r.aiCheck.checkId = :checkId")
    Double getAverageSimilarityForCheck(@Param("checkId") Long checkId);
    
    // Statistics queries
    @Query("SELECT AVG(r.similarityScore) FROM AiSimilarityResult r")
    Double getOverallAverageSimilarity();
    
    @Query("SELECT COUNT(r) FROM AiSimilarityResult r WHERE r.isDuplicate = true")
    long getTotalDuplicatesFound();
    
    @Query("SELECT COUNT(r) FROM AiSimilarityResult r")
    long getTotalComparisons();
    
    // Find similar questions to a specific question across all checks
    @Query("SELECT r FROM AiSimilarityResult r WHERE r.existingQuestion.questionId = :questionId AND r.similarityScore >= :threshold ORDER BY r.similarityScore DESC")
    List<AiSimilarityResult> findSimilarToQuestion(@Param("questionId") Long questionId, @Param("threshold") BigDecimal threshold);
    
    // Find results by course (through AI check)
    @Query("SELECT r FROM AiSimilarityResult r WHERE r.aiCheck.course.courseId = :courseId ORDER BY r.similarityScore DESC")
    List<AiSimilarityResult> findResultsByCourse(@Param("courseId") Long courseId);
}
