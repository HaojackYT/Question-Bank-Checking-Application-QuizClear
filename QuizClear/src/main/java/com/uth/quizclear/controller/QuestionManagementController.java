// package com.uth.quizclear.controller;

// import com.uth.quizclear.model.dto.QuestionDTO;
// import com.uth.quizclear.service.QuestionService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import java.util.List;

// // Import nếu bạn dùng Spring Security để lấy User ID
// // import org.springframework.security.core.Authentication;
// // import org.springframework.security.core.context.SecurityContextHolder;
// // import com.uth.quizclear.security.UserPrincipal; // Nếu có custom UserPrincipal

// @Controller
// @RequestMapping("/staff") // Tất cả các đường dẫn trong Controller này sẽ bắt đầu bằng /staff
// public class QuestionManagementController {

//     private static final Logger logger = LoggerFactory.getLogger(QuestionManagementController.class);

//     @Autowired
//     private QuestionService questionService;

//     // =========================================================================================
//     // ENDPOINTS CHO TỪNG TAB (MỖI TAB LÀ MỘT URL VÀ TẢI LẠI TRANG)
//     // =========================================================================================

//     // Endpoint cho Tab "Question Bank" (Đây là tab mặc định khi vào Question Management)
//     @GetMapping("/question-management") // Đường dẫn đầy đủ sẽ là /staff/question-management
//     public String showQuestionBankPage(
//         Model model,
//         @RequestParam(value = "searchTerm", required = false) String searchTerm,
//         @RequestParam(value = "subject", required = false) String subject,
//         @RequestParam(value = "clo", required = false) String clo,
//         @RequestParam(value = "difficulty", required = false) String difficulty,
//         @RequestParam(value = "creator", required = false) String creator,
//         @RequestParam(value = "page", defaultValue = "1") int page,
//         @RequestParam(value = "size", defaultValue = "10") int size
//     ) {
//         try {
//             Long currentUserId = 1L; // TODO: Lấy ID người dùng thực tế từ SecurityContext

//             Page<QuestionDTO> questionsPage = questionService.getFilteredQuestions(
//                 currentUserId, searchTerm, subject, clo, difficulty, creator, page, size
//             );

//             model.addAttribute("questions", questionsPage.getContent());
//             model.addAttribute("currentPage", questionsPage.getNumber() + 1);
//             model.addAttribute("totalPages", questionsPage.getTotalPages());
//             model.addAttribute("totalItems", questionsPage.getTotalElements());

//             model.addAttribute("searchTerm", searchTerm);
//             model.addAttribute("selectedSubject", subject);
//             model.addAttribute("selectedClo", clo);
//             model.addAttribute("selectedDifficulty", difficulty);
//             model.addAttribute("selectedCreator", creator);

//             model.addAttribute("allSubjects", questionService.getAllSubjectsForFilter());
//             model.addAttribute("allClos", questionService.getAllClosForFilter());
//             model.addAttribute("allDifficultyLevels", questionService.getAllDifficultyLevelsForFilter());
//             model.addAttribute("allCreators", questionService.getAllCreatorsForFilter());

//             // Đánh dấu tab "Question Bank" là active
//             model.addAttribute("activeTab", "question-bank");

//             // Trả về tên file HTML cho trang Question Bank
//             return "Staff/staffQMQuestionBank";
//         } catch (Exception e) {
//             logger.error("Error loading question bank page for user {}: {}", currentUserId, e.getMessage(), e);
//             model.addAttribute("error", "Error loading question bank: " + e.getMessage());
//             return "Staff/staffQMQuestionBank";
//         }
//     }

//     // Endpoint cho Tab "Submission Table"
//     @GetMapping("/question-management/submission-table")
//     public String showSubmissionTablePage(Model model) {
//         // TODO: Logic để lấy dữ liệu cho Submission Table
//         // model.addAttribute("submissions", submissionService.getLatestSubmissions());

//         // Đánh dấu tab "Submission Table" là active
//         model.addAttribute("activeTab", "submission-table");
        
//         // Trả về tên file HTML cho trang Submission Table
//         // Bạn sẽ cần tạo file này và nội dung của nó
//         return "Staff/staffQMSubmissionTable";
//     }

//     // Endpoint cho Tab "Question Planning"
//     @GetMapping("/question-management/question-planning")
//     public String showQuestionPlanningPage(Model model) {
//         // TODO: Logic để lấy dữ liệu cho Question Planning
//         // Đánh dấu tab "Question Planning" là active
//         model.addAttribute("activeTab", "question-planning");
//         return "Staff/staffQMQuestionPlanning";
//     }

//     // Endpoint cho Tab "Duplication Check"
//     @GetMapping("/question-management/duplication-check")
//     public String showDuplicationCheckPage(Model model) {
//         // TODO: Logic để lấy dữ liệu cho Duplication Check
//         // Đánh dấu tab "Duplication Check" là active
//         model.addAttribute("activeTab", "duplication-check");
//         return "Staff/staffQMDuplicationCheck";
//     }

//     // Endpoint cho Tab "Reports & Statistics"
//     @GetMapping("/question-management/reports-statistics")
//     public String showReportsStatisticsPage(Model model) {
//         // TODO: Logic để lấy dữ liệu cho Reports & Statistics
//         // Đánh dấu tab "Reports & Statistics" là active
//         model.addAttribute("activeTab", "reports-statistics");
//         return "Staff/staffQMReportsStatistics";
//     }
// }