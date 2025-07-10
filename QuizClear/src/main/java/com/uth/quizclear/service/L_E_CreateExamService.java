package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.L_E_CreateExamRequestDTO;
import com.uth.quizclear.model.dto.L_E_CreateExamQuestionDTO;
import com.uth.quizclear.model.dto.L_E_CreateExamQuestionDetailDTO;
import com.uth.quizclear.model.dto.L_E_CreateExamSaveDTO;
import com.uth.quizclear.model.entity.*;
import com.uth.quizclear.model.enums.ExamStatus;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.repository.L_E_CreateExamRepository;
import com.uth.quizclear.repository.L_E_CreateExamQuestionRepository;
import com.uth.quizclear.repository.L_E_CreateExamCloRepository;
import com.uth.quizclear.repository.L_E_CreateExamExamRepository;
import com.uth.quizclear.repository.L_E_CreateExamExamQuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class L_E_CreateExamService {

    private static final Logger logger = LoggerFactory.getLogger(L_E_CreateExamService.class);

    @Autowired
    private L_E_CreateExamRepository taskRepository;

    @Autowired
    private L_E_CreateExamQuestionRepository questionRepository;

    @Autowired
    private L_E_CreateExamCloRepository cloRepository;

    @Autowired
    private L_E_CreateExamExamRepository examRepository;

    @Autowired
    private L_E_CreateExamExamQuestionRepository examQuestionRepository;

    public L_E_CreateExamRequestDTO getTaskDetails(Long taskId) {
        logger.debug("Fetching task details for taskId: {}", taskId);
        Optional<Tasks> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Tasks task = optionalTask.get();
            Plan plan = task.getPlan();
            User assignedBy = task.getAssignedBy();
            Course course = task.getCourse();
            String requester = assignedBy != null ? assignedBy.getFullName() : "Unknown";
            String timeExam = task.getDurationMinutes() != null ? task.getDurationMinutes() + ":00" : "90:00";
            String subject = course != null ? course.getCourseName() : "Unknown";
            String deadline = task.getDueDate() != null ? task.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A";
            
            int remember = plan != null && plan.getTotalRecognition() != null ? plan.getTotalRecognition() : 0;
            int understand = plan != null && plan.getTotalComprehension() != null ? plan.getTotalComprehension() : 0;
            int applyBasic = plan != null && plan.getTotalBasicApplication() != null ? plan.getTotalBasicApplication() : 0;
            int applyAdvanced = plan != null && plan.getTotalAdvancedApplication() != null ? plan.getTotalAdvancedApplication() : 0;

            return new L_E_CreateExamRequestDTO(requester, timeExam, subject, deadline, remember, understand, applyBasic, applyAdvanced);
        }
        logger.error("Task not found for taskId: {}", taskId);
        return null;
    }

    public List<L_E_CreateExamQuestionDTO> getApprovedQuestionsByTaskId(Long taskId) {
        logger.debug("Fetching approved questions for taskId: {}", taskId);
        Optional<Tasks> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Tasks task = optionalTask.get();
            Course course = task.getCourse();
            Long courseId = course != null ? course.getCourseId() : null;
            if (courseId != null) {
                List<Question> questions = questionRepository.findApprovedQuestionsByCourseId(courseId);
                return questions.stream().map(q -> {
                    String difficultyTag = mapDifficultyToTag(q.getDifficultyLevel().getValue());
                    CLO clo = q.getClo();
                    String cloTag = clo != null ? clo.getCloCode().split("\\.")[0] : "Unknown";
                    return new L_E_CreateExamQuestionDTO(q.getQuestionId(), q.getContent(), difficultyTag, cloTag);
                }).collect(Collectors.toList());
            }
        }
        logger.error("Task or course not found for taskId: {}", taskId);
        return new ArrayList<>();
    }

    public List<L_E_CreateExamQuestionDetailDTO> getQuestionsByIds(List<Long> questionIds) {
        logger.debug("Fetching questions by IDs: {}", questionIds);
        List<Question> questions = questionRepository.findAllById(questionIds);
        return questions.stream().map(q -> {
            String difficultyTag = mapDifficultyToTag(q.getDifficultyLevel().getValue());
            CLO clo = q.getClo();
            String cloTag = clo != null ? clo.getCloCode().split("\\.")[0] : "Unknown";
            return new L_E_CreateExamQuestionDetailDTO(
                q.getQuestionId(),
                q.getContent(),
                difficultyTag,
                cloTag,
                q.getAnswerKey(),
                q.getAnswerF1(),
                q.getAnswerF2(),
                q.getAnswerF3()
            );
        }).collect(Collectors.toList());
    }

    public List<String> getCloCodesByTaskId(Long taskId) {
        logger.debug("Fetching CLO codes for taskId: {}", taskId);
        Optional<Tasks> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Tasks task = optionalTask.get();
            Course course = task.getCourse();
            Long courseId = course != null ? course.getCourseId() : null;
            if (courseId != null) {
                List<CLO> clos = cloRepository.findByCourseId(courseId);
                return clos.stream()
                        .map(clo -> clo.getCloCode().split("\\.")[0])
                        .distinct()
                        .collect(Collectors.toList());
            }
        }
        logger.error("Task or course not found for taskId: {}", taskId);
        return new ArrayList<>();
    }

    public Long saveDraft(L_E_CreateExamSaveDTO saveDTO, Long userId) {
        logger.debug("Saving draft for taskId: {}, userId: {}", saveDTO.getTaskId(), userId);
        Optional<Tasks> optionalTask = taskRepository.findById(saveDTO.getTaskId());
        if (!optionalTask.isPresent()) {
            logger.error("Task not found for taskId: {}", saveDTO.getTaskId());
            throw new IllegalArgumentException("Task not found");
        }

        Tasks task = optionalTask.get();
        Course course = task.getCourse();
        Plan plan = task.getPlan();
        if (course == null) {
            logger.error("Course not found for taskId: {}", saveDTO.getTaskId());
            throw new IllegalArgumentException("Course not found");
        }

        User createdBy = new User();
        createdBy.setUserId(userId);

        Exam exam = new Exam();
        exam.setExamTitle(saveDTO.getExamTitle());
        exam.setCourse(course);
        exam.setPlan(plan);
        exam.setCreatedBy(createdBy);
        exam.setExamStatus(ExamStatus.DRAFT);
        exam.setDurationMinutes(task.getDurationMinutes() != null ? task.getDurationMinutes() : 90);
        exam.setTotalMarks(BigDecimal.valueOf(saveDTO.getQuestionIds().size()));
        examRepository.save(exam);

        for (int i = 0; i < saveDTO.getQuestionIds().size(); i++) {
            Long questionId = saveDTO.getQuestionIds().get(i);
            Optional<Question> optionalQuestion = questionRepository.findById(questionId);
            if (!optionalQuestion.isPresent()) {
                logger.error("Question not found for questionId: {}", questionId);
                continue; // Bỏ qua nếu câu hỏi không tồn tại
            }
            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.setExam(exam);
            examQuestion.setQuestion(optionalQuestion.get());
            examQuestion.setQuestionOrder(i + 1);
            examQuestion.setMarks(BigDecimal.valueOf(1.0));
            examQuestionRepository.save(examQuestion);
        }

        logger.info("Draft saved successfully for examId: {}", exam.getExamId());
        return exam.getExamId();
    }

    public Long submitExam(L_E_CreateExamSaveDTO saveDTO, Long userId) {
        logger.debug("Submitting exam for taskId: {}, userId: {}", saveDTO.getTaskId(), userId);
        Optional<Tasks> optionalTask = taskRepository.findById(saveDTO.getTaskId());
        if (!optionalTask.isPresent()) {
            logger.error("Task not found for taskId: {}", saveDTO.getTaskId());
            throw new IllegalArgumentException("Task not found");
        }

        Tasks task = optionalTask.get();
        Course course = task.getCourse();
        Plan plan = task.getPlan();
        if (course == null) {
            logger.error("Course not found for taskId: {}", saveDTO.getTaskId());
            throw new IllegalArgumentException("Course not found");
        }

        User createdBy = new User();
        createdBy.setUserId(userId);

        Exam exam = new Exam();
        exam.setExamTitle(saveDTO.getExamTitle());
        exam.setCourse(course);
        exam.setPlan(plan);
        exam.setCreatedBy(createdBy);
        exam.setExamStatus(ExamStatus.FINALIZED);
        exam.setDurationMinutes(task.getDurationMinutes() != null ? task.getDurationMinutes() : 90);
        exam.setTotalMarks(BigDecimal.valueOf(saveDTO.getQuestionIds().size()));
        exam.setSubmittedAt(LocalDateTime.now());
        examRepository.save(exam);

        for (int i = 0; i < saveDTO.getQuestionIds().size(); i++) {
            Long questionId = saveDTO.getQuestionIds().get(i);
            Optional<Question> optionalQuestion = questionRepository.findById(questionId);
            if (!optionalQuestion.isPresent()) {
                logger.error("Question not found for questionId: {}", questionId);
                continue; // Bỏ qua nếu câu hỏi không tồn tại
            }
            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.setExam(exam);
            examQuestion.setQuestion(optionalQuestion.get());
            examQuestion.setQuestionOrder(i + 1);
            examQuestion.setMarks(BigDecimal.valueOf(1.0));
            examQuestionRepository.save(examQuestion);
        }

        task.setStatus(TaskStatus.completed);
        task.setCompletedAt(LocalDateTime.now());
        taskRepository.save(task);

        logger.info("Exam submitted successfully for examId: {}", exam.getExamId());
        return exam.getExamId();
    }

    private String mapDifficultyToTag(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "recognition":
                return "remember";
            case "comprehension":
                return "understand";
            case "basic application":
                return "apply-basic";
            case "advanced application":
                return "apply-advance";
            default:
                return "unknown";
        }
    }
}