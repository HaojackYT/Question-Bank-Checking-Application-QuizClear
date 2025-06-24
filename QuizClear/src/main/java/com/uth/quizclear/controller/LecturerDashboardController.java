package com.uth.quizclear.controller;

import java.util.List;

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

    @GetMapping("")
    public ResponseEntity<String> getLecturerDashboard(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        if (userId == null || role == null || !"Lec".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Access denied: Not lecturer or not logged in");
        }
        return ResponseEntity.ok("Welcome to Lecturer Dashboard! UserId: " + userId);
    }

    // Temporary test endpoint for debugging
    @GetMapping("/test-stats")
    public ResponseEntity<LecturerDashboardStatsDTO> getTestStats() {
        logger.info("Test stats API called (no session required)");
        // Use hardcoded userId = 1 (Ash Abrahams - Lecturer)
        LecturerDashboardStatsDTO stats = lecturerDashboardService.getStats(1L);
        logger.info("Test stats retrieved: {}", stats);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/test-tasks")
    public ResponseEntity<List<LecturerDashboardTaskDTO>> getTestTasks() {
        logger.info("Test tasks API called (no session required)");
        // Use hardcoded userId = 1 (Ash Abrahams - Lecturer)
        List<LecturerDashboardTaskDTO> tasks = lecturerDashboardService.getCurrentTasks(1L);
        logger.info("Test tasks retrieved: {}", tasks);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/test-activities")
    public ResponseEntity<List<LecturerDashboardActivityDTO>> getTestActivities() {
        logger.info("Test activities API called (no session required)");
        // Use hardcoded userId = 1 (Ash Abrahams - Lecturer)
        List<LecturerDashboardActivityDTO> activities = lecturerDashboardService.getRecentActivities(1L);
        logger.info("Test activities retrieved: {}", activities);
        return ResponseEntity.ok(activities);
    }
}
