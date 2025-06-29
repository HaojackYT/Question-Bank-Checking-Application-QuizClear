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
            filterData.put("subjects", service.getSubjectOptions());
            filterData.put("submitters", service.getSubmitterOptions());
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

