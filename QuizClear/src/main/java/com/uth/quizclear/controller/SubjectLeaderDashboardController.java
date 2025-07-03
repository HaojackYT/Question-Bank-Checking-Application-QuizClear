package com.uth.quizclear.controller;

import com.uth.quizclear.dto.SLDashboardStatsDTO;
import com.uth.quizclear.dto.SLDashboardChartDTO;
import com.uth.quizclear.dto.SLDashboardActivityDTO;
import com.uth.quizclear.service.SubjectLeaderDashboardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/subject-leader")
public class SubjectLeaderDashboardController {
    
    private static final Logger logger = LoggerFactory.getLogger(SubjectLeaderDashboardController.class);
    
    @Autowired
    private SubjectLeaderDashboardService dashboardService;
    
    @GetMapping("")
    public ResponseEntity<String> getSubjectLeaderDashboard(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        if (userId == null || role == null || !"SL".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Access denied: Not subject leader or not logged in");
        }
        return ResponseEntity.ok("Welcome to Subject Leader Dashboard! UserId: " + userId);
    }
    
    /**
     * Get dashboard statistics for Subject Leader
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getSLStats(HttpSession session) {
        try {
            logger.info("=== [SL DASHBOARD API] /stats called ===");
            Long userId = (Long) session.getAttribute("userId");
            String role = (String) session.getAttribute("role");
            logger.info("Session userId: {}, role: {}", userId, role);
            
            // Check if user is authenticated and has SL role
            if (userId == null || role == null || !"SL".equalsIgnoreCase(role)) {
                logger.warn("ACCESS DENIED - userId: {}, role: {}", userId, role);
                // For demo purposes, use a default SL user if not authenticated
                // userId = 4L; // Brian Carter - Subject Leader (user_id=4) - COMMENTED OUT
                // logger.warn("Using demo userId for testing: {}", userId);
                return ResponseEntity.status(403).body("Access denied: Not subject leader or not logged in");
            }
            
            logger.info("Getting SL dashboard stats for user: {}", userId);
            SLDashboardStatsDTO stats = dashboardService.getSLStats(userId);
            logger.info("Stats retrieved successfully: {}", stats);
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("Error getting SL dashboard stats: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error retrieving dashboard statistics");
        }
    }
    
    /**
     * Get chart data for Subject Leader dashboard
     */
    @GetMapping("/chart-data")
    public ResponseEntity<?> getSLChartData(HttpSession session) {
        try {
            logger.info("=== [SL DASHBOARD API] /chart-data called ===");
            Long userId = (Long) session.getAttribute("userId");
            String role = (String) session.getAttribute("role");
            logger.info("Session userId: {}, role: {}", userId, role);
            
            // TEMPORARY: Use hardcoded userId for testing DB data
            if (userId == null) {
                // userId = 4L; // Brian Carter - Subject Leader (user_id=4) - COMMENTED OUT
                // logger.warn("Using hardcoded userId for testing: {}", userId);
                logger.warn("User not authenticated - userId is null");
                return ResponseEntity.status(403).body("Access denied: Not logged in");
            }
            
            logger.info("Getting SL chart data for user: {}", userId);
            SLDashboardChartDTO chartData = dashboardService.getSLChartData(userId);
            logger.info("Chart data retrieved successfully: {}", chartData);
            return ResponseEntity.ok(chartData);
            
        } catch (Exception e) {
            logger.error("Error getting SL chart data: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error retrieving chart data");
        }
    }
    
    /**
     * Get recent activities for Subject Leader dashboard
     */
    @GetMapping("/activities")
    public ResponseEntity<?> getSLActivities(HttpSession session, 
                                           @RequestParam(defaultValue = "10") int limit) {
        try {
            logger.info("=== [SL DASHBOARD API] /activities called with limit: {} ===", limit);
            Long userId = (Long) session.getAttribute("userId");
            String role = (String) session.getAttribute("role");
            logger.info("Session userId: {}, role: {}", userId, role);
            
            // TEMPORARY: Use hardcoded userId for testing DB data
            if (userId == null) {
                // userId = 4L; // Brian Carter - Subject Leader (user_id=4) - COMMENTED OUT
                // logger.warn("Using hardcoded userId for testing: {}", userId);
                logger.warn("User not authenticated - userId is null");
                return ResponseEntity.status(403).body("Access denied: Not logged in");
            }
            
            logger.info("Getting SL activities for user: {}, limit: {}", userId, limit);
            List<SLDashboardActivityDTO> activities = dashboardService.getSLActivities(userId, limit);
            logger.info("Activities retrieved successfully: {} items", activities.size());
            return ResponseEntity.ok(activities);
            
        } catch (Exception e) {
            logger.error("Error getting SL activities: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving activities");
        }
    }
    
    /**
     * Test session endpoint for debugging
     */
    @GetMapping("/test-session")
    public ResponseEntity<?> testSession(HttpSession session) {
        logger.info("=== [TEST SESSION] ===");
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        String email = (String) session.getAttribute("email");
        
        logger.info("Session ID: {}", session.getId());
        logger.info("Session userId: {}", userId);
        logger.info("Session role: {}", role);
        logger.info("Session email: {}", email);
        
        return ResponseEntity.ok(Map.of(
            "sessionId", session.getId(),
            "userId", userId,
            "role", role,
            "email", email,
            "isAuthenticated", userId != null,
            "isSubjectLeader", "SL".equalsIgnoreCase(role)
        ));
    }
    
    /**
     * Get any Subject Leader ID from database for demo
     */
    @GetMapping("/demo-data")
    public ResponseEntity<?> getDemoData() {
        try {
            logger.info("=== [DEMO DATA] Getting any SL user for demo ===");
            
            // Query to get any active Subject Leader
            Long demoUserId = dashboardService.getAnySubjectLeaderUserId();
            if (demoUserId == null) {
                return ResponseEntity.status(404).body("No Subject Leader found in database");
            }
            
            logger.info("Using demo SL userId: {}", demoUserId);
            
            // Get all dashboard data for this user
            SLDashboardStatsDTO stats = dashboardService.getSLStats(demoUserId);
            SLDashboardChartDTO chartData = dashboardService.getSLChartData(demoUserId);
            List<SLDashboardActivityDTO> activities = dashboardService.getSLActivities(demoUserId, 15);
            
            return ResponseEntity.ok(Map.of(
                "demoUserId", demoUserId,
                "stats", stats,
                "chartData", chartData,
                "activities", activities,
                "message", "Demo data from real database"
            ));
            
        } catch (Exception e) {
            logger.error("Error getting demo data: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error retrieving demo data");
        }
    }
}

