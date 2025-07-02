package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.ProcessingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessingLogRepository extends JpaRepository<ProcessingLog, Long> {
    
    // Get all processing logs ordered by processed date (newest first)
    List<ProcessingLog> findAllByOrderByProcessedAtDesc();
    
    // Get processing logs by action
    List<ProcessingLog> findByActionOrderByProcessedAtDesc(ProcessingLog.ProcessingAction action);
    
    // Get processing logs by processor
    @Query("SELECT pl FROM ProcessingLog pl WHERE pl.processedBy.userId = ?1 ORDER BY pl.processedAt DESC")
    List<ProcessingLog> findByProcessedByOrderByProcessedAtDesc(Long processorId);
    
    // Get processing logs for a specific question
    List<ProcessingLog> findByNewQuestionIdOrderByProcessedAtDesc(Long questionId);
    
    // Count total processing logs
    long count();
    
    // Count by action
    long countByAction(ProcessingLog.ProcessingAction action);
}
