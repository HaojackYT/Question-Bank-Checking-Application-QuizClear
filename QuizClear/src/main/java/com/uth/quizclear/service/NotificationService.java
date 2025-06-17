package com.uth.quizclear.service;

import com.uth.quizclear.model.entity.Notification;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.NotificationType;
import com.uth.quizclear.model.enums.Priority;
import com.uth.quizclear.repository.NotificationRepository;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Tạo thông báo mới
     */
    @Transactional
    public Notification createNotification(Long userId, NotificationType type, String title, 
                                         String message, String actionUrl, Priority priority) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setActionUrl(actionUrl);
        notification.setPriority(priority != null ? priority : Priority.MEDIUM);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        
        return notificationRepository.save(notification);
    }

    /**
     * Gửi thông báo cho người tạo câu hỏi về quyết định duplicate
     */
    @Transactional
    public void notifyQuestionCreator(Long creatorId, String action, String questionContent, String feedback) {
        String title = "Kết quả xử lý câu hỏi trùng lặp";
        String message = buildNotificationMessage(action, questionContent, feedback);
        String actionUrl = "/staff/questions"; // Link để xem danh sách câu hỏi của họ
        
        NotificationType type = NotificationType.DUPLICATE_FOUND;
        Priority priority = Priority.HIGH;
        
        createNotification(creatorId, type, title, message, actionUrl, priority);
    }

    /**
     * Xây dựng nội dung thông báo dựa trên hành động
     */
    private String buildNotificationMessage(String action, String questionContent, String feedback) {
        String shortContent = questionContent.length() > 100 ? 
            questionContent.substring(0, 97) + "..." : questionContent;
        
        StringBuilder message = new StringBuilder();
        message.append("Câu hỏi: \"").append(shortContent).append("\"\n\n");
        
        switch (action.toUpperCase()) {
            case "ACCEPT":
                message.append("✅ Kết quả: Câu hỏi đã được chấp nhận\n");
                message.append("Câu hỏi của bạn đã được xem xét và chấp nhận vào ngân hàng câu hỏi.");
                break;
            case "REJECT":
                message.append("❌ Kết quả: Câu hỏi đã bị từ chối\n");
                message.append("Câu hỏi của bạn đã bị từ chối do trùng lặp với câu hỏi hiện có.");
                break;
            case "SEND_BACK":
                message.append("⚠️ Kết quả: Câu hỏi cần chỉnh sửa\n");
                message.append("Câu hỏi của bạn cần được chỉnh sửa để tránh trùng lặp.");
                break;
            default:
                message.append("ℹ️ Câu hỏi của bạn đã được xử lý.");
                break;
        }
        
        if (feedback != null && !feedback.trim().isEmpty()) {
            message.append("\n\nPhản hồi từ người duyệt:\n").append(feedback);
        }
        
        return message.toString();
    }

    /**
     * Lấy danh sách thông báo của user
     */
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Lấy danh sách thông báo chưa đọc của user
     */
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUser_UserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    /**
     * Đánh dấu thông báo đã đọc
     */
    @Transactional
    public void markAsRead(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    /**
     * Đếm số thông báo chưa đọc
     */
    public Long countUnreadNotifications(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }
}
