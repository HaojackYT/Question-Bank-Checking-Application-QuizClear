package com.uth.quizclear.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.QuesReportDTO;
import com.uth.quizclear.model.dto.SummaryReportDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.SummaryQuestion;
import com.uth.quizclear.model.entity.SummaryReport;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.FeedbackStatus;
import com.uth.quizclear.model.enums.SumStatus;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.SummaryQuesRepository;
import com.uth.quizclear.repository.SummaryRepository;
import com.uth.quizclear.repository.UserRepository;

@Service
public class SummaryService {

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SummaryQuesRepository summaryQuesRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // Lấy danh sách Report (DEBUG)
    public List<SummaryReport> getAllRps() {
        return summaryRepository.findAll();
    }

    // Lấy Report theo ID
    public Optional<SummaryReportDTO> getReportDetail(Long id) {
        System.out.println("📌 [Service] Finding report with ID: " + id);

        return summaryRepository.findReportDetail(id)
                .map(summary -> {
                    System.out.println("✅ [Service] Found SummaryReport: ID = " + summary.getSumId());

                    SummaryReportDTO dto = new SummaryReportDTO();

                    // === Basic mapping ===
                    dto.setSumId(summary.getSumId());
                    dto.setTitle(summary.getTitle());
                    dto.setDescription(summary.getDescription());
                    dto.setTotalQuestions(summary.getTotalQuestions());
                    dto.setStatus(summary.getStatus().name());
                    dto.setFeedbackStatus(summary.getFeedbackStatus().name());

                    // === Format createdAt ===
                    if (summary.getCreatedAt() != null) {
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        dto.setCreatedAt(summary.getCreatedAt().format(fmt));
                    }

                    // === Mapping AssignedBy ===
                    User assignedBy = summary.getAssignedBy();
                    if (assignedBy != null) {
                        UserBasicDTO assignedByDTO = new UserBasicDTO(
                                assignedBy.getUserIdLong(),
                                assignedBy.getFullName(),
                                assignedBy.getDepartment()
                        );
                        dto.setAssignedBy(assignedByDTO);
                        System.out.println("🔧 Set AssignedBy: " + assignedByDTO.getFullName());
                    }

                    // === Mapping AssignedTo ===
                    User assignedTo = summary.getAssignedTo();
                    if (assignedTo != null) {
                        UserBasicDTO assignedToDTO = new UserBasicDTO(
                                assignedTo.getUserIdLong(),
                                assignedTo.getFullName(),
                                assignedBy.getDepartment()
                        );
                        dto.setAssignedTo(assignedToDTO);
                        System.out.println("🔧 Set AssignedTo: " + assignedToDTO.getFullName());
                    }

                    // === Mapping Questions ===
                    List<QuesReportDTO> quesDTOs = summary.getSummaryQuestions()
                            .stream()
                            .map(summaryQuestion -> {
                                var question = summaryQuestion.getQuestion();
                                return new QuesReportDTO(
                                        question.getQuestionId(),
                                        question.getCreatedBy().getFullName(),
                                        question.getDifficultyLevel().name(),
                                        question.getCreatedAt(),
                                        question.getStatus().name()
                                );
                            })
                            .collect(Collectors.toList());

                    dto.setQuestions(quesDTOs);
                    System.out.println("✅ Finished mapping questions. Total: " + quesDTOs.size());

                    return dto;
                });
    }

    // Lấy danh sách câu hỏi phù hợp
    public List<QuesReportDTO> getApprovedQuestionReports() {
        List<Question> questions = summaryRepository.findApprovedQuestions();

        questions.forEach(q -> System.out.println(
                q.getQuestionId() + ", "
                + (q.getCreatedBy() != null ? q.getCreatedBy().getFullName() : "null") + ", "
                + (q.getDifficultyLevel() != null ? q.getDifficultyLevel().name() : "null") + ", "
                + q.getCreatedAt() + ", "
                + q.getStatus()
        ));

        return questions.stream()
                .map(q -> new QuesReportDTO(
                q.getQuestionId(),
                q.getCreatedBy().getFullName(),
                q.getDifficultyLevel().name(),
                q.getCreatedAt(),
                q.getStatus().toString()
        ))
                .collect(Collectors.toList());
    }

