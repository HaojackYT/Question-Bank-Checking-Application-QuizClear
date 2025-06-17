package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.QuestionDTO;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionsRepository;

    public List<QuestionDTO> getQuestionsByTaskId(Long taskId) {
        List<Question> questions = questionsRepository.findByTaskId(taskId);
        return questions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

public void updateQuestionStatus(long questionId, String status) {
    Question question = questionsRepository.findById(questionId)
            .orElseThrow(() -> new RuntimeException("Question not found"));
    try {
        QuestionStatus questionStatus = QuestionStatus.valueOf(status.toUpperCase());
        
        // Cập nhật trường status
        java.lang.reflect.Field statusField = Question.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(question, questionStatus);
        
        // Cập nhật các trường thời gian
        if (questionStatus == QuestionStatus.APPROVED) {
            java.lang.reflect.Field approvedAtField = Question.class.getDeclaredField("approvedAt");
            approvedAtField.setAccessible(true);
            approvedAtField.set(question, LocalDateTime.now());
        } else if (questionStatus == QuestionStatus.REJECTED) {
            java.lang.reflect.Field reviewedAtField = Question.class.getDeclaredField("reviewedAt");
            reviewedAtField.setAccessible(true);
            reviewedAtField.set(question, LocalDateTime.now());
        }
        
        questionsRepository.save(question);
    } catch (Exception e) {
        throw new RuntimeException("Error updating question status: " + e.getMessage(), e);
    }
}




private QuestionDTO mapToDTO(Question question) {
    try {
        // Sử dụng reflection để truy cập các trường
        java.lang.reflect.Field questionIdField = Question.class.getDeclaredField("questionId");
        java.lang.reflect.Field taskIdField = Question.class.getDeclaredField("taskId");
        java.lang.reflect.Field contentField = Question.class.getDeclaredField("content");
        java.lang.reflect.Field difficultyLevelField = Question.class.getDeclaredField("difficultyLevel");
        java.lang.reflect.Field answerKeyField = Question.class.getDeclaredField("answerKey");
        java.lang.reflect.Field answerF1Field = Question.class.getDeclaredField("answerF1");
        java.lang.reflect.Field answerF2Field = Question.class.getDeclaredField("answerF2");
        java.lang.reflect.Field answerF3Field = Question.class.getDeclaredField("answerF3");
        java.lang.reflect.Field explanationField = Question.class.getDeclaredField("explanation");
        java.lang.reflect.Field statusField = Question.class.getDeclaredField("status");
        
        // Cho phép truy cập vào các trường private
        questionIdField.setAccessible(true);
        taskIdField.setAccessible(true);
        contentField.setAccessible(true);
        difficultyLevelField.setAccessible(true);
        answerKeyField.setAccessible(true);
        answerF1Field.setAccessible(true);
        answerF2Field.setAccessible(true);
        answerF3Field.setAccessible(true);
        explanationField.setAccessible(true);
        statusField.setAccessible(true);
        
        // Lấy giá trị của các trường
        Long questionId = (Long) questionIdField.get(question);
        Long taskId = (Long) taskIdField.get(question);
        String content = (String) contentField.get(question);
        com.uth.quizclear.model.enums.DifficultyLevel difficultyLevel = 
            (com.uth.quizclear.model.enums.DifficultyLevel) difficultyLevelField.get(question);
        String answerKey = (String) answerKeyField.get(question);
        String answerF1 = (String) answerF1Field.get(question);
        String answerF2 = (String) answerF2Field.get(question);
        String answerF3 = (String) answerF3Field.get(question);
        String explanation = (String) explanationField.get(question);
        com.uth.quizclear.model.enums.QuestionStatus status = 
            (com.uth.quizclear.model.enums.QuestionStatus) statusField.get(question);
        
        // Tạo và trả về DTO
        return new QuestionDTO(
                questionId,
                taskId,
                content,
                difficultyLevel,
                answerKey,
                answerF1,
                answerF2,
                answerF3,
                explanation,
                status
        );
    } catch (Exception e) {
        throw new RuntimeException("Error mapping Question to DTO: " + e.getMessage(), e);
    }
}

}