package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.StaffDashboardDTO;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.service.StaffDashboardService;
import com.uth.quizclear.service.UserService;
import com.uth.quizclear.service.ScopeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/api/dashboard/staff")
public class StaffDashboardController {

    @Autowired
    private StaffDashboardService staffDashboardService;

    @Autowired
    private UserService userService;    @Autowired
    private ScopeService scopeService;

    /**
     * Get staff dashboard statistics with scope filtering
     */
    @GetMapping("/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStaffStats(
            @RequestParam(required = false) String departmentIds,
            @RequestParam(required = false) String subjectIds,
            @RequestParam(required = false) Long requestingUserId,
            @RequestParam(required = false) String userRole,
            Authentication authentication,
            HttpSession session) {

        try {
            // Get current user ID
            Long userId = getCurrentUserId(authentication, session);
            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
            }

            // Verify user has staff/admin permissions
            User user = userService.findById(userId).orElse(null);
            if (user == null || !hasStaffPermissions(user)) {
                return ResponseEntity.status(403).body(Map.of("error", "Insufficient permissions"));
            }

            // Get dashboard data with scope filtering
            StaffDashboardDTO dashboard = staffDashboardService.getDashboardForStaff(userId);
            
            Map<String, Object> stats = new HashMap<>();
            
            // Basic statistics
            stats.put("totalSubjects", dashboard.getTotalSubjects());
            stats.put("totalQuestions", dashboard.getTotalQuestions());
            stats.put("duplicateQuestions", dashboard.getDuplicateQuestions());
            stats.put("examsCreated", dashboard.getExamsCreated());
            
            // Monthly statistics
            stats.put("subjectsThisMonth", dashboard.getSubjectsThisMonth());
            stats.put("questionsThisMonth", dashboard.getQuestionsThisMonth());
            stats.put("examsThisMonth", dashboard.getExamsThisMonth());
            
            // Scope information
            stats.put("scopeInfo", getScopeInfo(userId));
            
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error loading staff statistics");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Get chart data for staff dashboard
     */
    @GetMapping("/chart-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getChartData(Authentication authentication, HttpSession session) {
        try {
            Long userId = getCurrentUserId(authentication, session);
            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
            }

            StaffDashboardDTO dashboard = staffDashboardService.getDashboardForStaff(userId);
            
            Map<String, Object> chartData = new HashMap<>();
            chartData.put("barChart", dashboard.getBarChart());
            chartData.put("pieChart", dashboard.getPieChart());
            
            return ResponseEntity.ok(chartData);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error loading chart data"));
        }
    }

    /**
     * Get recent activities for staff dashboard
     */
    @GetMapping("/activities")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getRecentActivities(
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication, 
            HttpSession session) {
        
        try {
            Long userId = getCurrentUserId(authentication, session);
            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
            }

            StaffDashboardDTO dashboard = staffDashboardService.getDashboardForStaff(userId);
            
            Map<String, Object> activities = new HashMap<>();
            activities.put("recentTasks", dashboard.getRecentTasks());
            activities.put("duplicateWarnings", dashboard.getDuplicateWarnings());
            activities.put("totalDuplicateWarnings", dashboard.getDuplicateWarnings().size());
            
            return ResponseEntity.ok(activities);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error loading activities"));
        }
    }

    /**
     * Get detailed statistics for staff dashboard
     */
    @GetMapping("/detailed-stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDetailedStats(
            Authentication authentication, 
            HttpSession session) {
        
        try {
            Long userId = getCurrentUserId(authentication, session);
            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
            }            // Get detailed stats from the existing dashboard data
            StaffDashboardDTO dashboard = staffDashboardService.getDashboardForStaff(userId);
            
            Map<String, Object> detailedStats = new HashMap<>();
            detailedStats.put("dashboard", dashboard);
            detailedStats.put("barChart", dashboard.getBarChart());
            detailedStats.put("pieChart", dashboard.getPieChart());
            detailedStats.put("recentTasks", dashboard.getRecentTasks());
            detailedStats.put("duplicateWarnings", dashboard.getDuplicateWarnings());
            
            return ResponseEntity.ok(detailedStats);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error loading detailed statistics"));
        }
    }

    // Helper methods
    private Long getCurrentUserId(Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.findByEmail(email).orElse(null);
            return user != null ? user.getUserId().longValue() : null;
        }
        
        // Fallback to session
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        } else if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        }
        
        return null;
    }

    private boolean hasStaffPermissions(User user) {
        // Check if user has staff-level permissions (RD, HOED, HOD, etc.)
        switch (user.getRole()) {
            case RD:
            case HOED:
            case HOD:
            case SL:
                return true;
            default:
                return false;
        }
    }

    private Map<String, Object> getScopeInfo(Long userId) {
        Map<String, Object> scopeInfo = new HashMap<>();
        
        try {
            User user = userService.findById(userId).orElse(null);
            if (user != null) {
                scopeInfo.put("userRole", user.getRole().name());
                scopeInfo.put("canViewAllData", user.getRole().name().equals("RD"));
                
                // Get accessible departments and subjects
                List<Long> accessibleDeptIds = scopeService.getUserDepartmentIds(userId);
                List<Long> accessibleSubjectIds = scopeService.getUserSubjectIds(userId);
                
                scopeInfo.put("accessibleDepartmentIds", accessibleDeptIds);
                scopeInfo.put("accessibleSubjectIds", accessibleSubjectIds);
                scopeInfo.put("totalAccessibleDepartments", accessibleDeptIds.size());
                scopeInfo.put("totalAccessibleSubjects", accessibleSubjectIds.size());
            }
        } catch (Exception e) {
            scopeInfo.put("error", "Unable to load scope information");
        }
        
        return scopeInfo;
    }
}
