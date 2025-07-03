package com.uth.quizclear.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.LecturerDashboardActivityDTO;
import com.uth.quizclear.model.dto.LecturerDashboardStatsDTO;
import com.uth.quizclear.model.dto.LecturerDashboardTaskDTO;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.TasksRepository;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.repository.CourseRepository;

@Service
public class LecturerDashboardService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Get dashboard statistics for lecturer
     */
    public LecturerDashboardStatsDTO getStats(Long lecturerId) {
        User lecturer = userRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        // Count questions by status
        long submittedQuestions = questionRepository.countByCreatedByAndStatus(lecturer, QuestionStatus.SUBMITTED);
        long approvedQuestions = questionRepository.countByCreatedByAndStatus(lecturer, QuestionStatus.APPROVED);
        long rejectedQuestions = questionRepository.countByCreatedByAndStatus(lecturer, QuestionStatus.REJECTED);
        long pendingQuestions = questionRepository.countByCreatedByAndStatus(lecturer, QuestionStatus.DRAFT);

        return LecturerDashboardStatsDTO.builder()
                .questionSubmitted(submittedQuestions)
                .questionApproved(approvedQuestions)
                .questionReturned(rejectedQuestions)
                .questionPending(pendingQuestions)
                .build();
    }

    /**
     * Get current assignments/tasks for lecturer
     */
    public List<LecturerDashboardTaskDTO> getCurrentTasks(Long lecturerId) {
        User lecturer = userRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        List<Tasks> tasks = tasksRepository.findByAssignedToAndStatusIn(
                lecturer,
                List.of(TaskStatus.pending, TaskStatus.in_progress));

        return tasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get recent activities for lecturer
     */
    public List<LecturerDashboardActivityDTO> getRecentActivities(Long lecturerId) {
        User lecturer = userRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        // Get recent questions created by lecturer
        List<Question> recentQuestions = questionRepository
                .findTop10ByCreatedByOrderByCreatedAtDesc(lecturer);

        return recentQuestions.stream()
                .map(this::convertToActivityDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get lecturer scope information (subjects and total questions)
     */
    public Map<String, Object> getLecturerScope(Long lecturerId) {
        User lecturer = userRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        try {
            // Get courses where lecturer has questions
            List<Course> lecturerCourses = courseRepository.findCoursesWithQuestionsByCreator(lecturerId);

            // Get total questions count
            long totalQuestions = questionRepository.countByCreatedBy(lecturer);

            Map<String, Object> scopeData = new HashMap<>();

            // Create subjects list (using course names as subjects)
            List<Map<String, Object>> subjects = lecturerCourses.stream()
                    .map(course -> {
                        Map<String, Object> subject = new HashMap<>();
                        subject.put("subjectName", course.getCourseName());
                        subject.put("department", course.getDepartment());
                        return subject;
                    })
                    .collect(Collectors.toList());

            scopeData.put("subjects", subjects);
            scopeData.put("totalQuestions", totalQuestions);

            return scopeData;
        } catch (Exception e) {
            // Fallback data if there's an error
            Map<String, Object> fallbackData = new HashMap<>();
            fallbackData.put("subjects", new ArrayList<>());
            fallbackData.put("totalQuestions", 0L);
            return fallbackData;
        }
    }

    /**
     * Get chart data for lecturer dashboard
     */
    public Map<String, Object> getChartData(Long lecturerId) {
        User lecturer = userRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        // Get question counts by status
        long submittedCount = questionRepository.countByCreatedByAndStatus(lecturer, QuestionStatus.SUBMITTED);
        long approvedCount = questionRepository.countByCreatedByAndStatus(lecturer, QuestionStatus.APPROVED);
        long rejectedCount = questionRepository.countByCreatedByAndStatus(lecturer, QuestionStatus.REJECTED);
        long draftCount = questionRepository.countByCreatedByAndStatus(lecturer, QuestionStatus.DRAFT);

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", List.of("Submitted", "Approved", "Rejected", "Draft"));
        chartData.put("data", List.of(submittedCount, approvedCount, rejectedCount, draftCount));

        return chartData;
    }

    private LecturerDashboardTaskDTO convertToTaskDTO(Tasks task) {
        // Calculate progress
        long completedQuestions = 0;
        long totalQuestions = task.getTotalQuestions() != null ? task.getTotalQuestions() : 0;
        if (totalQuestions > 0) {
            completedQuestions = questionRepository.countByTaskIdAndStatus(
                    task.getTaskId(),
                    QuestionStatus.SUBMITTED);
        }

        double progressPercentage = totalQuestions > 0 ? (double) completedQuestions / totalQuestions * 100 : 0;

        String deadline = task.getDueDate() != null
                ? task.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "No deadline";

        return LecturerDashboardTaskDTO.builder()
                .taskId(task.getTaskId().longValue())
                .title(task.getTitle())
                .description(task.getDescription())
                .completedQuestions(completedQuestions)
                .totalQuestions(totalQuestions)
                .progressPercentage(progressPercentage)
                .deadline(deadline)
                .status(task.getStatus().toString())
                .courseName(task.getCourse() != null ? task.getCourse().getCourseName() : "Unknown Course")
                .build();
    }

    private LecturerDashboardActivityDTO convertToActivityDTO(Question question) {
        String statusText = getStatusText(question.getStatus());
        String statusClass = getStatusClass(question.getStatus());

        String formattedDate = question.getCreatedAt() != null
                ? question.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm MM/dd"))
                : "";

        String courseName = question.getCourse() != null ? question.getCourse().getCourseName() : "Unknown Course";

        String activityText = "Question for " + courseName;
        String activityCaption = getActivityCaption(question.getStatus());

        return LecturerDashboardActivityDTO.builder()
                .questionId(question.getQuestionId())
                .activityText(activityText)
                .activityCaption(activityCaption)
                .status(statusText)
                .statusClass(statusClass)
                .date(formattedDate)
                .build();
    }

    private String getStatusText(QuestionStatus status) {
        switch (status) {
            case APPROVED:
                return "Approved";
            case REJECTED:
                return "Rejected";
            case SUBMITTED:
                return "Pending";
            case DRAFT:
                return "Draft";
            default:
                return "Unknown";
        }
    }

    private String getStatusClass(QuestionStatus status) {
        switch (status) {
            case APPROVED:
                return "status-btn-a";
            case REJECTED:
                return "status-btn-r";
            case SUBMITTED:
                return "status-btn-p";
            case DRAFT:
                return "status-btn-d";
            default:
                return "status-btn-p";
        }
    }

    private String getActivityCaption(QuestionStatus status) {
        switch (status) {
            case APPROVED:
                return "The question has been approved.";
            case REJECTED:
                return "The question was rejected and needs revision.";
            case SUBMITTED:
                return "The question has been submitted for review.";
            case DRAFT:
                return "The question is saved as draft.";
            default:
                return "Question status unknown.";
        }
    }
}
