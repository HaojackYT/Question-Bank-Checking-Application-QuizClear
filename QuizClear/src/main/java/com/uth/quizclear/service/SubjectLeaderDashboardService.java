package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.SLDashboardActivityDTO;
import com.uth.quizclear.model.dto.SLDashboardChartDTO;
import com.uth.quizclear.model.dto.SLDashboardStatsDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;

@Service
public class SubjectLeaderDashboardService {
    
    private static final Logger logger = LoggerFactory.getLogger(SubjectLeaderDashboardService.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * Get dashboard statistics for Subject Leader
     */
    public SLDashboardStatsDTO getSLStats(Long subjectLeaderId) {
        try {
            logger.info("Getting SL dashboard stats for user: {}", subjectLeaderId);
            
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime startOfWeek = now.minusDays(7);
            
            // 1. Count pending assignments in SL's subjects
            String pendingAssignmentsQuery = """
                SELECT COUNT(DISTINCT t.task_id) 
                FROM tasks t 
                JOIN courses c ON t.course_id = c.course_id
                JOIN subjects s ON c.department = s.department_id
                WHERE s.subject_leader_id = ? 
                AND t.status IN ('pending', 'in_progress')
                """;
            Integer pendingAssignmentsResult = jdbcTemplate.queryForObject(pendingAssignmentsQuery, Integer.class, subjectLeaderId);
            int pendingAssignments = pendingAssignmentsResult != null ? pendingAssignmentsResult : 0;
            
            // 2. Count new assignments this month
            String newAssignmentsQuery = """
                SELECT COUNT(DISTINCT t.task_id) 
                FROM tasks t 
                JOIN courses c ON t.course_id = c.course_id
                JOIN subjects s ON c.department = s.department_id
                WHERE s.subject_leader_id = ? 
                AND t.assigned_at >= ?
                """;
            Integer newAssignmentsResult = jdbcTemplate.queryForObject(newAssignmentsQuery, Integer.class, subjectLeaderId, startOfMonth);
            int newAssignments = newAssignmentsResult != null ? newAssignmentsResult : 0;
            
            // 3. Count active lecturers in SL's subjects
            String activeLecturersQuery = """
                SELECT COUNT(DISTINCT usa.user_id)
                FROM user_subject_assignments usa
                JOIN subjects s ON usa.subject_id = s.subject_id
                WHERE s.subject_leader_id = ?
                AND usa.status = 'active'
                AND usa.role_in_subject IN ('lecturer', 'assistant')
                """;
            Integer activeLecturersResult = jdbcTemplate.queryForObject(activeLecturersQuery, Integer.class, subjectLeaderId);
            int activeLecturers = activeLecturersResult != null ? activeLecturersResult : 0;
            
            // 4. Total lecturers assigned (all time)
            int totalLecturersAssigned = activeLecturers; // For now same as active
            
            // 5. Count questions needing review (submitted status)
            String questionsNeedingReviewQuery = """
                SELECT COUNT(DISTINCT q.question_id)
                FROM questions q
                JOIN courses c ON q.course_id = c.course_id  
                JOIN subjects s ON c.department = s.department_id
                WHERE s.subject_leader_id = ?
                AND q.status = 'submitted'
                """;
            Integer questionsNeedingReviewResult = jdbcTemplate.queryForObject(questionsNeedingReviewQuery, Integer.class, subjectLeaderId);
            int questionsNeedingReview = questionsNeedingReviewResult != null ? questionsNeedingReviewResult : 0;
            
            // 6. Count new reviews this week
            String newReviewsQuery = """
                SELECT COUNT(DISTINCT q.question_id)
                FROM questions q
                JOIN courses c ON q.course_id = c.course_id
                JOIN subjects s ON c.department = s.department_id  
                WHERE s.subject_leader_id = ?
                AND q.status = 'submitted'
                AND q.submitted_at >= ?
                """;
            Integer newReviewsResult = jdbcTemplate.queryForObject(newReviewsQuery, Integer.class, subjectLeaderId, startOfWeek);
            int newReviews = newReviewsResult != null ? newReviewsResult : 0;
            
            // 7. Calculate completion rate this month
            String completionRateQuery = """
                SELECT 
                    COALESCE(
                        (COUNT(CASE WHEN t.status = 'completed' THEN 1 END) * 100.0 / 
                         NULLIF(COUNT(t.task_id), 0)), 0
                    ) as completion_rate
                FROM tasks t
                JOIN courses c ON t.course_id = c.course_id
                JOIN subjects s ON c.department = s.department_id
                WHERE s.subject_leader_id = ?
                AND t.assigned_at >= ?
                """;
            Double completionRateResult = jdbcTemplate.queryForObject(completionRateQuery, Double.class, subjectLeaderId, startOfMonth);
            double completionRate = completionRateResult != null ? completionRateResult : 0.0;
            
            // 8. Determine completion trend (simplified)
            String completionTrend = completionRate >= 75 ? "increasing" : 
                                   completionRate >= 50 ? "stable" : "decreasing";
            
            return new SLDashboardStatsDTO(
                pendingAssignments, newAssignments, activeLecturers, totalLecturersAssigned,
                questionsNeedingReview, newReviews, completionRate, completionTrend
            );
            
        } catch (Exception e) {
            logger.error("Error getting SL dashboard stats for user {}: {}", subjectLeaderId, e.getMessage());
            // Return default stats if error occurs
            return new SLDashboardStatsDTO(0, 0, 0, 0, 0, 0, 0.0, "stable");
        }
    }
    
    /**
     * Get chart data for Subject Leader dashboard
     */
    public SLDashboardChartDTO getSLChartData(Long subjectLeaderId) {
        try {
            logger.info("Getting SL chart data for user: {}", subjectLeaderId);
            
            // Get progress data for subjects managed by this SL
            String progressQuery = """
                SELECT 
                    s.subject_name,
                    COUNT(DISTINCT q.question_id) as completed_questions,
                    COALESCE(sqt.target_questions, 100) as target_questions
                FROM subjects s
                LEFT JOIN courses c ON c.department = s.department_id
                LEFT JOIN questions q ON q.course_id = c.course_id AND q.status = 'approved'
                LEFT JOIN subject_question_targets sqt ON sqt.subject_id = s.subject_id
                WHERE s.subject_leader_id = ?
                AND s.status = 'active'
                GROUP BY s.subject_id, s.subject_name, sqt.target_questions
                ORDER BY s.subject_name
                """;
            
            List<String> subjects = new ArrayList<>();
            List<Integer> completed = new ArrayList<>();
            List<Integer> targets = new ArrayList<>();
            
            jdbcTemplate.query(progressQuery, rs -> {
                subjects.add(rs.getString("subject_name"));
                completed.add(rs.getInt("completed_questions"));
                targets.add(rs.getInt("target_questions"));
            }, subjectLeaderId);
            
            // Calculate overall progress
            int totalCompleted = completed.stream().mapToInt(Integer::intValue).sum();
            int totalTarget = targets.stream().mapToInt(Integer::intValue).sum();
            double overallCompletionRate = totalTarget > 0 ? (totalCompleted * 100.0 / totalTarget) : 0.0;
            
            SLDashboardChartDTO chartDTO = new SLDashboardChartDTO();
            chartDTO.setProgressData(new SLDashboardChartDTO.SubjectProgressData(subjects, completed, targets));
            chartDTO.setOverallProgress(new SLDashboardChartDTO.OverallProgressData(overallCompletionRate, totalCompleted, totalTarget));
            
            return chartDTO;
            
        } catch (Exception e) {
            logger.error("Error getting SL chart data for user {}: {}", subjectLeaderId, e.getMessage());
            // Return empty chart data if error occurs
            SLDashboardChartDTO emptyChart = new SLDashboardChartDTO();
            emptyChart.setProgressData(new SLDashboardChartDTO.SubjectProgressData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
            emptyChart.setOverallProgress(new SLDashboardChartDTO.OverallProgressData(0.0, 0, 0));
            return emptyChart;
        }
    }
    
    /**
     * Get recent activities for Subject Leader dashboard
     */
    public List<SLDashboardActivityDTO> getSLActivities(Long subjectLeaderId, int limit) {
        try {
            logger.info("Getting SL activities for user: {}, limit: {}", subjectLeaderId, limit);
            
            // Get recent activities in subjects managed by this SL
            String activitiesQuery = """
                SELECT 
                    al.action,
                    al.activity,
                    al.created_at,
                    u.full_name as user_name,
                    COALESCE(s.subject_name, 'General') as subject_name
                FROM activity_logs al
                JOIN users u ON al.user_id = u.user_id
                LEFT JOIN subjects s ON al.entity_type = 'question' 
                    AND EXISTS (
                        SELECT 1 FROM questions q 
                        JOIN courses c ON q.course_id = c.course_id
                        JOIN subjects subj ON c.department = subj.department_id
                        WHERE q.question_id = al.entity_id 
                        AND subj.subject_leader_id = ?
                    )
                WHERE (
                    al.entity_type IN ('question', 'task', 'exam', 'plan') 
                    AND EXISTS (
                        SELECT 1 FROM subjects subj 
                        WHERE subj.subject_leader_id = ?
                        AND (
                            (al.entity_type = 'question' AND EXISTS (
                                SELECT 1 FROM questions q 
                                JOIN courses c ON q.course_id = c.course_id
                                WHERE q.question_id = al.entity_id AND c.department = subj.department_id
                            ))
                            OR (al.entity_type = 'task' AND EXISTS (
                                SELECT 1 FROM tasks t 
                                JOIN courses c ON t.course_id = c.course_id
                                WHERE t.task_id = al.entity_id AND c.department = subj.department_id
                            ))
                            OR (al.entity_type IN ('exam', 'plan') AND EXISTS (
                                SELECT 1 FROM courses c 
                                WHERE al.entity_id IS NOT NULL AND c.department = subj.department_id
                            ))
                        )
                    )
                )
                OR al.user_id = ?
                ORDER BY al.created_at DESC
                LIMIT ?
                """;
            
            List<SLDashboardActivityDTO> activities = jdbcTemplate.query(activitiesQuery, (rs, rowNum) -> {
                SLDashboardActivityDTO activity = new SLDashboardActivityDTO();
                activity.setActivityType(rs.getString("action"));
                activity.setDescription(rs.getString("activity"));
                activity.setUserName(rs.getString("user_name"));
                activity.setSubjectName(rs.getString("subject_name"));
                activity.setTimestamp(rs.getTimestamp("created_at").toLocalDateTime());
                activity.setTimeAgo(formatTimeAgo(rs.getTimestamp("created_at").toLocalDateTime()));
                return activity;
            }, subjectLeaderId, subjectLeaderId, subjectLeaderId, limit);
            
            return activities;
            
        } catch (Exception e) {
            logger.error("Error getting SL activities for user {}: {}", subjectLeaderId, e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Helper method to format time ago
     */
    private String formatTimeAgo(LocalDateTime timestamp) {
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(timestamp, now);
        long hours = ChronoUnit.HOURS.between(timestamp, now);
        long days = ChronoUnit.DAYS.between(timestamp, now);
        
        if (minutes < 60) {
            return minutes + " minutes ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else if (days < 7) {
            return days + " days ago";
        } else {
            return timestamp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
        }
    }
    
    /**
     * Get any Subject Leader user ID from database for demo purposes
     */
    public Long getAnySubjectLeaderUserId() {
        try {
            String query = "SELECT user_id FROM users WHERE role = 'SL' AND status = 'active' LIMIT 1";
            List<Long> userIds = jdbcTemplate.queryForList(query, Long.class);
            return userIds.isEmpty() ? null : userIds.get(0);
        } catch (Exception e) {
            logger.error("Error getting any Subject Leader user: {}", e.getMessage());
            return null;
        }
    }
}
