package com.uth.quizclear.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.ExReviewAssignDTO;
import com.uth.quizclear.model.dto.HoEDReviewExamDTO;
import com.uth.quizclear.model.enums.ReviewType;
import com.uth.quizclear.repository.ExamReviewRepository;

@Service
public class HoEDReviewExService {

    private final ExamReviewRepository examReviewRepository;

    public HoEDReviewExService(ExamReviewRepository examReviewRepository) {
        this.examReviewRepository = examReviewRepository;
    }

    // Load list reviews (for Debug)
    public List<HoEDReviewExamDTO> getAllReviews() {
        return examReviewRepository.findAllReview();
    }

    // Load list reviews for HoED
    public List<HoEDReviewExamDTO> getReviewsByED() {
        return examReviewRepository.findAllByReviewType(ReviewType.EXAMINATION_DEPARTMENT);
    }

    // Load exams for new review assignment
    public List<ExReviewAssignDTO> getAllExams() {
        return examReviewRepository.findAllExamsForReviewAssignment();
    }
}
