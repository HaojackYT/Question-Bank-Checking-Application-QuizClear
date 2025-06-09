package com.uth.quizclear.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.uth.quizclear.model.Exam;
import com.uth.quizclear.model.ExamReview;
import com.uth.quizclear.model.ExamReviewStatus;
import com.uth.quizclear.model.ExamStatus;
import com.uth.quizclear.service.ExamService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    @Autowired
    private ExamService examService;

    // API để lấy các số liệu tổng quan trên dashboard
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getExamSummary() {
        Map<String, Long> summary = new HashMap<>();
        summary.put("totalExams", examService.getTotalExams());
        summary.put("pendingApproval", examService.getPendingApprovalExams());
        summary.put("approved", examService.getApprovedExams());
        summary.put("rejected", examService.getRejectedExams());
        return ResponseEntity.ok(summary);
    }

    // TODO: CHECK
    // API để lấy danh sách tất cả các bài thi
    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams(@RequestParam(required = false) String status) {
        if (status != null && !status.isEmpty()) {
            try {
                ExamStatus examStatus = ExamStatus.valueOf(status.toUpperCase()); 
                return ResponseEntity.ok(examService.getExamsByStatus(examStatus));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok(examService.getAllExams());
    }

    // TODO: CHECK
    // API để lấy chi tiết một bài thi
    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable Integer id) {
        Optional<Exam> exam = examService.getExamById(id);
        return exam.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // TODO: CHECK
    // API để duyệt/từ chối một bài thi (ví dụ: người duyệt cuối cùng)
    @PutMapping("/{id}/status")
    public ResponseEntity<Exam> updateExamStatus(@PathVariable Integer id, @RequestParam ExamStatus newStatus) {
        Exam updatedExam = examService.updateExamStatus(id, newStatus); // 
        if (updatedExam != null) {
            return ResponseEntity.ok(updatedExam);
        }
        return ResponseEntity.notFound().build();
    }

    // TODO: CHECK
    // API để tạo một review mới cho bài thi
    @PostMapping("/{examId}/reviews")
    public ResponseEntity<ExamReview> createExamReview(@PathVariable Integer examId, @RequestBody ExamReview examReview) {
        Optional<Exam> exam = examService.getExamById(examId);
        if (exam.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        examReview.setExam(exam.get()); // 
        // Cần truyền reviewer_id từ authentication context
        // examReview.setReviewer(currentUser);
        ExamReview createdReview = examService.createExamReview(examReview); // 
        return ResponseEntity.ok(createdReview);
    }

    // TODO: CHECK
    // API để cập nhật trạng thái của một review và cập nhật trạng thái Exam tương ứng
    @PutMapping("/reviews/{reviewId}/status")
    public ResponseEntity<Exam> updateReviewStatus(
            @PathVariable Integer reviewId,
            @RequestParam ExamReviewStatus newStatus, // [cite: 34]
            @RequestParam(required = false) String comments, // [cite: 34]
            @RequestParam(required = false) String suggestions) { // 
        Exam updatedExam = examService.updateExamReviewStatus(reviewId, newStatus, comments, suggestions);
        if (updatedExam != null) {
            return ResponseEntity.ok(updatedExam);
        }
        return ResponseEntity.notFound().build();
    }
}
