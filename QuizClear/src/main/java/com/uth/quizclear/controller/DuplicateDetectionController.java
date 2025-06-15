/*package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.*;
import com.uth.quizclear.service.DuplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/duplications")
@CrossOrigin(origins = "*")
public class DuplicateDetectionController {

    @Autowired
    private DuplicationService duplicationService;

    // =================== DUPLICATE DETECTION ENDPOINTS ===================

    @GetMapping
    public ResponseEntity<List<DuplicateDetectionDTO>> getAllDetections() {
        try {
            List<DuplicateDetectionDTO> detections = duplicationService.getAllDetections();
            return ResponseEntity.ok(detections);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/filters")
    public ResponseEntity<Map<String, List<String>>> getFilterOptions() {
        try {
            Map<String, List<String>> filterOptions = duplicationService.getDistinctFilterOptions();
            return ResponseEntity.ok(filterOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DuplicateDetectionDTO> getDetectionDetails(@PathVariable Long id) {
        try {
            DuplicateDetectionDTO detectionDetails = duplicationService.getDuplicationDetailsById(id);
            if (detectionDetails == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(detectionDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DuplicateDetectionDTO>> getDetectionsByStatus(@PathVariable String status) {
        try {
            List<DuplicateDetectionDTO> detections = duplicationService.getDetectionsByStatus(status);
            return ResponseEntity.ok(detections);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<DuplicateDetectionDTO>> getPendingDetections() {
        try {
            List<DuplicateDetectionDTO> detections = duplicationService.getPendingDetections();
            return ResponseEntity.ok(detections);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/pending/count")
    public ResponseEntity<Map<String, Long>> getPendingCount() {
        try {
            long count = duplicationService.countPendingDetections();
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/high-similarity")
    public ResponseEntity<List<DuplicateDetectionDTO>> getHighSimilarityDetections(
            @RequestParam(defaultValue = "0.8") double threshold) {
        try {
            List<DuplicateDetectionDTO> detections = duplicationService.getHighSimilarityDetections(threshold);
            return ResponseEntity.ok(detections);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<Map<String, String>> processDetection(
            @PathVariable Long id,
            @RequestBody ProcessDetectionRequest request) {
        try {
            Long processorId = request.getProcessedBy();
            if (processorId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Processor ID is required"));
            }
            
            duplicationService.processDetection(id, request.getAction(), request.getFeedback(), processorId);
            return ResponseEntity.ok(Map.of("message", "Detection processed successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid action: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }

    // =================== AI DUPLICATE CHECK ENDPOINTS ===================

    @PostMapping("/ai-check")
    public ResponseEntity<AiDuplicateCheckDTO> performAiDuplicateCheck(
            @RequestBody AiDuplicateCheckRequestDTO request) {
        try {
            // Validate request
            if (!request.isValid()) {
                return ResponseEntity.badRequest().build();
            }

            AiDuplicateCheckDTO result = duplicationService.performAiDuplicateCheck(
                    request.getQuestionContent(),
                    request.getCourseId(),
                    request.getUserId()
            );
            
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/ai-check/course/{courseId}")
    public ResponseEntity<List<AiDuplicateCheckDTO>> getAiChecksByCourse(@PathVariable Long courseId) {
        try {
            List<AiDuplicateCheckDTO> checks = duplicationService.getAiChecksByCourse(courseId);
            return ResponseEntity.ok(checks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/ai-check/recent")
    public ResponseEntity<List<AiDuplicateCheckDTO>> getRecentAiChecks(
            @RequestParam(defaultValue = "7") int days) {
        try {
            List<AiDuplicateCheckDTO> checks = duplicationService.getRecentAiChecks(days);
            return ResponseEntity.ok(checks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =================== STATISTICS ENDPOINTS ===================

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> stats = duplicationService.getDuplicationStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =================== HEALTH CHECK ENDPOINT ===================

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "healthy",
                    "service", "DuplicationService",
                    "timestamp", java.time.LocalDateTime.now().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("status", "unhealthy", "error", e.getMessage()));
        }
    }
}
    */