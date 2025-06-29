package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.ExamAssignmentDTO;
import com.uth.quizclear.model.dto.ExamAssignmentRequestDTO;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.ExamAssignmentStatus;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.service.ExamAssignmentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for Subject Leader Exam Assignment functionality
 * Handles all exam assignment operations for Subject Leaders
 */
@Controller
@RequestMapping("/subject-leader/exam-assignments")
@RequiredArgsConstructor
@Slf4j
public class SLExamAssignmentController {

    private final ExamAssignmentService examAssignmentService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    /**
     * Display the exam assignment page
     */
    @GetMapping
    public String examAssignmentPage(Model model, HttpSession session) {
        log.info("Accessing exam assignment page");

        // Get current user from session (you may need to adjust this based on your
        // authentication)
        Long currentUserId = getCurrentUserId(session);
        if (currentUserId == null) {
            return "redirect:/login";
        }

        try {
            // Get assignments
            List<ExamAssignmentDTO> assignments = examAssignmentService.getAssignmentsBySubjectLeader(currentUserId);

            // Get statistics
            Map<String, Long> statistics = examAssignmentService.getAssignmentStatistics(currentUserId);

            // Get overdue assignments
            List<ExamAssignmentDTO> overdueAssignments = examAssignmentService.getOverdueAssignments(currentUserId);

            // Get available courses
            List<Course> courses = courseRepository.findAll();
            // Get available lecturers
            List<User> lecturers = userRepository.findByRole(UserRole.LEC);

            // Add to model
            model.addAttribute("assignments", assignments);
            model.addAttribute("statistics", statistics);
            model.addAttribute("overdueAssignments", overdueAssignments);
            model.addAttribute("courses", courses);
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("assignmentStatuses", ExamAssignmentStatus.values());

            return "subjectLeader/SLExamAssignment";

        } catch (Exception e) {
            log.error("Error loading exam assignment page", e);
            model.addAttribute("error", "Error loading page: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Get assignments with pagination and filters (AJAX)
     */
    @GetMapping("/api/assignments")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long assignedToId,
            HttpSession session) {

        Long currentUserId = getCurrentUserId(session);
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            ExamAssignmentStatus statusEnum = null;
            if (status != null && !status.trim().isEmpty()) {
                statusEnum = ExamAssignmentStatus.valueOf(status.toUpperCase());
            }

            Page<ExamAssignmentDTO> assignments = examAssignmentService.getAssignmentsWithPagination(
                    currentUserId, statusEnum, courseId, assignedToId, page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("content", assignments.getContent());
            response.put("totalElements", assignments.getTotalElements());
            response.put("totalPages", assignments.getTotalPages());
            response.put("currentPage", assignments.getNumber());
            response.put("size", assignments.getSize());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error fetching assignments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Create new assignment (AJAX)
     */
    @PostMapping("/api/assignments")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createAssignment(
            @Valid @RequestBody ExamAssignmentRequestDTO requestDTO,
            BindingResult bindingResult,
            HttpSession session) {

        Long currentUserId = getCurrentUserId(session);
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (bindingResult.hasErrors()) {
            Map<String, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            ExamAssignmentDTO created = examAssignmentService.createAssignment(requestDTO, currentUserId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Assignment created successfully",
                    "assignment", created));

        } catch (Exception e) {
            log.error("Error creating assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update assignment (AJAX)
     */
    @PutMapping("/api/assignments/{assignmentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateAssignment(
            @PathVariable Long assignmentId,
            @Valid @RequestBody ExamAssignmentRequestDTO requestDTO,
            BindingResult bindingResult,
            HttpSession session) {

        Long currentUserId = getCurrentUserId(session);
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (bindingResult.hasErrors()) {
            Map<String, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            ExamAssignmentDTO updated = examAssignmentService.updateAssignment(assignmentId, requestDTO, currentUserId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Assignment updated successfully",
                    "assignment", updated));

        } catch (Exception e) {
            log.error("Error updating assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete assignment (AJAX)
     */
    @DeleteMapping("/api/assignments/{assignmentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteAssignment(
            @PathVariable Long assignmentId,
            HttpSession session) {

        Long currentUserId = getCurrentUserId(session);
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            examAssignmentService.deleteAssignment(assignmentId, currentUserId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Assignment deleted successfully"));

        } catch (Exception e) {
            log.error("Error deleting assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get assignment details (AJAX)
     */
    @GetMapping("/api/assignments/{assignmentId}")
    @ResponseBody
    public ResponseEntity<ExamAssignmentDTO> getAssignmentDetails(@PathVariable Long assignmentId) {
        try {
            ExamAssignmentDTO assignment = examAssignmentService.getAssignmentById(assignmentId);
            return ResponseEntity.ok(assignment);

        } catch (Exception e) {
            log.error("Error fetching assignment details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Search assignments (AJAX)
     */
    @GetMapping("/api/assignments/search")
    @ResponseBody
    public ResponseEntity<List<ExamAssignmentDTO>> searchAssignments(
            @RequestParam String searchTerm,
            HttpSession session) {

        Long currentUserId = getCurrentUserId(session);
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<ExamAssignmentDTO> assignments = examAssignmentService.searchAssignments(searchTerm, currentUserId);
            return ResponseEntity.ok(assignments);

        } catch (Exception e) {
            log.error("Error searching assignments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get assignment statistics (AJAX)
     */
    @GetMapping("/api/statistics")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> getStatistics(HttpSession session) {
        Long currentUserId = getCurrentUserId(session);
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Map<String, Long> statistics = examAssignmentService.getAssignmentStatistics(currentUserId);
            return ResponseEntity.ok(statistics);

        } catch (Exception e) {
            log.error("Error fetching statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get overdue assignments (AJAX)
     */
    @GetMapping("/api/assignments/overdue")
    @ResponseBody
    public ResponseEntity<List<ExamAssignmentDTO>> getOverdueAssignments(HttpSession session) {
        Long currentUserId = getCurrentUserId(session);
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<ExamAssignmentDTO> overdueAssignments = examAssignmentService.getOverdueAssignments(currentUserId);
            return ResponseEntity.ok(overdueAssignments);

        } catch (Exception e) {
            log.error("Error fetching overdue assignments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get available courses for dropdown (AJAX)
     */
    @GetMapping("/api/courses")
    @ResponseBody
    public ResponseEntity<List<Course>> getCourses() {
        try {
            List<Course> courses = courseRepository.findAll();
            return ResponseEntity.ok(courses);

        } catch (Exception e) {
            log.error("Error fetching courses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get available lecturers for dropdown (AJAX)
     */
    @GetMapping("/api/lecturers")
    @ResponseBody
    public ResponseEntity<List<User>> getLecturers() {
        try {
            List<User> lecturers = userRepository.findByRole(UserRole.LEC);
            return ResponseEntity.ok(lecturers);

        } catch (Exception e) {
            log.error("Error fetching lecturers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Helper method to get current user ID from session
     * You may need to adjust this based on your authentication mechanism
     */
    private Long getCurrentUserId(HttpSession session) {
        // This is a placeholder - adjust based on your authentication
        // For testing purposes, we'll use a hardcoded SL user ID
        Object userId = session.getAttribute("userId");
        if (userId != null) {
            return Long.valueOf(userId.toString());
        }

        // For testing - return a Subject Leader ID from sample data
        // In production, this should come from authenticated session
        return 3L; // Brian Carter - Subject Leader from sample data
    }
}

