package com.uth.quizclear.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uth.quizclear.model.dto.ExReviewAssignDTO;
import com.uth.quizclear.model.dto.HoEDReviewExamDTO;
import com.uth.quizclear.model.entity.ExamReview;
import com.uth.quizclear.model.enums.ExamReviewStatus;
import com.uth.quizclear.model.enums.ReviewType;

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

    // Lấy hết review (debug)
    @Query("""
        SELECT new com.uth.quizclear.model.dto.HoEDReviewExamDTO(
            er.reviewId, 
            er.exam, 
            er.reviewer, 
            er.reviewType, 
            er.status, 
            er.comments, 
            er.createdAt, 
            er.dueDate) 
        FROM ExamReview er
        """)
    List<HoEDReviewExamDTO> findAllReview();

    // Lấy danh sách theo loại review
    @Query("""
        SELECT new com.uth.quizclear.model.dto.HoEDReviewExamDTO(
            er.reviewId,
            er.exam,
            er.reviewer,
            er.reviewType,
            er.status,
            er.comments,
            er.createdAt,
            er.dueDate)
        FROM ExamReview er
        WHERE er.reviewType = :reviewType
        ORDER BY er.createdAt DESC
        """)
    List<HoEDReviewExamDTO> findAllByReviewType(@Param("reviewType") ReviewType reviewType);

    // Lấy danh sách đề thi để tạo review mới
    @Query("""
        SELECT new com.uth.quizclear.model.dto.ExReviewAssignDTO(
            e.examId,
            e.examTitle,
            e.examStatus,
            e.createdBy.userId,
            e.createdBy.fullName,
            e.updatedAt,
            e.createdAt
        )
        FROM Exam e
            WHERE e.examStatus <> com.uth.quizclear.model.enums.ExamStatus.DRAFT
        """)
    List<ExReviewAssignDTO> findAllExamsForReviewAssignment();
}
