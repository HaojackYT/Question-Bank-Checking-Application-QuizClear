package com.uth.quizclear.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uth.quizclear.model.dto.LecturerDashboardActivityDTO;
import com.uth.quizclear.model.dto.LecturerDashboardStatsDTO;
import com.uth.quizclear.model.dto.LecturerDashboardTaskDTO;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.service.LecturerDashboardService;

@RestController
@RequestMapping("/api/dashboard/lecturer")
public class LecturerDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(LecturerDashboardController.class);

    @Autowired
    private LecturerDashboardService lecturerDashboardService;

    /**
     * Helper method to check if user is lecturer
     */
    private boolean isLecturer(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_LEC") || a.getAuthority().equals("ROLE_Lec"));
    }

    @Autowired
    private UserRepository userRepository;

    /**
     * Helper method to get userId from email
     */
    private Long getUserIdFromEmail(String email) {
        // Map emails to correct user IDs based on sample data
        switch (email) {
            case "ash.abrahams@university.edu":
                return 1L;
            case "daniel.evans@university.edu":
                return 5L;
            case "frank.green@university.edu":
                return 7L;
            default:
                // Query database for other users
                return userRepository.findByEmail(email)
                    .map(user -> user.getUserId().longValue())
                    .orElse(1L); // fallback
        }
    }    @GetMapping("/stats")
    public ResponseEntity<LecturerDashboardStatsDTO> getStats(Authentication authentication) {
        logger.info("Stats API called for user: {}", authentication.getName());
        
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Access denied - not authenticated");
            return ResponseEntity.status(403).build();
        }
        
        if (!isLecturer(authentication)) {
            logger.warn("Access denied - not lecturer role");
            return ResponseEntity.status(403).build();
        }
        
        Long userId = getUserIdFromEmail(authentication.getName());
        LecturerDashboardStatsDTO stats = lecturerDashboardService.getStats(userId);
        logger.info("Stats retrieved for user {}: {}", userId, stats);
        return ResponseEntity.ok(stats);
    }    @GetMapping("/tasks")
    public ResponseEntity<List<LecturerDashboardTaskDTO>> getCurrentTasks(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || !isLecturer(authentication)) {
            return ResponseEntity.status(403).build();
        }

        Long userId = getUserIdFromEmail(authentication.getName());
        List<LecturerDashboardTaskDTO> tasks = lecturerDashboardService.getCurrentTasks(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/activities")
    public ResponseEntity<List<LecturerDashboardActivityDTO>> getRecentActivities(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || !isLecturer(authentication)) {
            return ResponseEntity.status(403).build();
        }

        Long userId = getUserIdFromEmail(authentication.getName());
        List<LecturerDashboardActivityDTO> activities = lecturerDashboardService.getRecentActivities(userId);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/scope")
    public ResponseEntity<Map<String, Object>> getLecturerScope(Authentication authentication) {
        logger.info("Scope API called for user: {}", authentication.getName());

        if (authentication == null || !authentication.isAuthenticated() || !isLecturer(authentication)) {
            logger.warn("Access denied for scope API");
            return ResponseEntity.status(403).build();
        }

        try {
            Long userId = getUserIdFromEmail(authentication.getName());
            Map<String, Object> scopeData = lecturerDashboardService.getLecturerScope(userId);
            logger.info("Scope data retrieved for user {}: {}", userId, scopeData);
            return ResponseEntity.ok(scopeData);
        } catch (Exception e) {
            logger.error("Error getting lecturer scope: ", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/chart-data")
    public ResponseEntity<Map<String, Object>> getChartData(Authentication authentication) {
        logger.info("Chart data API called for user: {}", authentication.getName());

        if (authentication == null || !authentication.isAuthenticated() || !isLecturer(authentication)) {
            logger.warn("Access denied for chart data API");
            return ResponseEntity.status(403).build();
        }

        try {
            Long userId = getUserIdFromEmail(authentication.getName());
            Map<String, Object> chartData = lecturerDashboardService.getChartData(userId);
            logger.info("Chart data retrieved for user {}: {}", userId, chartData);
            return ResponseEntity.ok(chartData);
        } catch (Exception e) {
            logger.error("Error getting chart data: ", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("")
    public ResponseEntity<String> getLecturerDashboard(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || !isLecturer(authentication)) {
            return ResponseEntity.status(403).body("Access denied: Not lecturer or not logged in");
        }
        
        Long userId = getUserIdFromEmail(authentication.getName());
        return ResponseEntity.ok("Welcome to Lecturer Dashboard! UserId: " + userId);
    }
}
