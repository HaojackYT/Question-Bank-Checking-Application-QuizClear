package com.uth.quizclear.controller;

import com.uth.quizclear.model.DuplicateDetection;
import com.uth.quizclear.model.DuplicateDetectionDTO;
import com.uth.quizclear.model.User;
import com.uth.quizclear.service.DuplicateDetectionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/duplications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DuplicateDetectionController {
    private final DuplicateDetectionService service;

    //  GET: Danh sách đã lọc hoặc toàn bộ
    @GetMapping
    public List<DuplicateDetectionDTO> getAllFiltered(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String submitter) {
        return service.getFilteredDetections(subject, submitter);
    }

    //  GET: Trả về danh sách môn học & người nộp (dropdown filter)
    @GetMapping("/filters")
    public Map<String, List<String>> getFilters() {
        List<DuplicateDetectionDTO> all = service.getAllDetections();

        List<String> subjects = all.stream()
                .map(DuplicateDetectionDTO::getCourseName)
                .distinct()
                .sorted()
                .toList();

        List<String> submitters = all.stream()
                .map(DuplicateDetectionDTO::getSubmitterName)
                .distinct()
                .sorted()
                .toList();

        return Map.of("subjects", subjects, "submitters", submitters);
    }

    //GET: Lấy chi tiết một duplicate
    @GetMapping("/{id}")
    public DuplicateDetection getById(@PathVariable Integer id) {
        return service.getById(id)
                .orElseThrow(() -> new RuntimeException("Detection not found"));
    }

    // POST: Xử lý duplicate
    @PostMapping("/{id}/process")
    public void processDetection(
            @PathVariable Integer id,
            @RequestBody ProcessRequest request
    ) {
        User fakeProcessor = new User(); // giả lập người xử lý
        fakeProcessor.setUserId(request.getProcessedBy());

        service.processDetection(id, request.getAction(), request.getFeedback(), fakeProcessor);
    }

    // DTO nội bộ xử lý request
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ProcessRequest {
        private String action;
        private String feedback;
        private Integer processedBy;
    }
}
