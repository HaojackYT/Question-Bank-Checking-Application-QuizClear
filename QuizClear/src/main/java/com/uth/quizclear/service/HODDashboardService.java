package com.uth.quizclear.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;
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
        YearMonth currentMonth = YearMonth.now();
        YearMonth lastMonth = currentMonth.minusMonths(1);

        // Current month data
        long lecturerCurrent = userRepo.countUsersByRoleAndDepartmentActiveUnlocked(UserRole.LEC, department);

        List<Tasks> allTasks = tasksRepo.findTasksByDepartment(department);
        long pendingCurrent = allTasks.stream().filter(t -> t.getStatus() == TaskStatus.pending).count();
        long approvedCurrent = allTasks.stream().filter(t -> t.getStatus() == TaskStatus.completed).count();
        long rejectedCurrent = allTasks.stream().filter(t -> t.getStatus() == TaskStatus.cancelled).count();

        // Last month data using stream filtering
        long lecturerLast = userRepo.findAll().stream()
                .filter(u -> u.getRole() == UserRole.LEC)
                .filter(u -> department.equals(u.getDepartment()))
                .filter(u -> u.getCreatedAt() != null)
                .filter(u -> YearMonth.from(u.getCreatedAt()).equals(lastMonth))
                .count();

        long pendingLast = allTasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.pending)
                .filter(t -> t.getCreatedAt() != null)
                .filter(t -> YearMonth.from(t.getCreatedAt()).equals(lastMonth))
                .count();

        long approvedLast = allTasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.completed)
                .filter(t -> t.getCompletedAt() != null)
                .filter(t -> YearMonth.from(t.getCompletedAt()).equals(lastMonth))
                .count();

        long rejectedLast = allTasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.cancelled)
                .filter(t -> t.getCreatedAt() != null)
                .filter(t -> YearMonth.from(t.getCreatedAt()).equals(lastMonth))
                .count();

        // Calculate changes
        long lecturerChange = lecturerCurrent - lecturerLast;
        long pendingChange = pendingCurrent - pendingLast;
        long approvedChange = approvedCurrent - approvedLast;
        long rejectedChange = rejectedCurrent - rejectedLast;

        return new HODDashboardStatsDTO(
                lecturerCurrent, lecturerChange,
                pendingCurrent, pendingChange,
                approvedCurrent, approvedChange,
                rejectedCurrent, rejectedChange);
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

        YearMonth currentMonth = YearMonth.now();

        List<Tasks> currentMonthTasks = tasks.stream()
                .filter(t -> t.getDueDate() != null)
                .filter(t -> YearMonth.from(t.getDueDate()).equals(currentMonth))
                .collect(Collectors.toList());

        int created = currentMonthTasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.completed)
                .mapToInt(t -> t.getTotalQuestions() == null ? 0 : t.getTotalQuestions())
                .sum();

        int target = currentMonthTasks.stream()
                .mapToInt(t -> t.getTotalQuestions() == null ? 0 : t.getTotalQuestions())
                .sum();

        return target > 0 ? (double) created / target * 100 : 0;
    }

    public List<HODDashboardDeadlineDTO> getDeadlines(Long userId) {
        String department = getHODDepartment(userId);
        LocalDateTime now = LocalDateTime.now();

        return tasksRepo.findTasksByDepartment(department).stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(now.plusDays(7)))
                .map(t -> {
                    // Chuyển taskType thành chữ in hoa, giữ nguyên các phần còn lại
                    String taskTypeUpper = t.getTaskType().name().replace("_", " ").toUpperCase();
                    String title = "DEADLINE: " + taskTypeUpper + " - " + t.getCourse().getCourseCode();

                    // Nếu quá hạn thì thêm chữ OVERDUE
                    if (t.getDueDate().isBefore(now)) {
                        title += " - OVERDUE";
                    }

                    String description = (t.getDescription() != null && !t.getDescription().isBlank())
                            ? t.getDescription()
                            : "Please check";

                    return new HODDashboardDeadlineDTO(
                            title,
                            description,
                            "/tasks/" + t.getTaskId());
                })
                .collect(Collectors.toList());
    }

    public List<HODDashboardActivityDTO> getRecentActivities(Long userId) {
        String department = getHODDepartment(userId);
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        return tasksRepo.findTasksByDepartment(department).stream()
                .filter(t -> t.getCreatedAt() != null && t.getCreatedAt().isAfter(oneWeekAgo))
                .filter(t -> (t.getAssignedTo() != null && t.getAssignedTo().getUserId().equals(userId)) ||
                        (t.getAssignedBy() != null && t.getAssignedBy().getUserId().equals(userId)))
                .map(t -> {
                    String title;
                    String description;
                    String actionUrl = "/tasks/" + t.getTaskId();

                    if (t.getAssignedTo() != null && t.getAssignedTo().getUserId().equals(userId)) {
                        String rdName = t.getAssignedBy().getFullName();
                        title = "New Assignment: " + rdName + " - " + t.getTitle();
                        description = "You have been assigned a task by " + rdName;
                    } else if (t.getAssignedBy() != null && t.getAssignedBy().getUserId().equals(userId) &&
                            (t.getStatus() == TaskStatus.in_progress || t.getStatus() == TaskStatus.completed)) {
                        String lecturerName = t.getAssignedTo().getFullName();
                        title = "New Approve: " + lecturerName + " - " + t.getTitle();
                        description = lecturerName + " has completed a task that needs your approval.";
                    } else {
                        return null;
                    }

                    return new HODDashboardActivityDTO(title, description, actionUrl);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
