package com.uth.quizclear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uth.quizclear.model.entity.ExamReview;
import com.uth.quizclear.model.enums.ReviewType;
import com.uth.quizclear.model.enums.ExamReviewStatus;

import java.util.List;

@Repository
public interface ExamReviewRepository extends JpaRepository<ExamReview, Long> {

    // Lấy danh sách review theo loại reviewer và trạng thái
    List<ExamReview> findByReviewTypeAndStatus(ReviewType reviewType, ExamReviewStatus status);

    // Lấy danh sách review theo loại reviewer (tất cả trạng thái)
    List<ExamReview> findByReviewTypeOrderByCreatedAtDesc(ReviewType reviewType);

    // Lấy danh sách review theo trạng thái (tất cả loại reviewer)
    List<ExamReview> findByStatusOrderByCreatedAtDesc(ExamReviewStatus status);

    // Đếm số lượng review theo loại reviewer và trạng thái
    long countByReviewTypeAndStatus(ReviewType reviewType, ExamReviewStatus status);

    // Tìm kiếm review theo tiêu đề exam và loại reviewer
    @Query("SELECT er FROM ExamReview er WHERE er.reviewType = :reviewType AND er.exam.examTitle LIKE %:searchTerm%")
    List<ExamReview> findByReviewTypeAndExamTitleContaining(@Param("reviewType") ReviewType reviewType,
            @Param("searchTerm") String searchTerm);
}