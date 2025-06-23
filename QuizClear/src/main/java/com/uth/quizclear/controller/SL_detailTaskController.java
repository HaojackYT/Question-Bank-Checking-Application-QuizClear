package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.SL_detailTaskDTO;
import com.uth.quizclear.service.SL_detailTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subject-leader/review-approval/details/api")
public class SL_detailTaskController {

    @Autowired
    private SL_detailTaskService service;

    @GetMapping("/{taskId}")
    public ResponseEntity<SL_detailTaskDTO> getTaskDetails(@PathVariable Integer taskId) {
        SL_detailTaskDTO dto = service.getTaskDetails(taskId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/questions/{questionId}/approve")
    public ResponseEntity<String> approveQuestion(@PathVariable Long questionId, @RequestParam Integer approverId) {
        service.approveQuestion(questionId, approverId);
        return ResponseEntity.ok("Question approved successfully");
    }

    @PostMapping("/questions/{questionId}/decline")
    public ResponseEntity<String> declineQuestion(@PathVariable Long questionId, @RequestBody String reason, @RequestParam Integer commenterId) {
        service.declineQuestion(questionId, reason, commenterId);
        return ResponseEntity.ok("Question declined successfully");
    }

    @PostMapping("/questions/{questionId}/comment")
    public ResponseEntity<String> addComment(@PathVariable Long questionId, @RequestBody String comment, @RequestParam Integer commenterId) {
        service.addComment(questionId, comment, commenterId);
        return ResponseEntity.ok("Comment added successfully");
    }
}