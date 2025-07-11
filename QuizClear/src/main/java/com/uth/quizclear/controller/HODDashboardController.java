package com.uth.quizclear.controller;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.uth.quizclear.model.dto.HODDashboardActivityDTO;
import com.uth.quizclear.model.dto.HODDashboardChartsDTO;
import com.uth.quizclear.model.dto.HODDashboardDeadlineDTO;
import com.uth.quizclear.model.dto.HODDashboardStatsDTO;
import com.uth.quizclear.service.HODDashboardService;

@RestController
@RequestMapping("/api")

public class HODDashboardController {

    @Autowired
    private HODDashboardService dashboardService;

    // Hàm kiểm tra quyền truy cập của HOD
    private boolean isHOD(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        return userId != null && role != null && "HOD".equalsIgnoreCase(role);
    }

    @GetMapping("")
    public ResponseEntity<String> getHODDashboard(HttpSession session) {
        if (!isHOD(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Not HOD or not logged in");
        }
        Long userId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok("Welcome to HOD Dashboard! UserId: " + userId);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(HttpSession session) {
        if (!isHOD(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Not HOD or not logged in");
        }

        try {
            Long userId = (Long) session.getAttribute("userId");
            HODDashboardStatsDTO stats = dashboardService.getStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load stats.");
        }
    }

    @GetMapping("/subject-progress")
    public ResponseEntity<?> getBarCharts(HttpSession session) {
        if (!isHOD(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Not HOD or not logged in");
        }

        try {
            Long userId = (Long) session.getAttribute("userId");
            List<HODDashboardChartsDTO> charts = dashboardService.getBarCharts(userId);
            return ResponseEntity.ok(charts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load charts.");
        }
    }

    @GetMapping("/progress-percent")
    public ResponseEntity<?> getProgressPercent(HttpSession session) {
        if (!isHOD(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Not HOD or not logged in");
        }

        try {
            Long userId = (Long) session.getAttribute("userId");
            double progress = dashboardService.getOverallProgress(userId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load progress.");
        }
    }

    @GetMapping("/overall-progress")
    public ResponseEntity<?> getOverallProgress(HttpSession session) {
        if (!isHOD(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Not HOD or not logged in");
        }

        try {
            Long userId = (Long) session.getAttribute("userId");
            double progress = dashboardService.getOverallProgress(userId);
            return ResponseEntity.ok(Map.of("percentage", progress));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load progress.");
        }
    }

    @GetMapping("/deadlines")
    public ResponseEntity<?> getDeadlines(HttpSession session) {
        if (!isHOD(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Not HOD or not logged in");
        }

        try {
            Long userId = (Long) session.getAttribute("userId");
            List<HODDashboardDeadlineDTO> deadlines = dashboardService.getDeadlines(userId);
            return ResponseEntity.ok(deadlines);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load deadlines.");
        }
    }

    @GetMapping("/activities")
    public ResponseEntity<?> getActivities(HttpSession session) {
        if (!isHOD(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Not HOD or not logged in");
        }

        try {
            Long userId = (Long) session.getAttribute("userId");
            List<HODDashboardActivityDTO> activities = dashboardService.getRecentActivities(userId);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load activities.");
        }
    }
}

