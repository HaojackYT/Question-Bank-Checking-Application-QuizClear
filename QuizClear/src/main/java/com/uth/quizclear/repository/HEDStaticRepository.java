package com.uth.quizclear.repository;

import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class HEDStaticRepository {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;    public Map<String, Object> getStatisticsData() {
        Map<String, Object> data = new HashMap<>();
        
        // Tổng số câu hỏi trong bảng questions
        long totalQuestions = questionRepository.count();
        
        // Số câu hỏi đã chấp nhận (status = APPROVED)
        long approvedQuestions = questionRepository.findByStatus(QuestionStatus.APPROVED).size();
        
        // Số câu hỏi đã gửi (status = SUBMITTED) - Pending
        long pendingQuestions = questionRepository.findByStatus(QuestionStatus.SUBMITTED).size();
        
        // Số câu hỏi đã từ chối (status = REJECTED) - Reject  
        long rejectedQuestions = questionRepository.findByStatus(QuestionStatus.REJECTED).size();
        
        // Số câu hỏi được thêm vào trong tháng này
        LocalDateTime startOfMonth = java.time.LocalDate.now().withDayOfMonth(1).atStartOfDay();
        long questionChange = questionRepository.findByCreatedAtBetween(startOfMonth, LocalDateTime.now()).size();
        
        // Tỉ lệ câu hỏi được chấp nhận trên tổng số câu hỏi
        double approvedPercentage = totalQuestions > 0 ? (approvedQuestions * 100.0 / totalQuestions) : 0;
        
        data.put("totalQuestions", totalQuestions);
        data.put("approvedQuestions", approvedQuestions);
        data.put("pendingQuestions", pendingQuestions);
        data.put("rejectedQuestions", rejectedQuestions);
        data.put("questionChange", questionChange);
        data.put("approvedPercentage", String.format("%.1f", approvedPercentage));        // Biểu đồ cột thể hiện tiến độ tạo câu hỏi của mỗi môn
        List<Course> courses = courseRepository.findAll();
        List<Map<String, Object>> chartData = new ArrayList<>();
        for (Course course : courses) {
            long created = questionRepository.findByCourse_CourseId(course.getCourseId()).size();
            // Mục tiêu cho mỗi môn (có thể điều chỉnh theo yêu cầu thực tế)
            Integer target = 100; // Có thể lấy từ database hoặc config
            
            // Tính tỉ lệ hoàn thành
            double completedPercentage = target > 0 ? (created * 100.0 / target) : 0;
            
            chartData.add(Map.of(
                "subject", course.getCourseName(),
                "created", created,
                "target", target,
                "completedPercentage", String.format("%.1f", completedPercentage)
            ));
        }
        data.put("chartData", chartData);// Biểu đồ tròn thể hiện tiến độ hoàn thành tổng thể
        // Tính tổng số câu hỏi cần tạo cho tất cả môn
        long totalTargetQuestions = courses.size() * 100L; // 100 câu hỏi/môn
        long totalCreatedQuestions = questionRepository.count();
        
        // Tính tỉ lệ hoàn thành tổng thể
        int completedOverall = totalTargetQuestions > 0 ? 
            (int) Math.round((totalCreatedQuestions * 100.0) / totalTargetQuestions) : 0;
        if (completedOverall > 100) completedOverall = 100; // Không vượt quá 100%
        
        int remainingOverall = 100 - completedOverall;
        Map<String, Integer> progressData = new HashMap<>();
        progressData.put("completed", completedOverall);
        progressData.put("remaining", remainingOverall);
        data.put("progressData", progressData);        // Biểu đồ thanh ngang thể hiện tiến độ công việc của các giáo viên
        List<User> lecturers = userRepository.findByRole(com.uth.quizclear.model.enums.UserRole.LEC);
        List<Map<String, Object>> lecturerData = new ArrayList<>();
        
        for (User lec : lecturers) {
            Long lecId = lec.getUserId() != null ? lec.getUserId().longValue() : null;
            
            // Approve: số câu hỏi đã được chấp nhận
            long approved = questionRepository.findByCreatedBy_UserId(lecId).stream()
                .filter(q -> q.getStatus() == QuestionStatus.APPROVED).count();
            
            // Pending: số câu hỏi đang chờ duyệt (đã gửi)
            long pending = questionRepository.findByCreatedBy_UserId(lecId).stream()
                .filter(q -> q.getStatus() == QuestionStatus.SUBMITTED).count();
            
            // Reject: số câu hỏi bị từ chối
            long rejected = questionRepository.findByCreatedBy_UserId(lecId).stream()
                .filter(q -> q.getStatus() == QuestionStatus.REJECTED).count();
            
            // Need: số câu hỏi nháp (chưa hoàn thành)
            long need = questionRepository.findByCreatedBy_UserId(lecId).stream()
                .filter(q -> q.getStatus() == QuestionStatus.DRAFT).count();
            
            lecturerData.add(Map.of(
                "userName", lec.getFullName(),
                "completed", approved,    // Xanh lá - đã hoàn thành
                "inProgress", pending,    // Cam - đang xử lý  
                "overdue", rejected,      // Đỏ - bị từ chối
                "remaining", need         // Xám - còn lại (nháp)
            ));
        }
        data.put("lecturerData", lecturerData);
        return data;
    }
}
