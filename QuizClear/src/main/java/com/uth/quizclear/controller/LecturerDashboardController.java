package com.uth.quizclear.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uth.quizclear.model.dto.LecturerDashboardActivityDTO;
import com.uth.quizclear.model.dto.LecturerDashboardStatsDTO;
import com.uth.quizclear.model.dto.LecturerDashboardTaskDTO;
import com.uth.quizclear.service.LecturerDashboardService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/dashboard/lecturer")
public class LecturerDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(LecturerDashboardController.class);

    @Autowired
    private LecturerDashboardService lecturerDashboardService;

    @GetMapping("/stats")
    public ResponseEntity<LecturerDashboardStatsDTO> getStats(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        logger.info("Stats API called - UserId: {}, Role: {}", userId, role);

        if (userId == null || role == null || !"Lec".equalsIgnoreCase(role)) {
            logger.warn("Access denied for stats API - UserId: {}, Role: {}", userId, role);
            return ResponseEntity.status(403).build();
        }

        LecturerDashboardStatsDTO stats = lecturerDashboardService.getStats(userId);
        logger.info("Stats retrieved for user {}: {}", userId, stats);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<LecturerDashboardTaskDTO>> getCurrentTasks(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null || !"Lec".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).build();
        }

        List<LecturerDashboardTaskDTO> tasks = lecturerDashboardService.getCurrentTasks(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/activities")
    public ResponseEntity<List<LecturerDashboardActivityDTO>> getRecentActivities(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null || !"Lec".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).build();
        }

        List<LecturerDashboardActivityDTO> activities = lecturerDashboardService.getRecentActivities(userId);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/scope")
    public ResponseEntity<Map<String, Object>> getLecturerScope(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        logger.info("Scope API called - UserId: {}, Role: {}", userId, role);

        if (userId == null || role == null || !"Lec".equalsIgnoreCase(role)) {
            logger.warn("Access denied for scope API - UserId: {}, Role: {}", userId, role);
            return ResponseEntity.status(403).build();
        }

        try {
            Map<String, Object> scopeData = lecturerDashboardService.getLecturerScope(userId);
            logger.info("Scope data retrieved for user {}: {}", userId, scopeData);
            return ResponseEntity.ok(scopeData);
        } catch (Exception e) {
            logger.error("Error getting lecturer scope for user {}: ", userId, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/chart-data")
    public ResponseEntity<Map<String, Object>> getChartData(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        logger.info("Chart data API called - UserId: {}, Role: {}", userId, role);

        if (userId == null || role == null || !"Lec".equalsIgnoreCase(role)) {
            logger.warn("Access denied for chart data API - UserId: {}, Role: {}", userId, role);
            return ResponseEntity.status(403).build();
        }

        try {
            Map<String, Object> chartData = lecturerDashboardService.getChartData(userId);
            logger.info("Chart data retrieved for user {}: {}", userId, chartData);
            return ResponseEntity.ok(chartData);
        } catch (Exception e) {
            logger.error("Error getting chart data for user {}: ", userId, e);
            return ResponseEntity.status(500).build();
        }
    }

    // TEMPORARY TEST ENDPOINT - REMOVE IN PRODUCTION
    @GetMapping("/test-session")
    public ResponseEntity<String> setTestSession(HttpSession session) {
        // Set test session for Ash Abrahams (ID: 1, Role: Lec)
        session.setAttribute("userId", 1L);
        session.setAttribute("role", "Lec");
        session.setAttribute("isLoggedIn", true);

        logger.info("Test session set - UserId: 1, Role: Lec");
        return ResponseEntity.ok("Test session set for Lecturer ID: 1");
    }

    @GetMapping("")
    public ResponseEntity<String> getLecturerDashboard(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        if (userId == null || role == null || !"Lec".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Access denied: Not lecturer or not logged in");
        }
        return ResponseEntity.ok("Welcome to Lecturer Dashboard! UserId: " + userId);
    }
}
