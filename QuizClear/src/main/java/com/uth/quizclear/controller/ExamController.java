package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.ExamDTO;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.ExamStatus;
import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.service.ExamService;
import com.uth.quizclear.service.PermissionService;
import com.uth.quizclear.service.ScopeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exam Controller with scope-based access control
 * Handles CRUD operations for exams with proper permission checking
 */
@Controller
@RequestMapping("/exams")
public class ExamController {

    private static final Logger logger = LoggerFactory.getLogger(ExamController.class);

    @Autowired
    private ExamService examService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ScopeService scopeService;

    @Autowired
    private UserRepository userRepository;

    /**
     * List exams with scope filtering
     */
    @GetMapping
    public String listExams(
            HttpSession session,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String search) {

        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            // Check general permission to view exams
            if (!permissionService.canCreate(userId, "exam")) { // If user can't create, they likely can't read either
                logger.warn("User {} does not have permission to view exams", userId);
                model.addAttribute("error", "You do not have permission to view exams");
                return "error/403";
            }

            // Get exams with scope filtering
            List<Exam> examsInScope = examService.getExamsForUser(userId);

            // Apply additional filters
            if (status != null && !status.isEmpty()) {
                examsInScope = examsInScope.stream()
                    .filter(e -> e.getStatus().name().equalsIgnoreCase(status))
                    .toList();
            }

            if (search != null && !search.isEmpty()) {
                examsInScope = examsInScope.stream()
                    .filter(e -> e.getExamName().toLowerCase().contains(search.toLowerCase()) ||
                            (e.getDescription() != null && e.getDescription().toLowerCase().contains(search.toLowerCase())))
                    .toList();
            }

            // Manual pagination
            int start = page * size;
            int end = Math.min(start + size, examsInScope.size());
            List<Exam> pageContent = examsInScope.subList(start, end);
            
            model.addAttribute("exams", pageContent);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", (int) Math.ceil((double) examsInScope.size() / size));
            model.addAttribute("totalElements", examsInScope.size());
            model.addAttribute("hasNext", end < examsInScope.size());
            model.addAttribute("hasPrevious", page > 0);

            // Add filter values to model
            model.addAttribute("statusFilter", status);
            model.addAttribute("subjectFilter", subject);
            model.addAttribute("searchFilter", search);

            // Add metadata
            model.addAttribute("canCreate", permissionService.canCreate(userId, "exam"));
            model.addAttribute("availableSubjects", scopeService.getAccessibleSubjects(userId));
            model.addAttribute("examStatuses", ExamStatus.values());

            User currentUser = userRepository.findById(userId).orElse(null);
            model.addAttribute("currentUser", currentUser);

            return "exams/list";

        } catch (Exception e) {
            logger.error("Error loading exams for user {}: {}", userId, e.getMessage(), e);
            model.addAttribute("error", "Unable to load exams");
            return "error/500";
        }
    }

    /**
     * Show exam details with permission check
     */
    @GetMapping("/{id}")
    public String viewExam(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            // Check if user has access to this specific exam
            if (!permissionService.canRead(userId, "exam", id)) {
                logger.warn("User {} attempted to access exam {} outside their scope", userId, id);
                model.addAttribute("error", "You do not have permission to view this exam");
                return "error/403";
            }

            // Get exam from user's scope
            List<Exam> userExams = examService.getExamsForUser(userId);
            Exam exam = userExams.stream()
                .filter(e -> e.getExamId().equals(id))
                .findFirst()
                .orElse(null);

            if (exam == null) {
                model.addAttribute("error", "Exam not found");
                return "error/404";
            }

            model.addAttribute("exam", exam);
            model.addAttribute("canEdit", permissionService.canUpdate(userId, "exam", id));
            model.addAttribute("canDelete", permissionService.canDelete(userId, "exam", id));
            model.addAttribute("canReview", permissionService.canReview(userId, "exam", id));

            User currentUser = userRepository.findById(userId).orElse(null);
            model.addAttribute("currentUser", currentUser);

            return "exams/view";

        } catch (Exception e) {
            logger.error("Error viewing exam {} for user {}: {}", id, userId, e.getMessage(), e);
            model.addAttribute("error", "Unable to load exam");
            return "error/500";
        }
    }

    /**
     * Show create exam form
     */
    @GetMapping("/create")
    public String createExamForm(HttpSession session, Model model) {
        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            // Check creation permission
            if (!permissionService.canCreate(userId, "exam")) {
                model.addAttribute("error", "You do not have permission to create exams");
                return "error/403";
            }

            model.addAttribute("examDTO", new ExamDTO());
            model.addAttribute("availableSubjects", scopeService.getAccessibleSubjects(userId));

            return "exams/create";

        } catch (Exception e) {
            logger.error("Error loading create exam form for user {}: {}", userId, e.getMessage(), e);
            model.addAttribute("error", "Unable to load form");
            return "error/500";
        }
    }

    /**
     * Process exam creation
     */
    @PostMapping
    public String createExam(
            @Valid @ModelAttribute ExamDTO examDTO,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            // Check creation permission
            if (!permissionService.canCreate(userId, "exam")) {
                model.addAttribute("error", "You do not have permission to create exams");
                return "error/403";
            }

            if (bindingResult.hasErrors()) {
                model.addAttribute("availableSubjects", scopeService.getAccessibleSubjects(userId));
                return "exams/create";
            }

            // Validate scope access to selected subject
            if (examDTO.getSubjectId() != null) {
                if (!permissionService.canAccess(userId, "subject", examDTO.getSubjectId())) {
                    bindingResult.rejectValue("subjectId", "error.subject", "You do not have access to this subject");
                    model.addAttribute("availableSubjects", scopeService.getAccessibleSubjects(userId));
                    return "exams/create";
                }
            }

            // Create exam using available methods
            Exam createdExam = examService.createExamFromDTO(examDTO, userId);
            
            logger.info("Exam {} created by user {}", createdExam.getExamId(), userId);
            redirectAttributes.addFlashAttribute("success", "Exam created successfully");
            return "redirect:/exams/" + createdExam.getExamId();

        } catch (Exception e) {
            logger.error("Error creating exam for user {}: {}", userId, e.getMessage(), e);
            model.addAttribute("error", "Unable to create exam: " + e.getMessage());
            model.addAttribute("availableSubjects", scopeService.getAccessibleSubjects(userId));
            return "exams/create";
        }
    }

    /**
     * Show edit exam form
     */
    @GetMapping("/{id}/edit")
    public String editExamForm(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            // Check update permission for specific exam
            if (!permissionService.canUpdate(userId, "exam", id)) {
                model.addAttribute("error", "You do not have permission to edit this exam");
                return "error/403";
            }

            // Get exam from user's scope
            List<Exam> userExams = examService.getExamsForUser(userId);
            Exam exam = userExams.stream()
                .filter(e -> e.getExamId().equals(id))
                .findFirst()
                .orElse(null);

            if (exam == null) {
                return "error/404";
            }

            // Convert to DTO
            ExamDTO examDTO = convertToDTO(exam);
            
            model.addAttribute("examDTO", examDTO);
            model.addAttribute("exam", exam);
            model.addAttribute("availableSubjects", scopeService.getAccessibleSubjects(userId));

            return "exams/edit";

        } catch (Exception e) {
            logger.error("Error loading edit form for exam {} and user {}: {}", id, userId, e.getMessage(), e);
            model.addAttribute("error", "Unable to load exam for editing");
            return "error/500";
        }
    }

    /**
     * Process exam update
     */
    @PostMapping("/{id}")
    public String updateExam(
            @PathVariable Long id,
            @Valid @ModelAttribute ExamDTO examDTO,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            // Check update permission
            if (!permissionService.canUpdate(userId, "exam", id)) {
                model.addAttribute("error", "You do not have permission to edit this exam");
                return "error/403";
            }

            if (bindingResult.hasErrors()) {
                // Get exam from user's scope
                List<Exam> userExams = examService.getExamsForUser(userId);
                Exam exam = userExams.stream()
                    .filter(e -> e.getExamId().equals(id))
                    .findFirst()
                    .orElse(null);
                
                model.addAttribute("exam", exam);
                model.addAttribute("availableSubjects", scopeService.getAccessibleSubjects(userId));
                return "exams/edit";
            }

            // Validate scope access to new subject if changed
            if (examDTO.getSubjectId() != null) {
                if (!permissionService.canAccess(userId, "subject", examDTO.getSubjectId())) {
                    bindingResult.rejectValue("subjectId", "error.subject", "You do not have access to this subject");
                    
                    List<Exam> userExams = examService.getExamsForUser(userId);
                    Exam exam = userExams.stream()
                        .filter(e -> e.getExamId().equals(id))
                        .findFirst()
                        .orElse(null);
                    
                    model.addAttribute("exam", exam);
                    model.addAttribute("availableSubjects", scopeService.getAccessibleSubjects(userId));
                    return "exams/edit";
                }
            }

            Exam updatedExam = examService.updateExamFromDTO(id, examDTO, userId);
            
            logger.info("Exam {} updated by user {}", id, userId);
            redirectAttributes.addFlashAttribute("success", "Exam updated successfully");
            return "redirect:/exams/" + id;

        } catch (Exception e) {
            logger.error("Error updating exam {} for user {}: {}", id, userId, e.getMessage(), e);
            model.addAttribute("error", "Unable to update exam: " + e.getMessage());
            
            // Get exam from user's scope for error display
            List<Exam> userExams = examService.getExamsForUser(userId);
            Exam exam = userExams.stream()
                .filter(examItem -> examItem.getExamId().equals(id))
                .findFirst()
                .orElse(null);
            
            model.addAttribute("exam", exam);
            model.addAttribute("availableSubjects", scopeService.getAccessibleSubjects(userId));
            return "exams/edit";
        }
    }

    /**
     * Delete exam
     */
    @PostMapping("/{id}/delete")
    public String deleteExam(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            // Check delete permission
            if (!permissionService.canDelete(userId, "exam", id)) {
                redirectAttributes.addFlashAttribute("error", "You do not have permission to delete this exam");
                return "redirect:/exams/" + id;
            }

            examService.deleteExamById(id, userId);
            
            logger.info("Exam {} deleted by user {}", id, userId);
            redirectAttributes.addFlashAttribute("success", "Exam deleted successfully");
            return "redirect:/exams";

        } catch (Exception e) {
            logger.error("Error deleting exam {} for user {}: {}", id, userId, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Unable to delete exam: " + e.getMessage());
            return "redirect:/exams/" + id;
        }
    }

    /**
     * Submit exam for review
     */
    @PostMapping("/{id}/submit")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitExam(@PathVariable Long id, HttpSession session) {
        Long userId = getCurrentUserId(session);
        Map<String, Object> response = new HashMap<>();

        if (userId == null) {
            response.put("success", false);
            response.put("message", "Not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            // Check if user can submit this exam
            if (!permissionService.canUpdate(userId, "exam", id)) {
                response.put("success", false);
                response.put("message", "You do not have permission to submit this exam");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            examService.submitExamForReview(id, userId);
            
            logger.info("Exam {} submitted for review by user {}", id, userId);
            response.put("success", true);
            response.put("message", "Exam submitted for review successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error submitting exam {} for user {}: {}", id, userId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Unable to submit exam: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Review exam (approve/reject)
     */
    @PostMapping("/{id}/review")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> reviewExam(
            @PathVariable Long id,
            @RequestParam String action,
            @RequestParam(required = false) String comments,
            HttpSession session) {

        Long userId = getCurrentUserId(session);
        Map<String, Object> response = new HashMap<>();

        if (userId == null) {
            response.put("success", false);
            response.put("message", "Not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            // Check review permission
            if (!permissionService.canReview(userId, "exam", id)) {
                response.put("success", false);
                response.put("message", "You do not have permission to review this exam");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            if ("approve".equals(action)) {
                examService.approveExam(id, userId);
                logger.info("Exam {} approved by user {}", id, userId);
                response.put("message", "Exam approved successfully");
            } else if ("reject".equals(action)) {
                // For rejection, we need feedback - using default for now
                examService.rejectExam(id, userId, "Rejected via API");
                logger.info("Exam {} rejected by user {}", id, userId);
                response.put("message", "Exam rejected successfully");
            } else {
                response.put("success", false);
                response.put("message", "Invalid action");
                return ResponseEntity.badRequest().body(response);
            }

            response.put("success", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error reviewing exam {} for user {}: {}", id, userId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Unable to review exam: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Assign exam to lecturers for question creation
     */
    @PostMapping("/{id}/assign")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> assignExam(
            @PathVariable Long id,
            @RequestParam List<Long> lecturerIds,
            @RequestParam(required = false) String assignmentNote,
            HttpSession session) {

        Long userId = getCurrentUserId(session);
        Map<String, Object> response = new HashMap<>();

        if (userId == null) {
            response.put("success", false);
            response.put("message", "Not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            // Check assignment permission
            if (!permissionService.canAssign(userId, "exam", id)) {
                response.put("success", false);
                response.put("message", "You do not have permission to assign this exam");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            examService.assignExamToLecturers(id, lecturerIds, userId);
            
            logger.info("Exam {} assigned to {} lecturers by user {}", id, lecturerIds.size(), userId);
            response.put("success", true);
            response.put("message", "Exam assigned successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error assigning exam {} for user {}: {}", id, userId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Unable to assign exam: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Helper methods

    private Long getCurrentUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }

    private ExamDTO convertToDTO(Exam exam) {
        ExamDTO dto = new ExamDTO();
        dto.setExamId(exam.getExamId());
        dto.setExamTitle(exam.getExamName());
        dto.setDescription(exam.getDescription());
        dto.setDuration(exam.getDuration());
        dto.setStatus(exam.getStatus() != null ? exam.getStatus().getValue() : "");
        dto.setCreatedAt(exam.getCreatedAt());
        dto.setDueDate(exam.getExamDate());
        dto.setCreatedBy(exam.getCreatorName());
        
        if (exam.getSubject() != null) {
            dto.setSubjectId(exam.getSubject().getSubjectId());
            dto.setSubject(exam.getSubject().getSubjectName());
        }
        
        return dto;
    }
}
