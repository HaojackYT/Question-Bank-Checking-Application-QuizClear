package com.uth.quizclear.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.uth.quizclear.model.entity.User;

@Service
public class HODDashboardService {
        @Autowired
        private UserRepository userRepo;

        @Autowired
        private TasksRepository tasksRepo;

        private String getHODDepartment(Long userId) {
                return userRepo.findById(userId)
                                .map(User::getDepartment)
                                .orElseThrow(() -> new RuntimeException("HOD not found!"));
        }

        public HODDashboardStatsDTO getStats(Long userId) {
                String department = getHODDepartment(userId);

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

        public List<HODDashboardChartsDTO> getBarCharts(Long userId) {
                String department = getHODDepartment(userId);

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

        public double getOverallProgress(Long userId) {
                String department = getHODDepartment(userId);
                List<Tasks> tasks = tasksRepo.findTasksByDepartment(department);

                // Xác định tháng hiện tại
                YearMonth currentMonth = YearMonth.now();

                // Lọc các task có dueDate thuộc tháng hiện tại
                List<Tasks> currentMonthTasks = tasks.stream()
                                .filter(t -> t.getDueDate() != null)
                                .filter(t -> YearMonth.from(t.getDueDate()).equals(currentMonth))
                                .collect(Collectors.toList());

                // Tổng số câu hỏi đã tạo (chỉ tính các task đã completed trong tháng này)
                int created = currentMonthTasks.stream()
                                .filter(t -> t.getStatus() == TaskStatus.completed)
                                .mapToInt(t -> t.getTotalQuestions() == null ? 0 : t.getTotalQuestions())
                                .sum();

                // Tổng số câu hỏi dự kiến (target) trong tháng này
                int target = currentMonthTasks.stream()
                                .mapToInt(t -> t.getTotalQuestions() == null ? 0 : t.getTotalQuestions())
                                .sum();

                return target > 0 ? (double) created / target * 100 : 0;
        }

        // Lấy danh sách deadline cho HOD trong vòng 7 ngày tới và quá hạn trong vòng 7
        // ngày qua
        public List<HODDashboardDeadlineDTO> getDeadlines(Long userId) {
                String department = getHODDepartment(userId);
                LocalDateTime now = LocalDateTime.now();

                return tasksRepo.findTasksByDepartment(department).stream()
                                .filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(now.plusDays(7)))
                                .map(t -> {
                                        String title = "Deadline: " + t.getTaskType().name().replace("_", " ") + " - "
                                                        + t.getCourse().getCourseCode();
                                        if (t.getDueDate().isBefore(now)) {
                                                title += " - Overdue";
                                        }
                                        String description = (t.getDescription() != null ? t.getDescription()
                                                        : "Please check");
                                        return new HODDashboardDeadlineDTO(title, description,
                                                        "/tasks/" + t.getTaskId());
                                })
                                .collect(Collectors.toList());
        }

        // Lấy ds các hoạt động gần đây của hod trong 7 ngày qua
        public List<HODDashboardActivityDTO> getRecentActivities(Long userId) {
                String department = getHODDepartment(userId);
                LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

                return tasksRepo.findTasksByDepartment(department).stream()
                                .filter(t -> t.getCreatedAt() != null && t.getCreatedAt().isAfter(oneWeekAgo))
                                .map(t -> {
                                        String type = t.getTaskType().toString();
                                        String lecturerName = t.getAssignedBy().getFullName();
                                        String course = t.getCourse().getCourseName();

                                        String title = "New " + type + ": " + lecturerName + " - " + t.getTitle();
                                        String description = lecturerName + " has updated questions for " + course;
                                        String actionUrl = "/tasks/" + t.getTaskId();

                                        return new HODDashboardActivityDTO(title, description, actionUrl);
                                })
                                .collect(Collectors.toList());
        }

}