package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Notification;
import com.uth.quizclear.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    // Find notifications by user
    List<Notification> findByUser_UserIdOrderByCreatedAtDesc(Long userId);
    
    // Find unread notifications by user
    List<Notification> findByUser_UserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
    
    // Find notifications by type
    List<Notification> findByTypeOrderByCreatedAtDesc(NotificationType type);
    
    // Count unread notifications for user
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.userId = :userId AND n.isRead = false")
    Long countUnreadByUserId(@Param("userId") Long userId);    // Find recent notifications for user (last 30 days) - TEMPORARILY DISABLED
    // @Query(value = "SELECT * FROM notifications n WHERE n.user_id = :userId AND n.created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY) ORDER BY n.created_at DESC", nativeQuery = true)
    // List<Notification> findRecentNotificationsByUserId(@Param("userId") Long userId);
}
