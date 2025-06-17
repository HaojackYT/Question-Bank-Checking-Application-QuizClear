package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.FeedbackDTO;
import java.util.List;

public interface FeedbackService {
    List<FeedbackDTO> getFeedbackByExamId(Long examId);
}
