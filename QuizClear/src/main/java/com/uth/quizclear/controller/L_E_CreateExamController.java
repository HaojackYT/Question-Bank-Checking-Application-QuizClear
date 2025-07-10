package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.L_E_CreateExamRequestDTO;
import com.uth.quizclear.model.dto.L_E_CreateExamQuestionDTO;
import com.uth.quizclear.model.dto.L_E_CreateExamQuestionDetailDTO;
import com.uth.quizclear.model.dto.L_E_CreateExamSaveDTO;
import com.uth.quizclear.service.L_E_CreateExamService;
import com.uth.quizclear.service.UserService;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lecturer/api")
public class L_E_CreateExamController {

    private static final Logger logger = LoggerFactory.getLogger(L_E_CreateExamController.class);

    @Autowired
    private L_E_CreateExamService service;
    @Autowired
    private UserService userService;

    @GetMapping("/task-details/{taskId}")
    public ResponseEntity<L_E_CreateExamRequestDTO> getTaskDetails(@PathVariable Long taskId) {
        logger.debug("Request to get task details for taskId: {}", taskId);
        L_E_CreateExamRequestDTO dto = service.getTaskDetails(taskId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        logger.error("Task not found for taskId: {}", taskId);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/task-questions/{taskId}")
    public ResponseEntity<List<L_E_CreateExamQuestionDTO>> getApprovedQuestionsByTaskId(@PathVariable Long taskId) {
        logger.debug("Request to get approved questions for taskId: {}", taskId);
        List<L_E_CreateExamQuestionDTO> questions = service.getApprovedQuestionsByTaskId(taskId);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/clos/{taskId}")
    public ResponseEntity<List<String>> getCloCodesByTaskId(@PathVariable Long taskId) {
        logger.debug("Request to get CLO codes for taskId: {}", taskId);
        List<String> cloCodes = service.getCloCodesByTaskId(taskId);
        return ResponseEntity.ok(cloCodes);
    }

    @PostMapping("/questions-by-ids")
    public ResponseEntity<List<L_E_CreateExamQuestionDetailDTO>> getQuestionsByIds(@RequestBody List<Long> questionIds) {
        logger.debug("Request to get questions by IDs: {}", questionIds);
        List<L_E_CreateExamQuestionDetailDTO> questions = service.getQuestionsByIds(questionIds);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/save-draft")
public ResponseEntity<String> saveDraft(@RequestBody L_E_CreateExamSaveDTO saveDTO) {
    logger.debug("Request to save draft: {}", saveDTO);
    try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getCurrentUserId(authentication);
        Long examId = service.saveDraft(saveDTO, userId);
        return ResponseEntity.ok("Draft saved successfully with examId: " + examId);
    } catch (Exception e) {
        logger.error("Error saving draft: {}", e.getMessage(), e);
        return ResponseEntity.status(500).body("Failed to save draft: " + e.getMessage());
    }
}

@PostMapping("/submit-exam")
public ResponseEntity<String> submitExam(@RequestBody L_E_CreateExamSaveDTO saveDTO) {
    logger.debug("Request to submit exam: {}", saveDTO);
    try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getCurrentUserId(authentication);
        Long examId = service.submitExam(saveDTO, userId);
        return ResponseEntity.ok("Exam submitted successfully with examId: " + examId);
    } catch (Exception e) {
        logger.error("Error submitting exam: {}", e.getMessage(), e);
        return ResponseEntity.status(500).body("Failed to submit exam: " + e.getMessage());
    }
}

}