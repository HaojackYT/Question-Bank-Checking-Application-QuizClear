package com.uth.quizclear.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uth.quizclear.repository.TasksRepository;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.model.dto.HODDashboardActivityDTO;
import com.uth.quizclear.model.dto.HODDashboardChartsDTO;
import com.uth.quizclear.model.dto.HODDashboardDeadlineDTO;
import com.uth.quizclear.model.dto.HODDashboardStatsDTO;
import com.uth.quizclear.model.entity.Department;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.model.entity.User;

@Service
public class HODDashboardService {
        @Autowired
        private UserRepository userRepo;

        @Autowired
        private TasksRepository tasksRepo;

        // chưa truyền dl change
        public HODDashboardStatsDTO getStats() {
                // Lấy HOD đang đăng nhập
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                Optional<User> hodOpt = userRepo.findByEmail(email);

                if (hodOpt.isEmpty()) {
                        throw new RuntimeException("HOD not found!");
                }

                User hod = hodOpt.get();
                String department = hod.getDepartment();

                // Đếm số giảng viên trong cùng khoa
                long lecturerCount = userRepo.countUsersByRoleAndDepartmentActiveUnlocked(UserRole.LEC, department);

                // Đếm tasks theo trạng thái
                List<Tasks> allTasks = tasksRepo.findTasksByDepartment(department);
                long pendingCount = allTasks.stream().filter(t -> t.getStatus() == TaskStatus.pending).count();
                long approvedCount = allTasks.stream().filter(t -> t.getStatus() == TaskStatus.completed).count();
                long rejectedCount = allTasks.stream().filter(t -> t.getStatus() == TaskStatus.cancelled).count();

                return new HODDashboardStatsDTO(
                                lecturerCount,
                                pendingCount,
                                approvedCount,
                                rejectedCount);
        }

        // lấy dl biểu đồ cột
        public List<HODDashboardChartsDTO> getBarCharts() {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                Optional<User> hodOpt = userRepo.findByEmail(email);
                if (hodOpt.isEmpty()) {
                        throw new RuntimeException("HOD not found!");
                }

                User hod = hodOpt.get();
                String department = hod.getDepartment();
                if (department == null) {
                        throw new RuntimeException("HOD's department not found!");
                }

                // Lấy tất cả task trong khoa của HOD
                List<Tasks> allTasks = tasksRepo.findTasksByDepartment(department);
                if (allTasks.isEmpty())
                        return new ArrayList<>();

                // Xác định tháng hiện tại
                YearMonth currentMonth = YearMonth.now();

                // Target: đếm tất cả task có dueDate trong tháng hiện tại
                Map<String, Long> subjectTargetMap = allTasks.stream()
                                .filter(t -> t.getDueDate() != null)
                                .filter(t -> YearMonth.from(t.getDueDate()).equals(currentMonth))
                                .collect(Collectors.groupingBy(
                                                t -> t.getCourse().getCourseName(),
                                                Collectors.counting()));

                // Created: task đã hoàn thành trong tháng hiện tại
                Map<String, Long> subjectCompletedMap = allTasks.stream()
                                .filter(t -> t.getStatus() == TaskStatus.completed)
                                .filter(t -> t.getCompletedAt() != null)
                                .filter(t -> YearMonth.from(t.getCompletedAt()).equals(currentMonth))
                                .collect(Collectors.groupingBy(
                                                t -> t.getCourse().getCourseName(),
                                                Collectors.counting()));

                Set<String> allSubjects = new HashSet<>();
                allSubjects.addAll(subjectTargetMap.keySet());
                allSubjects.addAll(subjectCompletedMap.keySet());

                List<HODDashboardChartsDTO> result = new ArrayList<>();
                for (String subject : allSubjects) {
                        int target = subjectTargetMap.getOrDefault(subject, 0L).intValue();
                        int created = subjectCompletedMap.getOrDefault(subject, 0L).intValue();
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

                // Since Tasks doesn't have getPlan(), we'll use the same total questions as
                // target
                int target = tasks.stream()
                                .mapToInt(t -> t.getTotalQuestions() == null ? 0 : t.getTotalQuestions())
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