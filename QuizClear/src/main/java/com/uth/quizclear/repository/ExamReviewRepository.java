package com.uth.quizclear.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uth.quizclear.model.ExamReview;

@Repository
public interface ExamReviewRepository extends JpaRepository<ExamReview, Integer> {
    
}