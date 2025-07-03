package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.DuplicateDetectionDTO;
import com.uth.quizclear.model.dto.SubjectOptionDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.dto.ProcessingLogDTO;
import com.uth.quizclear.service.DuplicationStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff/duplications")
public class DuplicationStaffController {

    @Autowired
    private DuplicationStaffService service;

    // Get all duplicate detections
    @GetMapping
    public ResponseEntity<?> getAllDetections() {
        try {
            List<DuplicateDetectionDTO> detections = service.getAllDetections();
            return ResponseEntity.ok(detections);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("type", e.getClass().getSimpleName());
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Get detections by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DuplicateDetectionDTO>> getDetectionsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getDetectionsByStatus(status));
    }

    // Get detections by submitter
    @GetMapping("/submitter/{submitterId}")
    public ResponseEntity<List<DuplicateDetectionDTO>> getDetectionsBySubmitter(@PathVariable Long submitterId) {
        return ResponseEntity.ok(service.getDetectionsBySubmitter(submitterId));
    }

    // Get detections by subject
    @GetMapping("/subject/{courseId}")
    public ResponseEntity<List<DuplicateDetectionDTO>> getDetectionsBySubject(@PathVariable Long courseId) {
        return ResponseEntity.ok(service.getDetectionsBySubject(courseId));
    }

    // Get subject filter options
    @GetMapping("/subject-options")
    public ResponseEntity<List<SubjectOptionDTO>> getSubjectOptions() {
        return ResponseEntity.ok(service.getSubjectOptions());
    }

    // Get submitter filter options  
    @GetMapping("/submitter-options")
    public ResponseEntity<List<UserBasicDTO>> getSubmitterOptions() {
        return ResponseEntity.ok(service.getSubmitterOptions());
    }

    // Get detection by ID
    @GetMapping("/{detectionId}")
    public ResponseEntity<DuplicateDetectionDTO> getDetectionById(@PathVariable Long detectionId) {
        try {
            DuplicateDetectionDTO detection = service.getDetectionById(detectionId);
            if (detection != null) {
                return ResponseEntity.ok(detection);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }    // Get filter data (subjects and submitters)
    @GetMapping("/filters")
    public ResponseEntity<Map<String, Object>> getFilterData() {
        try {
            Map<String, Object> filterData = new HashMap<>();
            
            // Get subject names as strings
            List<SubjectOptionDTO> subjectOptions = service.getSubjectOptions();
            List<String> subjectNames = subjectOptions.stream()
                .map(SubjectOptionDTO::getCourseName)
                .collect(java.util.stream.Collectors.toList());
            filterData.put("subjects", subjectNames);
            
            // Get submitter names as strings  
            List<UserBasicDTO> submitterOptions = service.getSubmitterOptions();
            List<String> submitterNames = submitterOptions.stream()
                .map(UserBasicDTO::getFullName)
                .collect(java.util.stream.Collectors.toList());
            filterData.put("submitters", submitterNames);
            
            return ResponseEntity.ok(filterData);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Get filtered detections
    @GetMapping("/filtered")
    public ResponseEntity<List<DuplicateDetectionDTO>> getFilteredDetections(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String submitter,
            @RequestParam(required = false) String status) {
        try {
            List<DuplicateDetectionDTO> detections = service.getFilteredDetections(subject, submitter, status);
            return ResponseEntity.ok(detections);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }

    // Get statistics for dashboard - 100% from database
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            // Use new method that ensures 100% database data
            Map<String, Object> stats = service.getStatisticsFromDatabase();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error loading statistics: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Get processing logs
    @GetMapping("/processing-logs")
    public ResponseEntity<List<ProcessingLogDTO>> getProcessingLogs() {
        try {
            List<ProcessingLogDTO> logs = service.getProcessingLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            System.err.println("Error in controller getting processing logs: " + e.getMessage());
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }
    
    // Get processing log by ID
    @GetMapping("/processing-logs/{logId}")
    public ResponseEntity<ProcessingLogDTO> getProcessingLogById(@PathVariable Long logId) {
        try {
            ProcessingLogDTO log = service.getProcessingLogById(logId);
            if (log != null) {
                return ResponseEntity.ok(log);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error getting processing log by ID: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    // Process duplication (accept/reject)
    @PostMapping("/{detectionId}/process")
    public ResponseEntity<Map<String, Object>> processDuplication(
            @PathVariable Long detectionId,
            @RequestBody Map<String, Object> requestBody) {
        try {
            String action = (String) requestBody.get("action");
            String feedback = (String) requestBody.get("feedback");
            Object processorIdObj = requestBody.get("processorId");
            Long processorId = processorIdObj != null ? Long.valueOf(processorIdObj.toString()) : null;
            
            Map<String, Object> response = new HashMap<>();
            
            // Process the action - update database
            if ("ACCEPT".equals(action) || "accept".equals(action)) {
                // Accept the question - update detection status and question status
                service.processDetection(detectionId, "ACCEPTED", feedback, processorId);
                response.put("success", true);
                response.put("message", "Question accepted successfully");
                response.put("detectionId", detectionId);
                response.put("action", "ACCEPTED");
                return ResponseEntity.ok(response);
                
            } else if ("REJECT".equals(action) || "reject".equals(action)) {
                // Reject the question - update detection status and possibly delete/reject question
                service.processDetection(detectionId, "REJECTED", feedback, processorId);
                response.put("success", true);
                response.put("message", "Question rejected successfully");
                response.put("detectionId", detectionId);
                response.put("action", "REJECTED");
                return ResponseEntity.ok(response);
                
            } else if ("SEND_BACK".equals(action) || "send_back".equals(action)) {
                // Send back for revision - notify creator with feedback
                service.processDetection(detectionId, "SEND_BACK", feedback, processorId);
                response.put("success", true);
                response.put("message", "Question sent back for revision successfully");
                response.put("detectionId", detectionId);
                response.put("action", "SEND_BACK");
                return ResponseEntity.ok(response);
                
            } else {
                response.put("success", false);
                response.put("message", "Invalid action: " + action);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error processing request: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        response.put("service", "DuplicationStaffController");
        return ResponseEntity.ok(response);
    }

    // Get detection by ID with number validation
    @GetMapping("/{detectionId:[0-9]+}")
    public ResponseEntity<DuplicateDetectionDTO> getDetectionByIdValidated(@PathVariable Long detectionId) {
        return getDetectionById(detectionId);
    }

    // Ping endpoint for connectivity test
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}

