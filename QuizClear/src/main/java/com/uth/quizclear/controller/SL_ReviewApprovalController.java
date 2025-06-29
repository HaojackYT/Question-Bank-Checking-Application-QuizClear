package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.SL_ReviewApprovalTaskDTO;
import com.uth.quizclear.service.SL_ReviewApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject-leader/review-approval")
public class SL_ReviewApprovalController {

    @Autowired
    private SL_ReviewApprovalService service;

    @GetMapping("/tasks")
    public ResponseEntity<List<SL_ReviewApprovalTaskDTO>> getTasks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter) {
        List<SL_ReviewApprovalTaskDTO> tasks = service.getTasks(search, filter);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/tasks/{taskId}/approve")
    public ResponseEntity<String> approveTask(@PathVariable Integer taskId) {
        service.approveTask(taskId);
        return ResponseEntity.ok("Task approved successfully");
    }

    @PostMapping("/tasks/{taskId}/decline")
    public ResponseEntity<String> declineTask(@PathVariable Integer taskId, @RequestBody String declineReason) {
        service.declineTask(taskId, declineReason);
        return ResponseEntity.ok("Task declined successfully");
    }
}

