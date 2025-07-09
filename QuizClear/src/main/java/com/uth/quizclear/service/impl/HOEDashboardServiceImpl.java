package com.uth.quizclear.service.impl;

import com.uth.quizclear.repository.TasksRepository;
import com.uth.quizclear.repository.ExamReviewRepository;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.SubjectRepository;
import com.uth.quizclear.repository.ProcessingLogRepository;
import com.uth.quizclear.service.HOEDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HOEDashboardServiceImpl implements HOEDashboardService {
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private ExamReviewRepository examReviewRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private ProcessingLogRepository processingLogRepository;

    @Override
    public int getPendingAssignments() {
        // Đếm số task trạng thái PENDING
        return tasksRepository.findByStatus(com.uth.quizclear.model.enums.TaskStatus.pending).size();
    }

    @Override
    public int getTotalReviews() {
        // Ví dụ: tổng số review
        return (int) examReviewRepository.count();
    }

    @Override
    public int getNeedApproval() {
        // Đếm số câu hỏi cần duyệt
        // Không có NEED_APPROVAL, có thể dùng SUBMITTED hoặc APPROVED tùy logic
        return questionRepository.findByStatus(com.uth.quizclear.model.enums.QuestionStatus.SUBMITTED).size();
    }

    @Override
    public int getTaskCompletion() {
        // Đếm số task đã hoàn thành
        return tasksRepository.findByStatus(com.uth.quizclear.model.enums.TaskStatus.completed).size();
    }

    @Override
    public int getOverallProgress() {
        // % task đã hoàn thành trên tổng số task
        int total = (int) tasksRepository.count();
        int completed = tasksRepository.findByStatus(com.uth.quizclear.model.enums.TaskStatus.completed).size();
        return total == 0 ? 0 : (completed * 100 / total);
    }

    @Override
    public List<Map<String, Object>> getChartData() {
        // Trả về dữ liệu chart cho từng học phần (course)
        List<Map<String, Object>> chart = new ArrayList<>();
        // Lấy tất cả các course có trong examReview
        Set<String> courseNames = new HashSet<>();
        examReviewRepository.findAll().forEach(r -> {
            if (r.getExam() != null && r.getExam().getCourse() != null) {
                courseNames.add(r.getExam().getCourse().getCourseName());
            }
        });
        for (String courseName : courseNames) {
            Map<String, Object> data = new HashMap<>();
            data.put("subject", courseName);
            long created = examReviewRepository.findAll().stream()
                .filter(r -> r.getExam() != null && r.getExam().getCourse() != null &&
                    courseName.equals(r.getExam().getCourse().getCourseName()))
                .count();
            data.put("created", created);
            data.put("target", 100); // Không có targetReview, hardcode 100
            chart.add(data);
        }
        return chart;
    }

    @Override
    public List<Map<String, Object>> getRecentUpdates() {
        // Lấy 5 log gần nhất
        List<Map<String, Object>> updates = new ArrayList<>();
        processingLogRepository.findAllByOrderByProcessedAtDesc().stream().limit(5).forEach(log -> {
            Map<String, Object> u = new HashMap<>();
            u.put("activity", log.getAction());
            u.put("created_at", log.getProcessedAt());
            updates.add(u);
        });
        return updates;
    }
}
