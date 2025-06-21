package com.uth.quizclear.service;

import com.uth.quizclear.model.entity.DuplicateDetection;
import com.uth.quizclear.model.enums.DuplicateDetectionAction;
import com.uth.quizclear.repository.DuplicationStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;

/**
 * Separate service for processing logs to ensure logs are saved in independent transactions
 */
@Service
public class ProcessingLogService {
    
    @Autowired
    private DuplicationStaffRepository repository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * Save processing log in a new transaction to ensure it's committed even if main transaction fails
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveProcessingLog(DuplicateDetection detection, DuplicateDetectionAction action, 
                                  String feedback, Long processorId) {
        try {
            System.out.println("=== SAVING PROCESSING LOG (NEW TRANSACTION) ===");              // Lấy thông tin chi tiết câu hỏi mới (duplicate) để lưu log
            String newQuestionContent = "Unknown";
            String newCourseName = "Unknown";
            String newCreatorName = "Unknown";
              try {
                // Get FULL new question info với JOIN query
                Object newQuestionResult = entityManager.createNativeQuery(
                    "SELECT q.content, c.course_name, u.full_name " +
                    "FROM questions q " +
                    "JOIN courses c ON q.course_id = c.course_id " +
                    "JOIN users u ON q.created_by = u.user_id " +
                    "WHERE q.question_id = ?")
                    .setParameter(1, detection.getNewQuestionId())
                    .getSingleResult();
                
                if (newQuestionResult instanceof Object[]) {
                    Object[] newQuestionInfo = (Object[]) newQuestionResult;
                    if (newQuestionInfo.length >= 3) {
                        newQuestionContent = newQuestionInfo[0] != null ? newQuestionInfo[0].toString() : "Unknown";
                        newCourseName = newQuestionInfo[1] != null ? newQuestionInfo[1].toString() : "Unknown";
                        newCreatorName = newQuestionInfo[2] != null ? newQuestionInfo[2].toString() : "Unknown";
                    }
                }
                  System.out.println("New question loaded: " + newQuestionContent.substring(0, Math.min(50, newQuestionContent.length())));
                
            } catch (Exception e) {
                System.err.println("Error getting new question details: " + e.getMessage());
                e.printStackTrace();
            }// Create activity log entry - CHỈ LƯU THÔNG TIN CÂU HỎI MỚI (DUPLICATE)
            String logMessage = String.format(
                "Duplicate Question Processing | ID: %d | Action: %s | " +
                "Question: %s | Course: %s | Creator: %s | " +
                "Similarity: %.1f%% | Duplicate of Question ID: %d | Feedback: %s",
                detection.getDetectionId(),
                action.name(),
                newQuestionContent.length() > 80 ? newQuestionContent.substring(0, 80) + "..." : newQuestionContent,
                newCourseName,
                newCreatorName,
                detection.getSimilarityScore().multiply(new BigDecimal("100")).doubleValue(),
                detection.getSimilarQuestionId(),
                feedback != null ? feedback : "No feedback provided"
            );
            
            // Insert into activity_logs table using native query
            int rowsAffected = entityManager.createNativeQuery(
                "INSERT INTO activity_logs (user_id, action, activity, entity_type, entity_id, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)")
                .setParameter(1, processorId)
                .setParameter(2, "PROCESS_DUPLICATE")
                .setParameter(3, logMessage)
                .setParameter(4, "duplicate_detection")
                .setParameter(5, detection.getDetectionId())
                .setParameter(6, java.time.LocalDateTime.now())
                .executeUpdate();
            
            // Force flush to ensure data is written
            entityManager.flush();
            
            System.out.println("Processing log saved successfully. Rows affected: " + rowsAffected);
            System.out.println("Log message: " + logMessage);
            
        } catch (Exception e) {
            System.err.println("Error saving processing log: " + e.getMessage());
            e.printStackTrace();
            // Re-throw to ensure the transaction is marked for rollback
            throw new RuntimeException("Failed to save processing log: " + e.getMessage(), e);
        }
    }
}
