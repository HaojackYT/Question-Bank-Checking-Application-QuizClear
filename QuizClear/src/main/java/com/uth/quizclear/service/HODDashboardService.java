package com.uth.quizclear.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.repository.TasksRepository;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.model.dto.HODDashboardActivityDTO;
import com.uth.quizclear.model.dto.HODDashboardChartsDTO;
import com.uth.quizclear.model.dto.HODDashboardDeadlineDTO;
import com.uth.quizclear.model.dto.HODDashboardStatsDTO;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.model.enums.UserRole;

@Service
public class HODDashboardService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TasksRepository tasksRepo;

    public HODDashboardStatsDTO getStats() {
        long lecturerCount = userRepo.countByRole(UserRole.LEC);
        long pendingCount = tasksRepo.countByStatus(TaskStatus.PENDING);
        long approvedCount = tasksRepo.countByStatus(TaskStatus.APPROVED);
        long rejectedCount = tasksRepo.countByStatus(TaskStatus.REJECTED);

        return new HODDashboardStatsDTO(
                lecturerCount,
                pendingCount,
                approvedCount,
                rejectedCount);
    }

    // lấy dl biểu đồ cột
    public List<HODDashboardChartsDTO> getBarCharts() {
        // Lấy tất cả tasks từ repository
        List<Tasks> allTasks = tasksRepo.findAll();
        // Nhóm tasks theo tên môn
        Map<String, List<Tasks>> grouped = allTasks.stream()
                .filter(t -> t.getCourse() != null)
                .collect(Collectors.groupingBy(t -> t.getCourse().getCourseName()));
        // tạo ds
        List<HODDashboardChartsDTO> result = new ArrayList<>();

        // duyệt qua từng nhóm

        for (Map.Entry<String, List<Tasks>> entry : grouped.entrySet()) {
            String subject = entry.getKey();
            List<Tasks> tasks = entry.getValue();

            int created = tasks.stream()
                    .mapToInt(t -> t.getTotalQuestions() != null ? t.getTotalQuestions() : 0)
                    .sum();

            int target = tasks.stream()
                    .filter(t -> t.getPlan() != null)
                    .mapToInt(t -> t.getPlan().getTotalPlannedQuestions())
                    .sum();

            result.add(new HODDashboardChartsDTO(subject, created, target));
        }
        return result;
    }

    // dl biểu đồ tròn
    public double getOverallProgress() {
        List<Tasks> tasks = tasksRepo.findAll();

        int created = tasks.stream()
                .mapToInt(t -> t.getTotalQuestions() == null ? 0 : t.getTotalQuestions())
                .sum();

        int target = tasks.stream()
                .filter(t -> t.getPlan() != null)
                .mapToInt(t -> t.getPlan().getTotalPlannedQuestions())
                .sum();

        // tính tỉ lệ phần trăm
        return target > 0 ? (double) created / target * 100 : 0;
    }

    // lấy deadline
    public List<HODDashboardDeadlineDTO> getDeadlines() {
        LocalDateTime now = LocalDateTime.now();

        return tasksRepo.findAll().stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(now.plusDays(7)))
                .map(t -> new HODDashboardDeadlineDTO(
                        t.getTitle(),
                        t.getCourse().getCourseName(),
                        t.getDueDate().isBefore(now) ? "Overdue" : "Upcoming"))
                .collect(Collectors.toList());
    }

    // lấy hoạt động gần đây
    public List<HODDashboardActivityDTO> getRecentActivities() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        return tasksRepo.findAll().stream()
                .filter(t -> t.getCreatedAt() != null && t.getCreatedAt().isAfter(oneWeekAgo))
                .map(t -> new HODDashboardActivityDTO(
                        "New Assignment: " + t.getTitle(),
                        t.getAssignedBy().getFullName(),
                        t.getCreatedAt().toString()))
                .collect(Collectors.toList());
    }
}