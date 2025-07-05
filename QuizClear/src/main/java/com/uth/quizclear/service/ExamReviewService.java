package com.uth.quizclear.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.ExamQuestionDTO;
import com.uth.quizclear.model.dto.ExamReviewDTO;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.ExamQuestion;
import com.uth.quizclear.model.entity.ExamReview;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.enums.ReviewType;
import com.uth.quizclear.model.enums.ExamReviewStatus;
import com.uth.quizclear.repository.ExamQuestionRepository;
import com.uth.quizclear.repository.ExamRepository;
import com.uth.quizclear.repository.ExamReviewRepository;
import com.uth.quizclear.model.enums.QuestionStatus;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ExamReviewService {
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamQuestionRepository examQuestionRepository;
    @Autowired
    private ExamReviewRepository examReviewRepository;

    public ExamReviewDTO getExamReview(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found"));

        List<ExamQuestion> examQuestions = examQuestionRepository.findByExam_ExamIdOrderByQuestionOrder(examId);

        int reviewed = 0, approved = 0, rejected = 0;
        List<ExamQuestionDTO> questionDTOs = new ArrayList<>();
        for (ExamQuestion eq : examQuestions) {
            Question q = eq.getQuestion();
            // Đếm trạng thái
            if (q.getStatus() == QuestionStatus.APPROVED)
                approved++;
            else if (q.getStatus() == QuestionStatus.REJECTED)
                rejected++;
            if (q.getStatus() != QuestionStatus.DRAFT)
                reviewed++;

            // Lấy đáp án
            List<String> options = new ArrayList<>();
            options.add(q.getAnswerKey());
            if (q.getAnswerF1() != null)
                options.add(q.getAnswerF1());
            if (q.getAnswerF2() != null)
                options.add(q.getAnswerF2());
            if (q.getAnswerF3() != null)
                options.add(q.getAnswerF3());

            ExamQuestionDTO dto = new ExamQuestionDTO();
            dto.setExamQuestionId(eq.getExamQuestionId());
            dto.setQuestionOrder(eq.getQuestionOrder());
            dto.setContent(q.getContent());
            dto.setOptions(options);
            dto.setCorrectIndex(0); // giả sử đáp án đúng là answerKey (A)
            dto.setDifficulty(q.getDifficultyLevel().getValue());
            dto.setStatus(q.getStatus().getValue());
            dto.setFeedback(q.getFeedback());
            questionDTOs.add(dto);
        }

        ExamReviewDTO reviewDTO = new ExamReviewDTO();
        reviewDTO.setExamId(exam.getExamId());
        reviewDTO.setExamTitle(exam.getExamTitle());
        reviewDTO.setExamCode(exam.getExamCode());
        reviewDTO.setCourseName(exam.getCourse().getCourseName());
        reviewDTO.setTotalQuestions(examQuestions.size());
        reviewDTO.setReviewedCount(reviewed);
        reviewDTO.setApprovedCount(approved);
        reviewDTO.setRejectedCount(rejected);
        reviewDTO.setRemainingCount(examQuestions.size() - reviewed);
        reviewDTO.setQuestions(questionDTOs);
        return reviewDTO;    } // HED methods for approval workflow

    public List<com.uth.quizclear.model.dto.ExamSummaryDTO> getPendingExamsForApproval() {
        // TODO: Replace with actual database query
        // Return mock data for now
        List<com.uth.quizclear.model.dto.ExamSummaryDTO> result = new ArrayList<>();
        
        // Mock data
        result.add(new com.uth.quizclear.model.dto.ExamSummaryDTO(
                1L,
                "Introduction to Computer Science Exam",
                "Introduction to Computer Science",
                "Computer Science Department",
                java.time.LocalDateTime.now().minusHours(2),
                java.time.LocalDateTime.now().plusDays(7),
                com.uth.quizclear.model.enums.ExamReviewStatus.PENDING,
                "Tran Thi B",
                20));
                
        result.add(new com.uth.quizclear.model.dto.ExamSummaryDTO(
                2L,
                "Database Management Systems Quiz",
                "Database Management Systems",
                "Computer Science Department",
                java.time.LocalDateTime.now().minusHours(4),
                java.time.LocalDateTime.now().plusDays(5),
                com.uth.quizclear.model.enums.ExamReviewStatus.PENDING,
                "Nguyen Van A",
                25));
                  result.add(new com.uth.quizclear.model.dto.ExamSummaryDTO(
                3L,
                "Operating Systems Test",
                "Operating Systems",
                "Computer Science Department",
                java.time.LocalDateTime.now().minusHours(1),
                java.time.LocalDateTime.now().plusDays(10),
                com.uth.quizclear.model.enums.ExamReviewStatus.PENDING,
                "Le Thi C",
                30));
        
        return result;
    }    public com.uth.quizclear.model.dto.ExamSummaryDTO getExamDetailsById(Long examId) {
        // TODO: Replace with actual database query
        // Return mock data for now
        return new com.uth.quizclear.model.dto.ExamSummaryDTO(
                examId,
                "Sample Exam " + examId,
                "Sample Course",
                "Computer Science Department",
                java.time.LocalDateTime.now().minusHours(2),
                java.time.LocalDateTime.now().plusDays(7),
                com.uth.quizclear.model.enums.ExamReviewStatus.PENDING,
                "Dr. Sample",
                25);
    }    public void approveExam(Long examId, String feedback) {
        // TODO: Implement actual approve logic
        System.out.println("Approving exam " + examId + " with feedback: " + feedback);
    }

    public void rejectExam(Long examId, String feedback) {
        // TODO: Implement actual reject logic
        System.out.println("Rejecting exam " + examId + " with feedback: " + feedback);
    }

    public List<com.uth.quizclear.model.dto.ExamSummaryDTO> searchExams(String query, String status, String subject) {
        List<com.uth.quizclear.model.dto.ExamSummaryDTO> allExams = getPendingExamsForApproval();

        return allExams.stream()
                .filter(exam -> (query == null || query.isEmpty() ||
                        exam.getExamTitle().toLowerCase().contains(query.toLowerCase()) ||
                        exam.getCreatedBy().toLowerCase().contains(query.toLowerCase())) &&
                        (status == null || status.isEmpty()
                                || exam.getReviewStatus().toString().equalsIgnoreCase(status))
                        &&
                        (subject == null || subject.isEmpty() || exam.getCourseName().equalsIgnoreCase(subject)))
                .collect(java.util.stream.Collectors.toList());
    }

    // ============ Phương thức mới cho HOE Approvals ============

    /**
     * Lấy danh sách tất cả review cần phê duyệt bởi Head of Examination Department
     */
    public List<ExamReview> getAllApprovalsForHOE() {
        return examReviewRepository.findByReviewTypeOrderByCreatedAtDesc(ReviewType.EXAMINATION_DEPARTMENT);
    }

    /**
     * Lấy danh sách review pending cho Head of Examination Department
     */
    public List<ExamReview> getPendingApprovalsForHOE() {
        return examReviewRepository.findByReviewTypeAndStatus(ReviewType.EXAMINATION_DEPARTMENT,
                ExamReviewStatus.PENDING);
    }

    /**
     * Lấy danh sách review đã approved bởi Head of Examination Department
     */
    public List<ExamReview> getApprovedReviewsByHOE() {
        return examReviewRepository.findByReviewTypeAndStatus(ReviewType.EXAMINATION_DEPARTMENT,
                ExamReviewStatus.APPROVED);
    }

    /**
     * Lấy danh sách review bị rejected bởi Head of Examination Department
     */
    public List<ExamReview> getRejectedReviewsByHOE() {
        return examReviewRepository.findByReviewTypeAndStatus(ReviewType.EXAMINATION_DEPARTMENT,
                ExamReviewStatus.REJECTED);
    }

    /**
     * Lấy danh sách review cần revision bởi Head of Examination Department
     */
    public List<ExamReview> getNeedsRevisionReviewsByHOE() {
        return examReviewRepository.findByReviewTypeAndStatus(ReviewType.EXAMINATION_DEPARTMENT,
                ExamReviewStatus.NEEDS_REVISION);
    }

    /**
     * Tìm kiếm review theo tiêu đề exam
     */
    public List<ExamReview> searchReviewsByExamTitle(String searchTerm) {
        return examReviewRepository.findByReviewTypeAndExamTitleContaining(ReviewType.EXAMINATION_DEPARTMENT,
                searchTerm);
    }

    /**
     * Lấy thống kê số lượng review theo trạng thái
     */
    public long countPendingReviews() {
        return examReviewRepository.countByReviewTypeAndStatus(ReviewType.EXAMINATION_DEPARTMENT,
                ExamReviewStatus.PENDING);
    }

    public long countApprovedReviews() {
        return examReviewRepository.countByReviewTypeAndStatus(ReviewType.EXAMINATION_DEPARTMENT,
                ExamReviewStatus.APPROVED);
    }

    public long countRejectedReviews() {
        return examReviewRepository.countByReviewTypeAndStatus(ReviewType.EXAMINATION_DEPARTMENT,
                ExamReviewStatus.REJECTED);
    }

    public long countNeedsRevisionReviews() {
        return examReviewRepository.countByReviewTypeAndStatus(ReviewType.EXAMINATION_DEPARTMENT,
                ExamReviewStatus.NEEDS_REVISION);
    }

    /**
     * Lấy review theo ID
     */
    public ExamReview getReviewById(Long reviewId) {
        return examReviewRepository.findById(reviewId)                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
    }
}
