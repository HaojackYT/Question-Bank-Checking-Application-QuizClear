package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.DuplicateDetectionDTO;
import com.uth.quizclear.model.dto.SubjectOptionDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.service.DuplicationStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff/duplications")
public class DuplicationStaffController {

    @Autowired
    private DuplicationStaffService service;    // Get all duplicate detections
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
    }    // Get detections by subject
    @GetMapping("/subject/{courseId}")
    public ResponseEntity<List<DuplicateDetectionDTO>> getDetectionsBySubject(@PathVariable Long courseId) {
        return ResponseEntity.ok(service.getDetectionsBySubject(courseId));
    }

    // Process a detection
    @PostMapping("/{detectionId}/process")
    public ResponseEntity<DuplicateDetectionDTO> processDetection(
            @PathVariable Long detectionId,
            @RequestBody ProcessRequest request) {
        return ResponseEntity.ok(service.processDetection(
                detectionId,
                request.getAction(),
                request.getFeedback(),
                request.getProcessorId()
        ));
    }

    // Get subject options
    @GetMapping("/subject-options")
    public ResponseEntity<List<SubjectOptionDTO>> getSubjectOptions() {
        return ResponseEntity.ok(service.getSubjectOptions());
    }

    // Get submitter options
    @GetMapping("/submitter-options")
    public ResponseEntity<List<UserBasicDTO>> getSubmitterOptions() {
        return ResponseEntity.ok(service.getSubmitterOptions());
    }    // Simple test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> simpleTest() {
        StringBuilder result = new StringBuilder();
        result.append("API is working at ").append(java.time.LocalDateTime.now());
        
        // Test service availability
        if (service == null) {
            result.append("\nService: NULL");
            return ResponseEntity.ok(result.toString());
        }
        result.append("\nService: Available");
        
        // Test database connection
        try {
            String dbTest = service.testRepository();
            result.append("\nDatabase test: ").append(dbTest);
        } catch (Exception e) {
            result.append("\nDatabase error: ").append(e.getClass().getSimpleName())
                  .append(" - ").append(e.getMessage());
        }
        
        return ResponseEntity.ok(result.toString());
    }

    // Test database connection without entity mapping
    @GetMapping("/test-db")
    public ResponseEntity<Map<String, Object>> testDatabaseSimple() {
        Map<String, Object> result = new HashMap<>();
        try {
            // Use simple SQL query instead of entity
            result.put("status", "success");
            result.put("message", "Simple test successful");
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(500).body(result);
        }
    }

    // Debug endpoint to test database connection
    @GetMapping("/debug")
    public ResponseEntity<Map<String, Object>> debugDatabase() {
        Map<String, Object> result = new HashMap<>();
        try {
            // Test raw repository connection
            result.put("status", "success");
            result.put("message", "Database connection successful");
            result.put("rawCount", service.testRawRepository().size());
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            result.put("error", e.getClass().getSimpleName());
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(500).body(result);
        }    }

    // Simple test endpoint to debug database issues
    @GetMapping("/test-simple")
    public ResponseEntity<String> testSimple() {
        try {
            return ResponseEntity.ok("DuplicationStaffController is working at " + java.time.LocalDateTime.now());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // Simple test endpoint without complex logic
    @GetMapping("/simple-test")
    public ResponseEntity<Map<String, Object>> simpleTestEndpoint() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("status", "success");
            result.put("message", "Simple endpoint working");
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            result.put("service_status", service != null ? "injected" : "null");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            result.put("error_type", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(result);
        }
    }

    // Test repository connection
    @GetMapping("/test-repository")
    public ResponseEntity<Map<String, Object>> testRepository() {
        Map<String, Object> result = new HashMap<>();
        try {
            String testResult = service.testRepository();
            result.put("status", "success");
            result.put("message", testResult);
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            result.put("error_type", e.getClass().getSimpleName());
            e.printStackTrace();
            return ResponseEntity.status(500).body(result);
        }
    }    // Get detection by ID - TEMPORARILY COMMENTED TO AVOID CONFLICTS
    /*
    @GetMapping("/{detectionId}")
    public ResponseEntity<DuplicateDetectionDTO> getDetectionById(@PathVariable Long detectionId) {
        return ResponseEntity.ok(service.getDetectionById(detectionId));
    }
    */

    // Request body for processing
    public static class ProcessRequest {
        private String action;
        private String feedback;
        private Long processorId;

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getFeedback() { return feedback; }
        public void setFeedback(String feedback) { this.feedback = feedback; }
        public Long getProcessorId() { return processorId; }
        public void setProcessorId(Long processorId) { this.processorId = processorId; }
    }

    // Debug endpoint with completely different name
    @GetMapping("/debug-database")
    public ResponseEntity<Map<String, Object>> debugDatabaseStatus() {
        Map<String, Object> result = new HashMap<>();
        try {
            // Test basic repository
            long count = service.testRawRepository().size();
            result.put("status", "success");
            result.put("repository_count", count);
            result.put("service_available", service != null);
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            result.put("error_type", e.getClass().getSimpleName());
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(500).body(result);
        }
    }

    // Simple endpoint without dashes
    @GetMapping("/dbtest")
    public ResponseEntity<String> dbTest() {
        try {
            String result = service.testRepository();
            return ResponseEntity.ok("Database test result: " + result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // Get filter options for subjects and submitters
    @GetMapping("/filters")
    public ResponseEntity<Map<String, Object>> getFilterOptions() {
        try {
            Map<String, Object> filterOptions = service.getFilterOptions();
            return ResponseEntity.ok(filterOptions);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("type", e.getClass().getSimpleName());
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
      // Get filtered detections
    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredDetections(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String submitter,
            @RequestParam(required = false) String status) {
        try {
            List<DuplicateDetectionDTO> detections = service.getFilteredDetections(subject, submitter, status);
            return ResponseEntity.ok(detections);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("type", e.getClass().getSimpleName());
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}