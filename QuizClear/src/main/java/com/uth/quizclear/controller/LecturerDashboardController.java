package com.uth.quizclear.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard/lecturer")
public class LecturerDashboardController {
    @GetMapping("")
    public ResponseEntity<String> getLecturerDashboard(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        if (userId == null || role == null || !"LEC".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body("Access denied: Not lecturer or not logged in");
        }
        return ResponseEntity.ok("Welcome to Lecturer Dashboard! UserId: " + userId);
    }
}
