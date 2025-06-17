package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Notification;
import com.uth.quizclear.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByType(NotificationType type);
}