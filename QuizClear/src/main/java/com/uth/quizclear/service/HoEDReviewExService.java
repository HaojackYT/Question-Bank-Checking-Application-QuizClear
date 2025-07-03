package com.uth.quizclear.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

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

    // Load list reviews by Lec
    public List<HoEDReviewExamDTO> getReviewsByLecturer() {
        return examReviewRepository.findAllByReviewType(ReviewType.SUBJECT_LEADER);
    }
    
}
