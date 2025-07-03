package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.L_FB_revisionFeedbackDTO;
import com.uth.quizclear.repository.L_FB_revisionCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class L_FB_revisionFeedbackService {

    @Autowired
    private L_FB_revisionCommentRepository feedbackRepository;

    public List<L_FB_revisionFeedbackDTO> getApprovedQuestionsWithFeedback(Long lecturerId) {
        return feedbackRepository.findApprovedQuestionsWithCommentsByLecturer(lecturerId);
    }
}