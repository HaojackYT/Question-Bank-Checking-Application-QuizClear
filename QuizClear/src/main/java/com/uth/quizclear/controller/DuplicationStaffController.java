package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.DuplicateDetectionDTO;
import com.uth.quizclear.model.dto.SubjectOptionDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.DuplicateDetection;
import com.uth.quizclear.service.DuplicationStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    }    // Process a detection - Support both JSON and form data
    @PostMapping("/{detectionId}/process")
    public ResponseEntity<?> processDetection(
            @PathVariable Long detectionId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String feedback,
            @RequestParam(required = false) Long processorId,
            @RequestBody(required = false) ProcessRequest request,
            jakarta.servlet.http.HttpServletRequest httpRequest) {
          
        System.out.println("=== CONTROLLER CALLED ===");
        System.out.println("Detection ID from path: " + detectionId);
        System.out.println("==========================");
        
        try {
            String finalAction = null;
            String finalFeedback = null;
            Long finalProcessorId = null;
            
            // Debug log the request
            System.out.println("=== REQUEST DEBUG ===");
            System.out.println("Content-Type: " + httpRequest.getContentType());
            System.out.println("Method: " + httpRequest.getMethod());
            System.out.println("Action param: " + action);
            System.out.println("Feedback param: " + feedback);
            System.out.println("ProcessorId param: " + processorId);
            System.out.println("Request body: " + request);
            System.out.println("====================");
            
            // Check if it's form data submission
            String contentType = httpRequest.getContentType();
            if (contentType != null && contentType.contains("application/x-www-form-urlencoded")) {
                finalAction = action;
                finalFeedback = feedback;
                finalProcessorId = processorId != null ? processorId : 1L; // Default processor ID
            } else if (request != null) {
                // JSON submission
                finalAction = request.getAction();
                finalFeedback = request.getFeedback();
                finalProcessorId = request.getProcessorId() != null ? request.getProcessorId() : 1L;
            } else {
                // Try to get from parameters anyway (fallback)
                finalAction = action;
                finalFeedback = feedback;
                finalProcessorId = processorId != null ? processorId : 1L;
            }
            
            if (finalAction == null || finalAction.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Action is required");
                errorResponse.put("receivedAction", action);
                errorResponse.put("contentType", contentType);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            System.out.println("=== PROCESSING DETECTION ===");
            System.out.println("Detection ID: " + detectionId);
            System.out.println("Action: " + finalAction);
            System.out.println("Feedback: " + finalFeedback);
            System.out.println("Processor ID: " + finalProcessorId);
            System.out.println("============================");
            
            DuplicateDetectionDTO result = service.processDetection(
                    detectionId,
                    finalAction,
                    finalFeedback,
                    finalProcessorId
            );
            
            // If it's form submission, return a redirect response
            if ("application/x-www-form-urlencoded".equals(httpRequest.getContentType())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Detection processed successfully");
                response.put("action", finalAction);
                response.put("redirect", "/staff/duplications"); // URL to redirect to
                return ResponseEntity.ok(response);
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            System.err.println("Error processing detection: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
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
    }    // Get filtered detections with enhanced error handling
    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredDetections(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String submitter,
            @RequestParam(required = false) String status) {
        try {
            System.out.println("=== FILTER REQUEST ===");
            System.out.println("Subject: " + subject);
            System.out.println("Submitter: " + submitter);
            System.out.println("Status: " + status);
            System.out.println("=====================");
            
            List<DuplicateDetectionDTO> detections = service.getFilteredDetections(subject, submitter, status);
            
            System.out.println("=== FILTER RESPONSE ===");
            System.out.println("Returned: " + detections.size() + " items");
            System.out.println("=======================");
            
            return ResponseEntity.ok(detections);
        } catch (Exception e) {
            System.err.println("=== FILTER ERROR ===");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error type: " + e.getClass().getSimpleName());
            e.printStackTrace();
            System.err.println("====================");
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Filter operation failed: " + e.getMessage());
            errorResponse.put("type", e.getClass().getSimpleName());
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            errorResponse.put("filters", Map.of(
                "subject", subject != null ? subject : "null",
                "submitter", submitter != null ? submitter : "null", 
                "status", status != null ? status : "null"
            ));
            return ResponseEntity.status(500).body(errorResponse);
        }
    }    // Health check endpoint - MUST BE BEFORE /{detectionId}
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        try {
            health.put("status", "UP");
            health.put("timestamp", java.time.LocalDateTime.now().toString());
            health.put("service", service != null ? "AVAILABLE" : "NULL");
            
            // Test basic database connection
            List<DuplicateDetectionDTO> allDetections = service.getAllDetections();
            health.put("database", "CONNECTED");
            health.put("total_detections", allDetections.size());
            
            // Test filter options
            Map<String, Object> filterOptions = service.getFilterOptions();
            health.put("filter_options", "AVAILABLE");
            health.put("subjects_count", ((List<?>) filterOptions.get("subjects")).size());
            health.put("submitters_count", ((List<?>) filterOptions.get("submitters")).size());
            
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
            health.put("error_type", e.getClass().getSimpleName());
            health.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(500).body(health);
        }
    }    // Get detection details by id (for view button) - Use regex to only match numbers
    @GetMapping("/{detectionId:[0-9]+}")
    public ResponseEntity<DuplicateDetectionDTO> getDetectionById(@PathVariable Long detectionId) {
        return ResponseEntity.ok(service.getDetectionById(detectionId));
    }

    // Simple test endpoint for filters - DIAGNOSTIC ONLY
    @GetMapping("/test-filter")
    public ResponseEntity<Map<String, Object>> testFilterEndpoint(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String submitter) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            result.put("received_subject", subject);
            result.put("received_submitter", submitter);
            result.put("service_available", service != null);
            
            // Test basic getAllDetections first
            List<DuplicateDetectionDTO> allDetections = service.getAllDetections();
            result.put("total_detections", allDetections.size());
            
            if (!allDetections.isEmpty()) {
                DuplicateDetectionDTO first = allDetections.get(0);
                result.put("first_detection_id", first.getDetectionId());
                result.put("first_new_question", first.getNewQuestion() != null ? 
                    first.getNewQuestion().getContent() : "NULL");
                result.put("first_course_name", first.getNewQuestion() != null ? 
                    first.getNewQuestion().getCourseName() : "NULL");
                result.put("first_creator_name", first.getNewQuestion() != null ?
                    first.getNewQuestion().getCreatorName() : "NULL");
            }
            
            result.put("status", "SUCCESS");
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("error", e.getMessage());
            result.put("error_type", e.getClass().getSimpleName());
            e.printStackTrace();
            return ResponseEntity.status(500).body(result);
        }
    }
    
    // Simple ping endpoint - absolutely no dependencies
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong - " + java.time.LocalDateTime.now());
    }

    // Test endpoint để debug
    @PostMapping("/test-endpoint")
    public ResponseEntity<?> testEndpoint(@RequestBody(required = false) ProcessRequest request) {
        System.out.println("=== TEST ENDPOINT CALLED ===");
        System.out.println("Request: " + request);
        if (request != null) {
            System.out.println("Action: " + request.getAction());
            System.out.println("Feedback: " + request.getFeedback());
            System.out.println("ProcessorId: " + request.getProcessorId());
        }
        System.out.println("============================");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Test endpoint working");
        response.put("received", request);
        return ResponseEntity.ok(response);
    }    // Get duplication statistics
    @GetMapping("/statistics")
    public ResponseEntity<?> getDuplicationStatistics() {
        try {
            System.out.println("=== STATISTICS API CALLED ===");
            Map<String, Object> statistics = service.getDuplicationStatistics();
            System.out.println("Statistics data: " + statistics);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            System.err.println("Error getting statistics: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("type", e.getClass().getSimpleName());
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Debug/Test endpoint to check database state
    @GetMapping("/debug-state")
    public ResponseEntity<Map<String, Object>> debugDatabaseState() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DuplicateDetection> allDetections = service.testRawRepository();
            result.put("totalDetections", allDetections.size());
            
            // Count by status
            Map<String, Long> statusCounts = allDetections.stream()
                .collect(Collectors.groupingBy(
                    d -> d.getStatusString() != null ? d.getStatusString() : "null",
                    Collectors.counting()
                ));
            result.put("statusCounts", statusCounts);
            
            // Count by action
            Map<String, Long> actionCounts = allDetections.stream()
                .collect(Collectors.groupingBy(
                    d -> d.getActionString() != null ? d.getActionString() : "null", 
                    Collectors.counting()
                ));
            result.put("actionCounts", actionCounts);
            
            // Recent processed items
            List<Map<String, Object>> recentProcessed = allDetections.stream()
                .filter(d -> d.getProcessedAt() != null)
                .sorted((a, b) -> b.getProcessedAt().compareTo(a.getProcessedAt()))
                .limit(5)
                .map(d -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("detectionId", d.getDetectionId());
                    item.put("status", d.getStatusString());
                    item.put("action", d.getActionString());
                    item.put("processedAt", d.getProcessedAt());
                    return item;
                })
                .collect(Collectors.toList());
            result.put("recentProcessed", recentProcessed);
            
            result.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.status(500).body(result);
        }
    }
    
    // Force refresh statistics endpoint
    @PostMapping("/refresh-stats")
    public ResponseEntity<Map<String, Object>> refreshStatistics() {
        try {
            System.out.println("=== FORCE REFRESH STATISTICS ===");
            Map<String, Object> statistics = service.getDuplicationStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Statistics refreshed successfully");
            response.put("data", statistics);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error refreshing statistics: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Debug endpoint to check database state
    @GetMapping("/debug/{detectionId}")
    public ResponseEntity<Map<String, Object>> debugDatabaseState(
            @PathVariable Long detectionId,
            @RequestParam(required = false) Long questionId) {
        
        System.out.println("=== DEBUG ENDPOINT CALLED ===");
        System.out.println("Detection ID: " + detectionId);
        System.out.println("Question ID: " + questionId);
        
        try {
            service.debugDatabaseState(detectionId, questionId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Database state logged to console");
            response.put("detectionId", detectionId);
            response.put("questionId", questionId);
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Test endpoint to verify database operations
    @PostMapping("/test-delete/{detectionId}")
    public ResponseEntity<Map<String, Object>> testDeleteDetection(@PathVariable Long detectionId) {
        System.out.println("=== TEST DELETE ENDPOINT ===");
        System.out.println("Testing deletion of detection: " + detectionId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // First, get the detection to see its current state
            service.debugDatabaseState(detectionId, null);
            
            // Check if detection exists before deletion
            boolean existsBefore = service.getRepository().existsById(detectionId);
            System.out.println("Detection exists before deletion: " + existsBefore);
            
            if (!existsBefore) {
                response.put("success", false);
                response.put("message", "Detection not found before deletion");
                return ResponseEntity.ok(response);
            }
              // Delete the detection
            service.getRepository().deleteById(detectionId);
            
            // Force flush through service
            service.forceFlush();
            
            // Check if detection exists after deletion
            boolean existsAfter = service.getRepository().existsById(detectionId);
            System.out.println("Detection exists after deletion: " + existsAfter);
            
            // Debug database state again
            service.debugDatabaseState(detectionId, null);
            
            response.put("success", true);
            response.put("existsBefore", existsBefore);
            response.put("existsAfter", existsAfter);
            response.put("message", "Delete test completed");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error in test delete: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Get processing logs (history)
    @GetMapping("/processing-logs")
    public ResponseEntity<?> getProcessingLogs() {
        try {
            System.out.println("=== PROCESSING LOGS API CALLED ===");
            List<Map<String, Object>> logs = service.getProcessingLogs();
            System.out.println("Processing logs data: " + logs.size() + " entries");
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            System.err.println("Error getting processing logs: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("type", e.getClass().getSimpleName());
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}