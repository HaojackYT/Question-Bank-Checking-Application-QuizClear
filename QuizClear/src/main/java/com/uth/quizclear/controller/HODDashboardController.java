package com.uth.quizclear.controller;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uth.quizclear.model.dto.HODDashboardActivityDTO;
import com.uth.quizclear.model.dto.HODDashboardChartsDTO;
import com.uth.quizclear.model.dto.HODDashboardDeadlineDTO;
import com.uth.quizclear.model.dto.HODDashboardStatsDTO;
import com.uth.quizclear.service.HODDashboardService;

@RestController
@RequestMapping("/api/dashboard/hod")
public class HODDashboardController {

    @Autowired
    private HODDashboardService dashboardService;

    /**
     * GET /api/dashboard/hod
     * Hiển thị thông báo chào mừng nếu người dùng là HOD
     */

    @GetMapping("")
    public ResponseEntity<String> getHODDashboard(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null || !"HOD".equalsIgnoreCase(role)) {
            System.out.println("Access denied: Missing or invalid session info");
            return ResponseEntity.status(403).body("Access denied: Not HOD or not logged in");
        }

        return ResponseEntity.ok("Welcome to HOD Dashboard! UserId: " + userId);
    }

    /**
     * GET /api/dashboard/hod/stats
     * Trả về số liệu thống kê cho HOD Dashboard
     */
    @GetMapping("/stats")
    public ResponseEntity<HODDashboardStatsDTO> getStats(HttpSession session) {
        // Kiểm tra session để xác định quyền truy cập
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null || !"HOD".equalsIgnoreCase(role)) {
            System.out.println("Access denied: Missing or invalid session info");
            return ResponseEntity.status(403).body(null);
        }

        HODDashboardStatsDTO stats = dashboardService.getStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * GET /api/dashboard/hod/subject-progress
     * Trả về dữ liệu biểu đồ cho HOD Dashboard
     */
    @GetMapping("/subject-progress")
    public ResponseEntity<List<HODDashboardChartsDTO>> getBarCharts() {
        try {
            List<HODDashboardChartsDTO> charts = dashboardService.getBarCharts();
            return ResponseEntity.ok(charts);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/progress-percent")
    public ResponseEntity<Double> getProgressPercent() {
        try {
            double progress = dashboardService.getOverallProgress();
            return ResponseEntity.ok(progress);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/deadlines")
    public ResponseEntity<List<HODDashboardDeadlineDTO>> getDeadlines() {
        try {
            List<HODDashboardDeadlineDTO> deadlines = dashboardService.getDeadlines();
            return ResponseEntity.ok(deadlines);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/activities")
    public ResponseEntity<List<HODDashboardActivityDTO>> getActivities() {
        try {
            List<HODDashboardActivityDTO> activities = dashboardService.getRecentActivities();
            return ResponseEntity.ok(activities);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}