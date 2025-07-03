package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.L_FB_revisionEditQuestionDTO;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.repository.L_FB_revisionEditQuestionRepository;
import com.uth.quizclear.repository.QuestionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.uth.quizclear.model.enums.QuestionStatus;

@Service
public class L_FB_revisionEditQuestionService {

    private static final Logger logger = LoggerFactory.getLogger(L_FB_revisionEditQuestionService.class);

    @Autowired
    private L_FB_revisionEditQuestionRepository editQuestionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public Optional<L_FB_revisionEditQuestionDTO> getQuestionWithFeedback(Long questionId) {
        logger.info("Fetching question with ID: {}", questionId);
        Optional<L_FB_revisionEditQuestionDTO> questionOpt = editQuestionRepository.findQuestionWithFeedbackById(questionId);
        if (questionOpt.isEmpty()) {
            logger.warn("Question not found for ID: {}", questionId);
        }
        return questionOpt;
    }

    public String updateQuestion(Long questionId, L_FB_revisionEditQuestionDTO updatedQuestion, Long lecturerId) {
        logger.info("Updating question draft for ID: {}, Lecturer ID: {}", questionId, lecturerId);
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isEmpty()) {
            logger.error("Question not found for ID: {}", questionId);
            return "Question not found";
        }

        Question question = questionOpt.get();
        long createdById = question.getCreatedBy() != null ? question.getCreatedBy().getUserId() : 0;
        if (createdById != lecturerId) {
            logger.error("Permission denied: Question ID {} has created_by ID: {}, but lecturer ID is: {}",
                    questionId, createdById, lecturerId);
            return "Permission denied: You do not have permission to edit this question";
        }

        question.setContent(updatedQuestion.getQuestionContent());
        question.setAnswerKey(updatedQuestion.getCorrectAnswer());
        question.setAnswerF1(updatedQuestion.getIncorrectAnswer1());
        question.setAnswerF2(updatedQuestion.getIncorrectAnswer2());
        question.setAnswerF3(updatedQuestion.getIncorrectAnswer3());
        question.setExplanation(updatedQuestion.getExplanation());
        question.setUpdatedAt(LocalDateTime.now());

        // Convert difficultyLevel to enum
        try {
            String difficulty = updatedQuestion.getDifficultyLevel().toLowerCase();
            switch (difficulty) {
                case "remember":
                    question.setDifficultyLevel(DifficultyLevel.RECOGNITION);
                    break;
                case "comprehension":
                    question.setDifficultyLevel(DifficultyLevel.COMPREHENSION);
                    break;
                case "basic_application":
                    question.setDifficultyLevel(DifficultyLevel.BASIC_APPLICATION);
                    break;
                case "advanced_application":
                    question.setDifficultyLevel(DifficultyLevel.ADVANCED_APPLICATION);
                    break;
                default:
                    logger.error("Invalid difficultyLevel: {}", updatedQuestion.getDifficultyLevel());
                    return "Invalid difficulty level: " + updatedQuestion.getDifficultyLevel();
            }
        } catch (Exception e) {
            logger.error("Failed to parse difficultyLevel: {}. Error: {}", updatedQuestion.getDifficultyLevel(), e.getMessage());
            return "Failed to parse difficulty level: " + e.getMessage();
        }

        questionRepository.save(question);
        logger.info("Question draft updated successfully for ID: {}", questionId);
        return null; // Success
    }

    public String submitQuestion(Long questionId, L_FB_revisionEditQuestionDTO updatedQuestion, Long lecturerId) {
        logger.info("Submitting question for ID: {}, Lecturer ID: {}", questionId, lecturerId);
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isEmpty()) {
            logger.error("Question not found for ID: {}", questionId);
            return "Question not found";
        }

        Question question = questionOpt.get();
        Long createdById = question.getCreatedBy() != null ? Long.valueOf(question.getCreatedBy().getUserId()) : null;
        if (createdById == null || !createdById.equals(lecturerId)) {
            logger.error("Permission denied: Question ID {} has created_by ID: {}, but lecturer ID is: {}",
                    questionId, createdById, lecturerId);
            return "Permission denied: You do not have permission to submit this question";
        }

        question.setContent(updatedQuestion.getQuestionContent());
        question.setAnswerKey(updatedQuestion.getCorrectAnswer());
        question.setAnswerF1(updatedQuestion.getIncorrectAnswer1());
        question.setAnswerF2(updatedQuestion.getIncorrectAnswer2());
        question.setAnswerF3(updatedQuestion.getIncorrectAnswer3());
        question.setExplanation(updatedQuestion.getExplanation());
        question.setStatus(QuestionStatus.SUBMITTED);
        question.setSubmittedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());

        // Convert difficultyLevel to enum
        try {
            String difficulty = updatedQuestion.getDifficultyLevel().toLowerCase();
            switch (difficulty) {
                case "remember":
                    question.setDifficultyLevel(DifficultyLevel.RECOGNITION);
                    break;
                case "comprehension":
                    question.setDifficultyLevel(DifficultyLevel.COMPREHENSION);
                    break;
                case "basic_application":
                    question.setDifficultyLevel(DifficultyLevel.BASIC_APPLICATION);
                    break;
                case "advanced_application":
                    question.setDifficultyLevel(DifficultyLevel.ADVANCED_APPLICATION);
                    break;
                default:
                    logger.error("Invalid difficultyLevel: {}", updatedQuestion.getDifficultyLevel());
                    return "Invalid difficulty level: " + updatedQuestion.getDifficultyLevel();
            }
        } catch (Exception e) {
            logger.error("Failed to parse difficultyLevel: {}. Error: {}", updatedQuestion.getDifficultyLevel(), e.getMessage());
            return "Failed to parse difficulty level: " + e.getMessage();
        }

        questionRepository.save(question);
        logger.info("Question submitted successfully for ID: {}", questionId);
        return null; // Success
    }
}