    // Lấy danh sách cấp trên
    public List<UserBasicDTO> getRepient() {
        List<UserRole> roles = Arrays.asList(UserRole.RD, UserRole.HOD);
        List<User> recipients = summaryRepository.findRecipient(roles);

        // Map User -> UserBasicDTO (bạn có thể chọn constructor phù hợp)
        return recipients.stream()
                .map(user -> new UserBasicDTO(
                user.getUserIdLong(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().getValue()
        ))
                .collect(Collectors.toList());
    }

    // Tạo báo cáo
    public SummaryReport createSummary(SummaryReportDTO dto, Long userId) throws Exception {
        SummaryReport summary = new SummaryReport();

        summary.setTitle(dto.getTitle());
        summary.setDescription(dto.getDescription());
        summary.setTotalQuestions(dto.getTotalQuestions());
        summary.setCreatedAt(LocalDateTime.now());

        User assignedTo = userRepository.findById(dto.getAssignedTo().getUserId())
                .orElseThrow(() -> new Exception("AssignedTo User not found"));
        User assignedBy = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("AssignedBy User not found"));

        summary.setAssignedTo(assignedTo);
        summary.setAssignedBy(assignedBy);

        summary.setFeedbackStatus(
                dto.getFeedbackStatus() != null ? FeedbackStatus.valueOf(dto.getFeedbackStatus()) : FeedbackStatus.NOT_RECEIVED
        );
        summary.setStatus(
                dto.getStatus() != null ? SumStatus.valueOf(dto.getStatus()) : SumStatus.PENDING
        );

        summary = summaryRepository.save(summary);

        if (dto.getQuestions() != null) {
            for (QuesReportDTO quesDto : dto.getQuestions()) {
                Long qId = quesDto.getId();
                Question question = questionRepository.findById(qId)
                        .orElseThrow(() -> new Exception("Question id " + qId + " not found"));

                SummaryQuestion sq = new SummaryQuestion();
                sq.setSummaryReport(summary);
                sq.setQuestion(question);

                summaryQuesRepository.save(sq);
            }
        }

        return summary;
    }

    // Edit báo cáo
    public SummaryReport updateSummary(Long summaryId, SummaryReportDTO dto, Long userId) throws Exception {
        System.out.println("📥 [Service] Updating Summary ID: " + summaryId);
        SummaryReport summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new Exception("SummaryReport not found with id " + summaryId));

        System.out.println("✅ Found SummaryReport with ID: " + summary.getSumId());

        summary.setTitle(dto.getTitle());
        summary.setDescription(dto.getDescription());
        summary.setTotalQuestions(dto.getTotalQuestions());
        summary.setCreatedAt(LocalDateTime.now());

        User assignedTo = userRepository.findById(dto.getAssignedTo().getUserId())
                .orElseThrow(() -> new Exception("AssignedTo User not found"));
        User assignedBy = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("AssignedBy User not found"));

        summary.setAssignedTo(assignedTo);
        summary.setAssignedBy(assignedBy);

        summary.setFeedbackStatus(
                dto.getFeedbackStatus() != null ? FeedbackStatus.valueOf(dto.getFeedbackStatus()) : summary.getFeedbackStatus()
        );
        summary.setStatus(
                dto.getStatus() != null ? SumStatus.valueOf(dto.getStatus()) : summary.getStatus()
        );

        summary = summaryRepository.save(summary);

        // Xóa hết câu hỏi cũ liên kết với báo cáo này
        System.out.println("📌 Trước khi xóa liên kết câu hỏi cũ");
        summaryQuesRepository.deleteBySummaryReport(summary);
        System.out.println("✅ Đã xóa xong");

        
        if (dto.getQuestions() != null) {
            System.out.println("🔁 Updating questions. Count: " + dto.getQuestions().size());
            for (QuesReportDTO quesDto : dto.getQuestions()) {
                Long qId = quesDto.getId();
                Question question = questionRepository.findById(qId)
                        .orElseThrow(() -> new Exception("Question id " + qId + " not found"));

                SummaryQuestion sq = new SummaryQuestion();
                sq.setSummaryReport(summary);
                sq.setQuestion(question);

                summaryQuesRepository.save(sq);
            }
        }

        return summary;
    }

}
