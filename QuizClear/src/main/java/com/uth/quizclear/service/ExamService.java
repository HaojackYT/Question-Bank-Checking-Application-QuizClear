package com.uth.quizclear.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.uth.quizclear.repository.ExamRepository;
import com.uth.quizclear.repository.ExamReviewRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.uth.quizclear.model.Exam.ExamStatus;
import com.uth.quizclear.model.ExamReview.ExamReviewStatus;

import com.uth.quizclear.model.ExamReview;
import com.uth.quizclear.model.Exam;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamReviewRepository examReviewRepository;

    public long getTotalExams() {
        return examRepository.count();
    }

    public long getPendingApprovalExams() {
        // ExamStatus.SUBMITTED là trạng thái chờ duyệt (Pending Approval)
        return examRepository.findByStatus(ExamStatus.SUBMITTED).size();
    }

    public long getApprovedExams() {
        return examRepository.findByStatus(ExamStatus.APPROVED).size();
    }

    public long getRejectedExams() {
        return examRepository.findByStatus(ExamStatus.REJECTED).size();
    }

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public List<Exam> getExamsByStatus(ExamStatus status) {
        return examRepository.findByStatus(status);
    }

    public Optional<Exam> getExamById(Long examId) {
        return examRepository.findById(examId);
    }

    // TODO: Check 
    public Exam updateExamStatus(Long examId, ExamStatus newStatus) {
        Optional<Exam> optionalExam = examRepository.findById(examId);
        if (optionalExam.isPresent()) {
            Exam exam = optionalExam.get();
            exam.setStatus(newStatus);
            exam.setUpdatedAt(LocalDateTime.now());
            if (newStatus == ExamStatus.APPROVED) {
                exam.setApprovedAt(LocalDateTime.now());
                // TODO: exam.setApprovedBy(currentUser); // Cần thông tin người dùng hiện tại
            } else if (newStatus == ExamStatus.REJECTED) {
                // TODO: Xử lý logic khi bị từ chối
            }
            return examRepository.save(exam);
        }
        return null;
    }

    public ExamReview createExamReview(ExamReview examReview) {
        examReview.setCreatedAt(LocalDateTime.now()); 
        return examReviewRepository.save(examReview);
    }

    // TODO: Check 
    public Exam updateExamReviewStatus(Long reviewId, ExamReviewStatus newStatus, String comments, String suggestions) {
        Optional<ExamReview> optionalExamReview = examReviewRepository.findById(reviewId);
        if (optionalExamReview.isPresent()) {
            ExamReview examReview = optionalExamReview.get();
            examReview.setStatus(newStatus);
            examReview.setComments(comments);
            // Note: suggestions parameter is kept for controller compatibility but ExamReview model doesn't have suggestions field
            examReview.setCreatedAt(LocalDateTime.now()); // Cập nhật thời gian
            examReviewRepository.save(examReview);

            // Cập nhật trạng thái của Exam dựa trên review cuối cùng (có thể có nhiều review)
            Exam exam = examReview.getExam();
            if (newStatus == ExamReviewStatus.APPROVED) {
                exam.setStatus(ExamStatus.APPROVED);
                exam.setApprovedAt(LocalDateTime.now()); 
            } else if (newStatus == ExamReviewStatus.REJECTED) {
                exam.setStatus(ExamStatus.REJECTED);
            } else if (newStatus == ExamReviewStatus.NEEDS_REVISION) {
                exam.setStatus(ExamStatus.REJECTED);
            }
            exam.setReviewedAt(LocalDateTime.now()); 
            // TODO: exam.setReviewedBy(currentUser); // Cần thông tin người dùng hiện tại
            return examRepository.save(exam);
        }
        return null;
    }
}