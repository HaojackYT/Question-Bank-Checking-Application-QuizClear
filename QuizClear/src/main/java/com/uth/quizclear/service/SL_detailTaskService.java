package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.QuestionDTO;
import com.uth.quizclear.model.dto.SL_detailTaskDTO;
import com.uth.quizclear.model.entity.Comment;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.commentType;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.repository.CommentRepository;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.SL_detailTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SL_detailTaskService {

    @Autowired
    private SL_detailTaskRepository taskRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CommentRepository commentRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");

    public SL_detailTaskDTO getTaskDetails(Integer taskId) {
        Tasks task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        SL_detailTaskDTO dto = new SL_detailTaskDTO();
        dto.setTaskId(task.getTaskId());
        dto.setLecturerName(task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : "Unknown");
        dto.setDepartment(task.getAssignedTo() != null ? task.getAssignedTo().getDepartment() : "Unknown");
        dto.setTotalQuestions(task.getTotalQuestions());
        dto.setCourseName(task.getCourse() != null ? task.getCourse().getCourseName() : "Unknown");
        dto.setCloCode("CLOs"); // Giả sử lấy từ bảng clos, có thể cần join thêm
        dto.setDifficultyLevel("Multiple Levels"); // Có thể lấy từ questions
        dto.setStatusFromTaskStatus(task.getStatus());
        dto.setAssignedDateFromLocalDateTime(task.getAssignedAt());
        dto.setDueDateFromLocalDateTime(task.getDueDate());

        List<Question> questions = taskRepository.findQuestionsByTaskId(taskId);
        List<QuestionDTO> questionDTOs = questions.stream().map(q -> {
            QuestionDTO qDto = new QuestionDTO();
            qDto.setQuestionId(q.getQuestionId());
            qDto.setTaskId(q.getTaskId());
            qDto.setContent(q.getContent());
            qDto.setDifficultyLevel(q.getDifficultyLevel());
            qDto.setAnswerKey(q.getAnswerKey());
            qDto.setAnswerF1(q.getAnswerF1());
            qDto.setAnswerF2(q.getAnswerF2());
            qDto.setAnswerF3(q.getAnswerF3());
            qDto.setExplanation(q.getExplanation());
            qDto.setOptions(formatOptions(q));
            qDto.setUpdatedDateFromLocalDateTime(q.getUpdatedAt());
            qDto.setDisplayStatusFromStatus(q.getStatus());
            return qDto;
        }).collect(Collectors.toList());
        dto.setQuestions(questionDTOs);

        return dto;
    }

    public void approveQuestion(Long questionId, Integer approverId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        question.setStatus(QuestionStatus.APPROVED);
        question.setApprovedAt(LocalDateTime.now());
        question.setApprover(new com.uth.quizclear.model.entity.User(approverId.longValue()));
        questionRepository.save(question);
    }

    public void declineQuestion(Long questionId, String reason, Integer commenterId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        question.setStatus(QuestionStatus.REJECTED);
        question.setReviewedAt(LocalDateTime.now());
        question.setFeedback(reason);
        questionRepository.save(question);

        Comment comment = new Comment();
        comment.setEntityType(commentType.QUESTION); // Ghi chú: Có thể thay bằng EntityType.QUESTION để đồng bộ với Comment.java
        comment.setEntityId(questionId.intValue());
        comment.setCommenterId(commenterId);
        comment.setContent(reason);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public void addComment(Long questionId, String comment, Integer commenterId) {
        Comment c = new Comment();
        c.setEntityType(commentType.QUESTION); // Ghi chú: Có thể thay bằng EntityType.QUESTION để đồng bộ với Comment.java
        c.setEntityId(questionId.intValue());
        c.setCommenterId(commenterId);
        c.setContent(comment);
        c.setCreatedAt(LocalDateTime.now());
        commentRepository.save(c);
    }

    private String formatOptions(Question q) {
        StringBuilder options = new StringBuilder();
        if (q.getAnswerF1() != null) options.append("A. ").append(q.getAnswerF1()).append("<br>");
        if (q.getAnswerF2() != null) options.append("B. ").append(q.getAnswerF2()).append("<br>");
        if (q.getAnswerF3() != null) options.append("C. ").append(q.getAnswerF3()).append("<br>");
        return options.toString();
    }
}
