package com.uth.quizclear.controller;

import com.uth.quizclear.model.DuplicateDetectionDTO;
import com.uth.quizclear.model.ProcessDetectionRequest;
import com.uth.quizclear.service.DuplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/duplications")
public class DuplicateDetectionController {

    @Autowired
    private DuplicationService duplicationService;

    @GetMapping
    public ResponseEntity<List<DuplicateDetectionDTO>> getAllDetections() {
        List<DuplicateDetectionDTO> detections = duplicationService.getAllDetections();
        return ResponseEntity.ok(detections);
    }

    @GetMapping("/filters")
    public ResponseEntity<Map<String, List<String>>> getFilterOptions() {
        Map<String, List<String>> filterOptions = duplicationService.getDistinctFilterOptions();
        return ResponseEntity.ok(filterOptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DuplicateDetectionDTO> getDetectionDetails(@PathVariable Integer id) {
        DuplicateDetectionDTO detectionDetails = duplicationService.getDuplicationDetailsById(id);
        if (detectionDetails == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detectionDetails);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<Void> processDetection(
            @PathVariable Integer id,
            @RequestBody ProcessDetectionRequest request) {
        try {
            Integer processorId = request.getProcessedBy();
            if (processorId == null) {
                return ResponseEntity.badRequest().body(null);
            }
            duplicationService.processDetection(id, request.getAction(), request.getFeedback(), processorId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) { // Bắt ngoại lệ cụ thể (IllegalArgumentException) trước
            return ResponseEntity.badRequest().build(); // Trả về 400 Bad Request cho lỗi dữ liệu đầu vào
        } catch (RuntimeException e) { // Sau đó bắt ngoại lệ tổng quát hơn (RuntimeException)
            // Lỗi này có thể là "Duplicate detection not found" hoặc các RuntimeException khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Trả về 500 Internal Server Error
        }
    }
}