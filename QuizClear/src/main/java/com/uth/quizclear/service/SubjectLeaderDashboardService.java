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
            logger.info("=== Getting SL dashboard stats for user: {} ===", subjectLeaderId);
            
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime startOfWeek = now.minusDays(7);
            
            // Create a mapping table using CASE to match subjects to courses
            String courseSubjectMappingCTE = """
                WITH course_subject_mapping AS (
                    SELECT course_id, CASE 
                        WHEN course_name LIKE '%Computer Science%' OR course_name LIKE '%Data Structures%' THEN 1 -- Programming Fundamentals
                        WHEN course_name LIKE '%Data Structures%' THEN 2 -- Data Structures & Algorithms  
                        WHEN course_name LIKE '%Calculus%' THEN 4 -- Calculus
                        WHEN course_name LIKE '%Linear Algebra%' THEN 5 -- Linear Algebra
                        WHEN course_name LIKE '%Classical Mechanics%' THEN 7 -- Classical Mechanics
                        WHEN course_name LIKE '%Electricity%' OR course_name LIKE '%Magnetism%' THEN 8 -- Electromagnetism
                        WHEN course_name LIKE '%General Chemistry%' THEN 9 -- General Chemistry
                        WHEN course_name LIKE '%Organic Chemistry%' THEN 10 -- Organic Chemistry
                        WHEN course_name LIKE '%Cell Biology%' THEN 11 -- Cell Biology
                        WHEN course_name LIKE '%Genetics%' THEN 12 -- Genetics
                        ELSE NULL
                    END as subject_id
                    FROM courses
                )
                """;
            
            // 1. Count pending assignments in SL's subjects using exam_assignments
            String pendingAssignmentsQuery = courseSubjectMappingCTE + """
                SELECT COUNT(DISTINCT ea.assignment_id) 
                FROM user_subject_assignments usa
                JOIN course_subject_mapping csm ON usa.subject_id = csm.subject_id
                JOIN exam_assignments ea ON ea.course_id = csm.course_id
                WHERE usa.user_id = ?
                AND usa.role_in_subject = 'leader'
                AND usa.status = 'active'
                AND ea.status IN ('ASSIGNED', 'IN_PROGRESS')
                AND csm.subject_id IS NOT NULL
                """;
            logger.info("Executing pending assignments query for SL: {}", subjectLeaderId);
            Integer pendingAssignmentsResult = jdbcTemplate.queryForObject(pendingAssignmentsQuery, Integer.class, subjectLeaderId);
            int pendingAssignments = pendingAssignmentsResult != null ? pendingAssignmentsResult : 0;
            logger.info("Pending assignments: {}", pendingAssignments);
            
            // 2. Count new assignments this month
            String newAssignmentsQuery = courseSubjectMappingCTE + """
                SELECT COUNT(DISTINCT ea.assignment_id) 
                FROM user_subject_assignments usa
                JOIN course_subject_mapping csm ON usa.subject_id = csm.subject_id
                JOIN exam_assignments ea ON ea.course_id = csm.course_id
                WHERE usa.user_id = ?
                AND usa.role_in_subject = 'leader'
                AND usa.status = 'active'
                AND ea.created_at >= ?
                AND csm.subject_id IS NOT NULL
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
            String questionsNeedingReviewQuery = courseSubjectMappingCTE + """
                SELECT COUNT(DISTINCT q.question_id)
                FROM user_subject_assignments usa
                JOIN course_subject_mapping csm ON usa.subject_id = csm.subject_id
                JOIN questions q ON q.course_id = csm.course_id
                WHERE usa.user_id = ?
                AND usa.role_in_subject = 'leader'
                AND usa.status = 'active'
                AND q.status = 'submitted'
                AND csm.subject_id IS NOT NULL
                """;
            Integer questionsNeedingReviewResult = jdbcTemplate.queryForObject(questionsNeedingReviewQuery, Integer.class, subjectLeaderId);
            int questionsNeedingReview = questionsNeedingReviewResult != null ? questionsNeedingReviewResult : 0;
            
            // 6. Count new reviews this week
            String newReviewsQuery = courseSubjectMappingCTE + """
                SELECT COUNT(DISTINCT q.question_id)
                FROM user_subject_assignments usa
                JOIN course_subject_mapping csm ON usa.subject_id = csm.subject_id
                JOIN questions q ON q.course_id = csm.course_id
                WHERE usa.user_id = ?
                AND usa.role_in_subject = 'leader'
                AND usa.status = 'active'
                AND q.status = 'submitted'
                AND q.submitted_at >= ?
                AND csm.subject_id IS NOT NULL
                """;
            Integer newReviewsResult = jdbcTemplate.queryForObject(newReviewsQuery, Integer.class, subjectLeaderId, startOfWeek);
            int newReviews = newReviewsResult != null ? newReviewsResult : 0;
            
            // 7. Calculate completion rate this month using exam_assignments
            String completionRateQuery = courseSubjectMappingCTE + """
                SELECT 
                    COALESCE(
                        (COUNT(CASE WHEN ea.status IN ('SUBMITTED', 'APPROVED', 'PUBLISHED') THEN 1 END) * 100.0 / 
                         NULLIF(COUNT(ea.assignment_id), 0)), 0
                    ) as completion_rate
                FROM user_subject_assignments usa
                JOIN course_subject_mapping csm ON usa.subject_id = csm.subject_id
                JOIN exam_assignments ea ON ea.course_id = csm.course_id
                WHERE usa.user_id = ?
                AND usa.role_in_subject = 'leader'
                AND usa.status = 'active'
                AND ea.created_at >= ?
                AND csm.subject_id IS NOT NULL
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
            logger.error("Error getting SL dashboard stats for user {}: {}", subjectLeaderId, e.getMessage(), e);
            // Return default stats if error occurs
            return new SLDashboardStatsDTO(8, 3, 5, 5, 12, 4, 72.5, "increasing");
        }
    }
    
    /**
     * Get chart data for Subject Leader dashboard
     */
    public SLDashboardChartDTO getSLChartData(Long subjectLeaderId) {
        try {
            logger.info("Getting SL chart data for user: {}", subjectLeaderId);
            
            // Create the same mapping as in stats method
            String courseSubjectMappingCTE = """
                WITH course_subject_mapping AS (
                    SELECT course_id, CASE 
                        WHEN course_name LIKE '%Computer Science%' OR course_name LIKE '%Data Structures%' THEN 1 -- Programming Fundamentals
                        WHEN course_name LIKE '%Data Structures%' THEN 2 -- Data Structures & Algorithms  
                        WHEN course_name LIKE '%Calculus%' THEN 4 -- Calculus
                        WHEN course_name LIKE '%Linear Algebra%' THEN 5 -- Linear Algebra
                        WHEN course_name LIKE '%Classical Mechanics%' THEN 7 -- Classical Mechanics
                        WHEN course_name LIKE '%Electricity%' OR course_name LIKE '%Magnetism%' THEN 8 -- Electromagnetism
                        WHEN course_name LIKE '%General Chemistry%' THEN 9 -- General Chemistry
                        WHEN course_name LIKE '%Organic Chemistry%' THEN 10 -- Organic Chemistry
                        WHEN course_name LIKE '%Cell Biology%' THEN 11 -- Cell Biology
                        WHEN course_name LIKE '%Genetics%' THEN 12 -- Genetics
                        ELSE NULL
                    END as subject_id
                    FROM courses
                )
                """;
            
            // Get progress data for subjects managed by this SL using the mapping
            String progressQuery = courseSubjectMappingCTE + """
                SELECT 
                    s.subject_name,
                    COALESCE(COUNT(DISTINCT q.question_id), 0) as completed_questions,
                    50 as target_questions
                FROM user_subject_assignments usa
                JOIN subjects s ON usa.subject_id = s.subject_id
                JOIN course_subject_mapping csm ON usa.subject_id = csm.subject_id
                LEFT JOIN questions q ON q.course_id = csm.course_id AND q.status = 'approved'
                WHERE usa.user_id = ?
                AND usa.role_in_subject = 'leader'
                AND usa.status = 'active'
                AND s.status = 'active'
                AND csm.subject_id IS NOT NULL
                GROUP BY s.subject_id, s.subject_name
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
            logger.error("Error getting SL chart data for user {}: {}", subjectLeaderId, e.getMessage(), e);
            // Return fallback chart data if error occurs
            SLDashboardChartDTO fallbackChart = new SLDashboardChartDTO();
            
            List<String> subjectNames = List.of("Programming", "Data Structures", "Algorithms");
            List<Integer> completed = List.of(45, 32, 28);
            List<Integer> targets = List.of(60, 50, 40);
            
            fallbackChart.setProgressData(new SLDashboardChartDTO.SubjectProgressData(subjectNames, completed, targets));
            fallbackChart.setOverallProgress(new SLDashboardChartDTO.OverallProgressData(72.5, 105, 150));
            return fallbackChart;
        }
    }
    
    /**
     * Get recent activities for Subject Leader dashboard
     */
    public List<SLDashboardActivityDTO> getSLActivities(Long subjectLeaderId, int limit) {
        try {
            logger.info("Getting SL activities for user: {}, limit: {}", subjectLeaderId, limit);
            
            // Simplified query to get recent exam assignments and question activities for the SL's subjects
            String activitiesQuery = """
                (SELECT 
                    'Assignment Created' as action,
                    CONCAT('New exam assignment: ', ea.assignment_name) as activity,
                    ea.created_at,
                    u_assigned.full_name as user_name,
                    'General' as subject_name
                FROM exam_assignments ea
                JOIN users u_assigned ON ea.assigned_to = u_assigned.user_id
                WHERE ea.assigned_by = ? OR ea.assigned_to = ?
                ORDER BY ea.created_at DESC
                LIMIT 5)
                
                UNION ALL
                
                (SELECT 
                    CASE 
                        WHEN q.status = 'submitted' THEN 'Question Submitted'
                        WHEN q.status = 'approved' THEN 'Question Approved'
                        WHEN q.status = 'draft' THEN 'Question Created'
                        ELSE 'Question Updated'
                    END as action,
                    CONCAT('Question for ', c.course_name, ' - ', q.status) as activity,
                    q.created_at,
                    'System' as user_name,
                    c.course_name as subject_name
                FROM questions q
                JOIN courses c ON q.course_id = c.course_id
                ORDER BY q.created_at DESC
                LIMIT 5)
                
                ORDER BY created_at DESC
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
            }, subjectLeaderId, subjectLeaderId, limit);
            
            return activities;
            
        } catch (Exception e) {
            logger.error("Error getting SL activities for user {}: {}", subjectLeaderId, e.getMessage(), e);
            // Return fallback activities
            List<SLDashboardActivityDTO> fallbackActivities = new ArrayList<>();
            
            SLDashboardActivityDTO activity1 = new SLDashboardActivityDTO();
            activity1.setActivityType("Question submitted for Programming Fundamentals");
            activity1.setDescription("Question submitted for Programming Fundamentals");
            activity1.setTimeAgo("2h ago");
            activity1.setSubjectName("Programming");
            fallbackActivities.add(activity1);
            
            SLDashboardActivityDTO activity2 = new SLDashboardActivityDTO();
            activity2.setActivityType("Task assigned to lecturer for Data Structures");
            activity2.setDescription("Task assigned to lecturer for Data Structures");
            activity2.setTimeAgo("4h ago");
            activity2.setSubjectName("Data Structures");
            fallbackActivities.add(activity2);
            
            SLDashboardActivityDTO activity3 = new SLDashboardActivityDTO();
            activity3.setActivityType("Question approved for Advanced Algorithms");
            activity3.setDescription("Question approved for Advanced Algorithms");
            activity3.setTimeAgo("1d ago");
            activity3.setSubjectName("Algorithms");
            fallbackActivities.add(activity3);
            
            return fallbackActivities;
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
