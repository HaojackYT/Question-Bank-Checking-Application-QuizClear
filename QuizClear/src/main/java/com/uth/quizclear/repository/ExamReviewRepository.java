package com.uth.quizclear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uth.quizclear.model.ExamReview;

@Repository
public interface ExamReviewRepository extends JpaRepository<ExamReview, Long> {
    // Có thể thêm các method custom nếu cần
}