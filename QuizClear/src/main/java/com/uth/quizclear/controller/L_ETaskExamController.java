package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.L_ETaskExamDTO;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.service.L_ETaskExamService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lecturer")
public class L_ETaskExamController {

    private static final Logger logger = LoggerFactory.getLogger(L_ETaskExamController.class);

    @Autowired
    private L_ETaskExamService taskService;

    @Autowired
    private UserRepository userRepository; // Thêm dependency

    /**
     * Helper method to check if user is lecturer
     */
    private boolean isLecturer(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_LEC") || a.getAuthority().equals("ROLE_Lec"));
    }

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
    }

    @GetMapping("/EETaskExam")
    public String getTaskExamPage(HttpSession session, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized access attempt to EETaskExam");
            return "redirect:/login";
        }
        if (!isLecturer(authentication)) {
            logger.warn("Access denied - not lecturer role");
            return "redirect:/login";
        }
        Long userId = getUserIdFromEmail(authentication.getName());
        if (userId == null) {
            logger.error("UserId not found, redirecting to login");
            return "redirect:/login";
        }
        logger.info("Fetching tasks for userId: {}", userId);
        model.addAttribute("userId", userId); // Truyền để debug
        return "lecturerEETaskExam";
    }

    @GetMapping("/api/current-user")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpSession session, Authentication authentication) {
        logger.info("API call for current user");
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.error("Unauthorized access to current user or no session");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "Unauthorized or no session"));
            }
            if (!isLecturer(authentication)) {
                logger.warn("Access denied - not lecturer role");
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "Not a lecturer"));
            }
            Long userId = getUserIdFromEmail(authentication.getName());
            if (userId == null) {
                logger.warn("Could not retrieve valid lecturer ID, returning null response");
                return ResponseEntity.ok(Map.of("success", false, "message", "User ID not found"));
            }
            logger.info("Returning userId: {}", userId);
            return ResponseEntity.ok(Map.of("success", true, "userId", userId));
        } catch (Exception e) {
            logger.error("Error fetching current user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/api/exam-tasks")
    @ResponseBody
    public ResponseEntity<List<L_ETaskExamDTO>> getExamTasks(HttpSession session, Authentication authentication) {
        logger.info("API call for exam tasks");
        try {
            if (authentication == null || !authentication.isAuthenticated() || !isLecturer(authentication)) {
                logger.error("Unauthorized access to exam tasks");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            Long userId = getUserIdFromEmail(authentication.getName());
            if (userId == null) {
                logger.warn("UserId is null, returning empty list");
                return ResponseEntity.ok(List.of());
            }
            List<L_ETaskExamDTO> tasks = taskService.getCreateExamTasksByUserId(userId.intValue());
            if (tasks.isEmpty()) {
                logger.warn("No create_exam tasks found for userId: {}", userId);
            } else {
                logger.info("Found {} tasks for userId: {}", tasks.size(), userId);
            }
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error fetching exam tasks: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/api/task-exam/{taskId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTaskDetails(@PathVariable Long taskId, HttpSession session, Authentication authentication) {
        logger.info("API call for task details, taskId: {}", taskId);
        try {
            if (authentication == null || !authentication.isAuthenticated() || !isLecturer(authentication)) {
                logger.error("Unauthorized access to task details for taskId: {}", taskId);
                return ResponseEntity.ok(Map.of("success", false, "message", "Unauthorized"));
            }
            Long userId = getUserIdFromEmail(authentication.getName());
            if (userId == null) {
                logger.error("UserId is null for taskId: {}", taskId);
                return ResponseEntity.ok(Map.of("success", false, "message", "User not authenticated"));
            }
            L_ETaskExamDTO taskDTO = taskService.getTaskDetails(taskId.intValue(), userId.intValue());
            if (taskDTO == null) {
                logger.warn("TaskDTO is null for taskId: {}", taskId);
                return ResponseEntity.ok(Map.of("success", false, "message", "Task not found"));
            }
            return ResponseEntity.ok(Map.of("success", true, "data", taskDTO));
        } catch (Exception e) {
            logger.error("Error fetching task details for taskId: {}. Error: {}", taskId, e.getMessage());
            return ResponseEntity.ok(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/api/task-exam/{taskId}/accept")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> acceptTask(@PathVariable Long taskId, HttpSession session, Authentication authentication) {
        logger.info("API call to accept taskId: {}", taskId);
        try {
            if (authentication == null || !authentication.isAuthenticated() || !isLecturer(authentication)) {
                logger.error("Unauthorized attempt to accept taskId: {}", taskId);
                return ResponseEntity.ok(Map.of("success", false, "message", "Unauthorized"));
            }
            Long userId = getUserIdFromEmail(authentication.getName());
            if (userId == null) {
                logger.error("UserId is null for taskId: {}", taskId);
                return ResponseEntity.ok(Map.of("success", false, "message", "User not authenticated"));
            }
            taskService.acceptTask(taskId.intValue(), userId.intValue());
            return ResponseEntity.ok(Map.of("success", true, "message", "Task accepted successfully"));
        } catch (Exception e) {
            logger.error("Error accepting taskId: {}. Error: {}", taskId, e.getMessage());
            return ResponseEntity.ok(Map.of("success", false, "message", e.getMessage()));
        }
    }
}