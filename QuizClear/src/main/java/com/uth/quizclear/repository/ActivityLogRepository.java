package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Integer> {
    
    // Find all duplicate processing logs (ordered by newest first)
    @Query("SELECT a FROM ActivityLog a WHERE a.entityType = 'duplicate_detection' ORDER BY a.createdAt DESC")
    List<ActivityLog> findDuplicateProcessingLogs();
    
    // Find logs by entity type and entity ID
    @Query("SELECT a FROM ActivityLog a WHERE a.entityType = :entityType AND a.entityId = :entityId ORDER BY a.createdAt DESC")
    List<ActivityLog> findByEntityTypeAndEntityId(@Param("entityType") String entityType, @Param("entityId") Integer entityId);
    
    // Find logs by user
    @Query("SELECT a FROM ActivityLog a WHERE a.user.userId = :userId ORDER BY a.createdAt DESC")
    List<ActivityLog> findByUserId(@Param("userId") Long userId);
}
