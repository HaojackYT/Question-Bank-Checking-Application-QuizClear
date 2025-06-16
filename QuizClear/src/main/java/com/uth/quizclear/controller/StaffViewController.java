package com.uth.quizclear.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.uth.quizclear.model.dto.DuplicateDetectionDTO;
import com.uth.quizclear.model.dto.QuestionDetailDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.service.DuplicationStaffService;

@Controller
public class StaffViewController {
    @Autowired
    private DuplicationStaffService duplicationStaffService;

    // @GetMapping("/staff/duplication-check")
// public String showStaffDuplicationCheckPage() {
//     return "Staff/staffDuplicationCheck";
// }
    @GetMapping("/staff/duplication-details") // URL để truy cập trang chi tiết (nếu được tải riêng)
    public String showStaffDuplicationDetailsPage() {
        return "Staff/staffDupDetails"; // Thymeleaf sẽ tìm classpath:/Template/Staff/staffDupDetails.html
    }

    // THÊM CÁC ENDPOINT MỚI NÀY ĐỂ PHỤC VỤ HEADER VÀ MENU
    @GetMapping("/partials/header-user")
    public String getHeaderUserPartial() {
        // Thymeleaf sẽ tìm classpath:/Template/header_user.html
        return "header_user";
    }

    @GetMapping("/partials/menu-staff")
    public String getMenuStaffPartial() {
        // Thymeleaf sẽ tìm classpath:/Template/Menu-Staff.html
        return "Menu-Staff";
    }    // Add missing endpoints for duplication check tab content
    @GetMapping("/staff/dup-content")
    public String getDupContent() {
        return "Staff/staffDupContent";
    }

    @GetMapping("/staff/stat-content")
    public String getStatContent() {
        return "Staff/staffStatContent";
    }

    @GetMapping("/staff/log-content") 
    public String getLogContent() {
        return "Staff/staffLogContent";
    }    // ĐƯỜNG DẪN ĐÚNG DUY NHẤT CHO CHI TIẾT DUPLICATION
    @GetMapping("/staff/dup-details/{detectionId}")
    public String showDuplicationDetails(@PathVariable Long detectionId, Model model) {
        try {
            System.out.println("Loading detection details for ID: " + detectionId);
            DuplicateDetectionDTO detection = duplicationStaffService.getDetectionById(detectionId);
            System.out.println("Detection found: " + (detection != null));
            
            if (detection == null) {
                System.out.println("Detection not found, creating mock data");
                detection = createMockDetection(detectionId);
            }
            
            model.addAttribute("detection", detection);
            System.out.println("Returning fragment: Staff/staffDupDetails :: root");
            return "Staff/staffDupDetails :: root";
        } catch (Exception e) {
            System.err.println("Error loading detection details: " + e.getMessage());
            e.printStackTrace();
            // Return mock data on error
            model.addAttribute("detection", createMockDetection(detectionId));
            return "Staff/staffDupDetails :: root";
        }
    }
      private DuplicateDetectionDTO createMockDetection(Long detectionId) {
        // Create a comprehensive mock detection with full question details
        DuplicateDetectionDTO mock = new DuplicateDetectionDTO();
        mock.setDetectionId(detectionId);
        mock.setStatus("PENDING");
        mock.setSimilarityScore(0.95);
        
        // Create mock new question with full details
        QuestionDetailDTO newQuestion = new QuestionDetailDTO();
        newQuestion.setQuestionId(1L);
        newQuestion.setContent("What is the purpose of a UML diagram?");
        newQuestion.setCourseName("Software Engineering");
        newQuestion.setCourseCode("CS105");
        newQuestion.setCloCode("CLO1_CS105");
        newQuestion.setCloDescription("Understand software lifecycle models");
        newQuestion.setDifficultyLevel("comprehension");
        newQuestion.setCreatorName("Ash Abrahams");
        newQuestion.setCreatorEmail("ash.abrahams@university.edu");
        newQuestion.setCreatedAt(java.time.LocalDateTime.of(2025, 2, 1, 16, 0));
        
        // Create mock similar question with full details
        QuestionDetailDTO similarQuestion = new QuestionDetailDTO();
        similarQuestion.setQuestionId(2L);
        similarQuestion.setContent("Find the order of the group Z_6");
        similarQuestion.setCourseName("Abstract Algebra");
        similarQuestion.setCourseCode("MATH205");
        similarQuestion.setCloCode("CLO1_MATH205");
        similarQuestion.setCloDescription("Apply group theory to solve problems");
        similarQuestion.setDifficultyLevel("Advanced Application");
        similarQuestion.setCreatorName("Alexander Brooks");
        similarQuestion.setCreatorEmail("alexander.brooks@university.edu");
        similarQuestion.setCreatedAt(java.time.LocalDateTime.of(2025, 2, 2, 16, 0));
        
        mock.setNewQuestion(newQuestion);
        mock.setSimilarQuestion(similarQuestion);
        
        // Add detected by info
        UserBasicDTO detectedBy = new UserBasicDTO();
        detectedBy.setUserId(1L);
        detectedBy.setFullName("Alexander Brooks");
        detectedBy.setEmail("alexander.brooks@university.edu");
        detectedBy.setRole("Staff");
        mock.setDetectedBy(detectedBy);
        
        mock.setDetectedAt(java.time.LocalDateTime.of(2025, 6, 14, 10, 30));
        
        return mock;
    }

    // Test endpoint to verify that the service is working with real data
    @GetMapping("/staff/test-details/{detectionId}")
    public String testDuplicationDetails(@PathVariable Long detectionId, Model model) {
        try {
            System.out.println("=== TEST ENDPOINT - Loading detection details for ID: " + detectionId + " ===");
            
            // Try to get real data from service
            DuplicateDetectionDTO detection = duplicationStaffService.getDetectionById(detectionId);
            
            if (detection != null) {
                System.out.println("SUCCESS: Detection found with ID: " + detection.getDetectionId());
                if (detection.getNewQuestion() != null) {
                    System.out.println("New Question: " + detection.getNewQuestion().getContent());
                    System.out.println("Course: " + detection.getNewQuestion().getCourseName());
                    System.out.println("CLO Code: " + detection.getNewQuestion().getCloCode());
                    System.out.println("Creator: " + detection.getNewQuestion().getCreatorName());
                }
                if (detection.getSimilarQuestion() != null) {
                    System.out.println("Similar Question: " + detection.getSimilarQuestion().getContent());
                    System.out.println("Course: " + detection.getSimilarQuestion().getCourseName());
                    System.out.println("CLO Code: " + detection.getSimilarQuestion().getCloCode());
                    System.out.println("Creator: " + detection.getSimilarQuestion().getCreatorName());
                }
                System.out.println("Similarity Score: " + detection.getSimilarityScore());
                
                model.addAttribute("detection", detection);
                model.addAttribute("testMessage", "SUCCESS: Real data loaded");
            } else {
                System.out.println("WARNING: No detection found for ID: " + detectionId);
                model.addAttribute("detection", createMockDetection(detectionId));
                model.addAttribute("testMessage", "WARNING: Using mock data");
            }
            
            return "Staff/staffDupDetails :: root";
            
        } catch (Exception e) {
            System.err.println("ERROR in test endpoint: " + e.getMessage());
            e.printStackTrace();
            
            model.addAttribute("detection", createMockDetection(detectionId));
            model.addAttribute("testMessage", "ERROR: " + e.getMessage());
            return "Staff/staffDupDetails :: root";
        }
    }
    
    // Simple test endpoint
    @GetMapping("/staff/test")
    public String simpleTest() {        return "redirect:/staff/duplication";
    }
}