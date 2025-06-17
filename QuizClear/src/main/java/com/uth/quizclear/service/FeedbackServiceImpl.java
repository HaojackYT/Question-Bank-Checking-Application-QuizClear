package com.uth.quizclear.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.FeedbackDTO;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.repository.ExamRepository;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private ExamRepository examRepository;

    @Override
    public List<FeedbackDTO> getFeedbackByExamId(Long examId) {
        Exam exam = examRepository.findById(examId).orElse(null);
        List<FeedbackDTO> feedbackList = new ArrayList<>();
        if (exam != null && exam.getFeedback() != null && !exam.getFeedback().isBlank()) {
            // Bạn có thể mở rộng để lấy thêm thông tin người gửi feedback nếu có
            FeedbackDTO dto = new FeedbackDTO();
            dto.setId(1L);
            dto.setAuthorName(exam.getReviewedBy() != null ? exam.getReviewedBy().getFullName() : "Reviewer");
            dto.setAvatarLetter(dto.getAuthorName().substring(0, 1).toUpperCase());
            dto.setText(exam.getFeedback());
            dto.setDateAgo(exam.getReviewedAt() != null
                    ? exam.getReviewedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    : "");
            dto.setLikes(0); // Nếu có tính năng like thì lấy số like, không thì để 0
            feedbackList.add(dto);
        }
        return feedbackList;
    }
}