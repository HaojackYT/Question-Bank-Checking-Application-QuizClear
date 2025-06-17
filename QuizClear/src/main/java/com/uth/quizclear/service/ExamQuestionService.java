package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.ExamQuestionDTO;
import java.util.List;

public interface ExamQuestionService {
    List<ExamQuestionDTO> getQuestionsByExamId(Long examId);
}