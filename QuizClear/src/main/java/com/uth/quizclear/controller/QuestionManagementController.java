package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.QuestionBankDTO;
import com.uth.quizclear.model.dto.CreatePlanDTO;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.CLO;
import com.uth.quizclear.model.entity.Plan;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.service.QuestionPlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/staff") // Tất cả các URL trong controller này sẽ bắt đầu bằng /staff
public class QuestionManagementController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionPlanningService questionPlanningService;

    // Đây là điểm vào chính cho "Question Management" từ menu.
    // Nó sẽ xử lý GET /staff/question-management
    @GetMapping("/question-management")
    public String showQuestionManagementHome(Model model) {
        // Mặc định sẽ hiển thị trang Question Bank với dữ liệu
        return loadQuestionBankData(model, "", "", "", "", 0, 10);
    }

    // Các trang con khác sẽ nằm dưới /staff/questions/
    // Ví dụ: GET /staff/questions/bank
    @GetMapping("/questions/bank")
    public String showQuestionBank(Model model,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String subject,
            @RequestParam(defaultValue = "") String clo,
            @RequestParam(defaultValue = "") String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return loadQuestionBankData(model, search, subject, clo, difficulty, page, size);
    }

    private String loadQuestionBankData(Model model, String search, String subject,
            String clo, String difficulty, int page, int size) {
        try {
            // Create pageable with sorting by creation date (newest first)
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

            // Apply filtering and search
            Page<Question> questionPage = getFilteredQuestions(search, subject, clo, difficulty, pageable);

            // Convert to DTOs
            List<QuestionBankDTO> questionDTOs = questionPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            // Add data to model
            model.addAttribute("questions", questionDTOs);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", questionPage.getTotalPages());
            model.addAttribute("totalElements", questionPage.getTotalElements());
            model.addAttribute("search", search);
            model.addAttribute("subject", subject);
            model.addAttribute("clo", clo);
            model.addAttribute("difficulty", difficulty);

            // Get unique values for filter dropdowns (from all questions, not filtered)
            List<String> subjects = questionRepository.findAll().stream()
                    .map(q -> q.getCourse().getCourseName())
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            List<String> clos = questionRepository.findAll().stream()
                    .map(q -> q.getClo().getCloCode())
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            List<String> difficulties = questionRepository.findAll().stream()
                    .map(q -> q.getDifficultyLevel().toString())
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            List<String> creators = questionRepository.findAll().stream()
                    .map(q -> q.getCreatedBy().getFullName())
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            model.addAttribute("subjectOptions", subjects);
            model.addAttribute("cloOptions", clos);
            model.addAttribute("difficultyOptions", difficulties);
            model.addAttribute("creatorOptions", creators);

            System.out.println("Loaded " + questionDTOs.size() + " questions for Question Bank (page " + (page + 1)
                    + "/" + questionPage.getTotalPages() + ")");

        } catch (Exception e) {
            System.err.println("Error loading question bank data: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("questions", List.of());
            model.addAttribute("error", "Error loading questions: " + e.getMessage());
        }

        return "Staff/staffQMQuestionBank"; // Tên của file HTML trong src/main/resources/templates/Staff/
    }

    private Page<Question> getFilteredQuestions(String search, String subject, String clo, String difficulty,
            Pageable pageable) {
        // If no filters applied, return all questions
        if ((search == null || search.trim().isEmpty()) &&
                (subject == null || subject.trim().isEmpty()) &&
                (clo == null || clo.trim().isEmpty()) &&
                (difficulty == null || difficulty.trim().isEmpty())) {
            return questionRepository.findAll(pageable);
        }

        // Apply filters step by step
        List<Question> allQuestions = questionRepository.findAll();
        List<Question> filteredQuestions = allQuestions.stream()
                .filter(question -> {
                    // Search filter (search in content)
                    if (search != null && !search.trim().isEmpty()) {
                        String searchLower = search.toLowerCase();
                        if (!question.getContent().toLowerCase().contains(searchLower)) {
                            return false;
                        }
                    }

                    // Subject filter
                    if (subject != null && !subject.trim().isEmpty()) {
                        if (!question.getCourse().getCourseName().equals(subject)) {
                            return false;
                        }
                    }

                    // CLO filter
                    if (clo != null && !clo.trim().isEmpty()) {
                        if (!question.getClo().getCloCode().equals(clo)) {
                            return false;
                        }
                    }

                    // Difficulty filter
                    if (difficulty != null && !difficulty.trim().isEmpty()) {
                        if (!question.getDifficultyLevel().toString().equals(difficulty)) {
                            return false;
                        }
                    }

                    return true;
                })
                .sorted((q1, q2) -> q2.getCreatedAt().compareTo(q1.getCreatedAt())) // Sort by newest first
                .collect(Collectors.toList());

        // Manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredQuestions.size());

        List<Question> pageContent = filteredQuestions.subList(start, end);

        return new org.springframework.data.domain.PageImpl<>(
                pageContent,
                pageable,
                filteredQuestions.size());
    }

    private QuestionBankDTO convertToDTO(Question question) {
        return new QuestionBankDTO(
                question.getQuestionId(),
                question.getContent(),
                question.getCourse() != null ? question.getCourse().getCourseName() : "N/A",
                question.getClo() != null ? question.getClo().getCloCode() : "N/A",
                question.getDifficultyLevel() != null ? question.getDifficultyLevel().toString() : "N/A",
                question.getCreatedBy() != null ? question.getCreatedBy().getFullName() : "N/A",
                question.getCreatedAt(),
                question.getStatus() != null ? question.getStatus().toString() : "N/A");
    }

    // API endpoint for AJAX requests to get question details
    @GetMapping("/api/questions/details")
    @ResponseBody
    public QuestionBankDTO getQuestionDetails(@RequestParam Long questionId) {
        try {
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));
            return convertToDTO(question);
        } catch (Exception e) {
            System.err.println("Error getting question details: " + e.getMessage());
            return null;
        }
    }

    // Trang Submission Table
    // Xử lý GET /staff/questions/submission-table
    @GetMapping("/questions/submission-table")
    public String showSubmissionTable(Model model) {
        // Logic để tải dữ liệu Submission Table nếu có
        return "Staff/staffSubmissionTable"; // Tên của file HTML trong src/main/resources/templates/Staff/
    }

    // Trang Question Planning
    // Xử lý GET /staff/questions/planning
    @GetMapping("/questions/planning")
    public String showQuestionPlanning(Model model) {
        // Rót dữ liệu cho form Create New Plan - chỉ lấy dữ liệu có HoD
        model.addAttribute("courses", questionPlanningService.getAllCourses()); // Chỉ course có dept HoD
        model.addAttribute("departments", questionPlanningService.getAllDepartments()); // Chỉ dept có HoD
        model.addAttribute("users", questionPlanningService.getAllHoDUsers()); // Tất cả HoD users
        model.addAttribute("plans", questionPlanningService.getAllPlans());

        return "Staff/staffQMQuestionPlanning";
    }

    /**
     * API để lấy users theo department cho dropdown Assign To
     */
    @GetMapping("/api/users-by-department")
    @ResponseBody
    public List<User> getUsersByDepartment(@RequestParam String departmentName) {
        return questionPlanningService.getUsersByDepartment(departmentName);
    }

    /**
     * API để lấy CLOs theo course và difficulty level
     */
    @GetMapping("/api/clos-by-course-difficulty")
    @ResponseBody
    public List<CLO> getClosByCourseAndDifficulty(@RequestParam Long courseId,
            @RequestParam String difficultyLevel) {
        return questionPlanningService.getClosByCourseAndDifficulty(courseId, difficultyLevel);
    }

    /**
     * API để lấy tất cả CLOs theo course
     */
    @GetMapping("/api/clos-by-course")
    @ResponseBody
    public List<CLO> getClosByCourse(@RequestParam Long courseId) {
        return questionPlanningService.getClosByCourse(courseId);
    }

    /**
     * API để tạo plan mới
     */
    @PostMapping("/api/create-plan")
    @ResponseBody
    public ResponseEntity<?> createPlan(@RequestBody CreatePlanDTO createPlanDTO) {
        try {
            questionPlanningService.createPlanAndTask(createPlanDTO);
            return ResponseEntity.ok().body(Map.of("success", true, "message", "Plan created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * API để lấy thống kê planning
     */
    @GetMapping("/api/planning-statistics")
    @ResponseBody
    public Map<String, Object> getPlanningStatistics() {
        return questionPlanningService.getPlanningStatistics();
    }

    /**
     * API để lấy chi tiết plan theo ID
     */
    @GetMapping("/api/plan-details/{planId}")
    @ResponseBody
    public ResponseEntity<?> getPlanDetails(@PathVariable Long planId) {
        try {
            Plan plan = questionPlanningService.getPlanById(planId);

            // Calculate actual progress based on approved questions by the HoD who assigned
            // the plan
            int totalQuestions = plan.getTotalQuestions() != null ? plan.getTotalQuestions() : 0;
            long createdQuestions = questionRepository.countByPlanId(planId);

            // Count approved questions by the HoD who created the plan (assigned_by)
            long approvedQuestions = 0;
            if (plan.getAssignedByUser() != null) {
                approvedQuestions = questionRepository.countByPlanIdAndStatusAndApprovedBy(
                        planId,
                        QuestionStatus.APPROVED,
                        plan.getAssignedByUser().getUserId().longValue());
            }

            int progressPercentage = totalQuestions > 0
                    ? (int) Math.round((double) approvedQuestions / totalQuestions * 100)
                    : 0;

            // Ensure progress doesn't exceed 100%
            progressPercentage = Math.min(progressPercentage, 100);

            // Create a simple DTO to avoid serialization issues with lazy loading
            Map<String, Object> planDetails = new HashMap<>();
            planDetails.put("planId", plan.getPlanId());
            planDetails.put("planTitle", plan.getPlanTitle() != null ? plan.getPlanTitle() : "");
            planDetails.put("planDescription", plan.getPlanDescription() != null ? plan.getPlanDescription() : "");
            planDetails.put("totalQuestions", totalQuestions);
            planDetails.put("createdQuestions", createdQuestions);
            planDetails.put("approvedCount", approvedQuestions);
            planDetails.put("remainingQuestions", Math.max(0, totalQuestions - approvedQuestions));
            planDetails.put("progressPercentage", progressPercentage);
            planDetails.put("totalRecognition", plan.getTotalRecognition() != null ? plan.getTotalRecognition() : 0);
            planDetails.put("totalComprehension",
                    plan.getTotalComprehension() != null ? plan.getTotalComprehension() : 0);
            planDetails.put("totalBasicApplication",
                    plan.getTotalBasicApplication() != null ? plan.getTotalBasicApplication() : 0);
            planDetails.put("totalAdvancedApplication",
                    plan.getTotalAdvancedApplication() != null ? plan.getTotalAdvancedApplication() : 0);
            planDetails.put("status", plan.getStatus() != null ? plan.getStatus().toString() : "NEW");
            planDetails.put("dueDate", plan.getDueDate() != null ? plan.getDueDate().toString() : "");

            // Handle course information
            Map<String, Object> courseInfo = new HashMap<>();
            if (plan.getCourse() != null) {
                courseInfo.put("courseName",
                        plan.getCourse().getCourseName() != null ? plan.getCourse().getCourseName() : "");
                courseInfo.put("department",
                        plan.getCourse().getDepartment() != null ? plan.getCourse().getDepartment() : "");
            } else {
                courseInfo.put("courseName", "");
                courseInfo.put("department", "");
            }
            planDetails.put("course", courseInfo);

            // Handle assigned user information
            Map<String, Object> assignedUserInfo = new HashMap<>();
            if (plan.getAssignedToUser() != null) {
                assignedUserInfo.put("fullName",
                        plan.getAssignedToUser().getFullName() != null ? plan.getAssignedToUser().getFullName() : "");
            } else {
                assignedUserInfo.put("fullName", "");
            }
            planDetails.put("assignedToUser", assignedUserInfo);

            return ResponseEntity.ok(planDetails);
        } catch (Exception e) {
            System.err.println("Error loading plan details: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Failed to load plan details: " + e.getMessage()));
        }
    }

    // Trang Duplication Check (của phần Question Management)
    // Xử lý GET /staff/questions/duplication-check
    @GetMapping("/questions/duplication-check")
    public String showDuplicationCheck(Model model) {
        // Có thể thêm logic cho Duplication Check nếu có
        return "Staff/staffQMDupliCheck"; // Giả định tên file, tránh trùng với /staff/duplications
    }

    // Trang Reports & Statistics
    // Xử lý GET /staff/questions/reports-statistics
    @GetMapping("/questions/reports-statistics")
    public String showReportsStatistics(Model model) {
        return "Staff/staffQMReportsStatistics"; // Giả định tên file:
                                                 // src/main/resources/templates/Staff/staffReportsStatistics.html
    }
}