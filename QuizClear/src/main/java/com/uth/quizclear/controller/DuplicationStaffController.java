package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.*;
import com.uth.quizclear.service.DuplicationStaffService; // Sửa tên service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/duplication")
@CrossOrigin(origins = "*")
public class DuplicationStaffController {

    @Autowired
    private DuplicationStaffService duplicationService; // Sửa tên service

    /**
     * Lấy danh sách câu hỏi trùng lặp đã được AI phát hiện
     */
    @GetMapping("/detections")
    public ResponseEntity<List<DuplicateDetectionDTO>> getDuplicateDetections(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String submitter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        List<DuplicateDetectionDTO> detections = duplicationService.getDuplicateDetections(subject, submitter, page, size);
        return ResponseEntity.ok(detections);
    }

    /**
     * Lấy chi tiết so sánh câu hỏi trùng lặp
     */
    @GetMapping("/detections/{detectionId}")
    public ResponseEntity<DuplicateComparisonDTO> getDetectionDetail(@PathVariable Long detectionId) {
        DuplicateComparisonDTO comparison = duplicationService.getDetectionDetail(detectionId);
        return ResponseEntity.ok(comparison);
    }

    /**
     * Xử lý hành động đối với câu hỏi trùng lặp (approve, reject, send_back)
     */
    @PostMapping("/detections/{detectionId}/action")
    public ResponseEntity<String> processDetection(
            @PathVariable Long detectionId,
            @RequestBody ProcessDetectionRequest request) {
        
        duplicationService.processDetection(detectionId, request.getAction(), 
                                          request.getFeedback(), request.getProcessedBy());
        return ResponseEntity.ok("Action processed successfully");
    }

    /**
     * Lấy thống kê về việc phát hiện trùng lặp
     */
    @GetMapping("/statistics")
    public ResponseEntity<DuplicationStatisticsDTO> getStatistics() {
        DuplicationStatisticsDTO statistics = duplicationService.getStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Lấy danh sách logs xử lý
     */
    @GetMapping("/logs")
    public ResponseEntity<List<ProcessingLogDTO>> getProcessingLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        List<ProcessingLogDTO> logs = duplicationService.getProcessingLogs(page, size);
        return ResponseEntity.ok(logs);
    }

    /**
     * Lấy chi tiết log xử lý
     */
    @GetMapping("/logs/{logId}")
    public ResponseEntity<ProcessingLogDetailDTO> getLogDetail(@PathVariable String logId) {
        ProcessingLogDetailDTO logDetail = duplicationService.getLogDetail(logId);
        return ResponseEntity.ok(logDetail);
    }

    /**
     * Export dữ liệu duplicate detection
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportDetections(
            @RequestParam(required = false) String format,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String submitter) {
        
        byte[] exportData = duplicationService.exportDetections(format, subject, submitter);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=duplicate_detections.xlsx")
                .body(exportData);
    }

    /**
     * Lấy danh sách subjects để filter
     */
    @GetMapping("/subjects")
    public ResponseEntity<List<String>> getSubjects() {
        List<String> subjects = duplicationService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    /**
     * Lấy danh sách submitters để filter
     */
    @GetMapping("/submitters")
    public ResponseEntity<List<String>> getSubmitters() {
        List<String> submitters = duplicationService.getAllSubmitters();
        return ResponseEntity.ok(submitters);
    }
}