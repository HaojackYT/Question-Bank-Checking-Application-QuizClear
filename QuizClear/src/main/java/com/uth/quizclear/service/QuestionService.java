package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.QuestionDTO;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<Question> findSubmittedQuestions(Pageable pageable) {
        return questionRepository.findAll(
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("status"), QuestionStatus.SUBMITTED),
                pageable
        );
    }

    @Transactional
    public void approveQuestion(Long questionId, Long reviewerId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        if (question.getStatus() != QuestionStatus.SUBMITTED) {
            throw new IllegalStateException("Only questions with SUBMITTED status can be approved.");
        }

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found with id: " + reviewerId));

        question.setStatus(QuestionStatus.APPROVED);
        question.setReviewedAt(LocalDateTime.now());
        question.setApprovedAt(LocalDateTime.now());
        question.setReviewer(reviewer);
        question.setApprover(reviewer);

        questionRepository.save(question);
    }

    @Transactional
    public void rejectQuestion(Long questionId, Long reviewerId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        if (question.getStatus() != QuestionStatus.SUBMITTED) {
            throw new IllegalStateException("Only questions with SUBMITTED status can be rejected.");
        }

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found with id: " + reviewerId));

        question.setStatus(QuestionStatus.REJECTED);
        question.setReviewedAt(LocalDateTime.now());
        question.setReviewer(reviewer);
        question.setApprover(null);
        question.setApprovedAt(null);

        questionRepository.save(question);
    }

    public List<QuestionDTO> getQuestionsByTaskId(Long taskId) {
        List<Question> questions = questionRepository.findByTaskId(taskId);
        return questions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public void updateQuestionStatus(long questionId, String status) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        try {
            QuestionStatus questionStatus = QuestionStatus.valueOf(status.toUpperCase());

            java.lang.reflect.Field statusField = Question.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(question, questionStatus);

            if (questionStatus == QuestionStatus.APPROVED) {
                java.lang.reflect.Field approvedAtField = Question.class.getDeclaredField("approvedAt");
                approvedAtField.setAccessible(true);
                approvedAtField.set(question, LocalDateTime.now());
            } else if (questionStatus == QuestionStatus.REJECTED) {
                java.lang.reflect.Field reviewedAtField = Question.class.getDeclaredField("reviewedAt");
                reviewedAtField.setAccessible(true);
                reviewedAtField.set(question, LocalDateTime.now());
            }

            questionRepository.save(question);
        } catch (Exception e) {
            throw new RuntimeException("Error updating question status: " + e.getMessage(), e);
        }
    }
    
    private QuestionDTO mapToDTO(Question question) {
        try {
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