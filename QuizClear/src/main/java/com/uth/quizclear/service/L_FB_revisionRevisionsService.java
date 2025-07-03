package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.L_FB_revisionRevisionsDTO;
import com.uth.quizclear.repository.L_FB_revisionRevisionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class L_FB_revisionRevisionsService {

    @Autowired
    private L_FB_revisionRevisionsRepository revisionsRepository;

    public List<L_FB_revisionRevisionsDTO> getRejectedQuestionsWithFeedback(Long lecturerId) {
        return revisionsRepository.findRejectedQuestionsWithCommentsByLecturer(lecturerId);
    }
}