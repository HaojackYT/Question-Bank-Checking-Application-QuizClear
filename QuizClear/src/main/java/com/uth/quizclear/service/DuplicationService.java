package com.uth.quizclear.service;

import com.uth.quizclear.model.*;
import com.uth.quizclear.repository.DuplicateDetectionRepository;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class DuplicationService {

    @Autowired
    private DuplicateDetectionRepository duplicationRepository;

    @Autowired
    private UserRepository userRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Lấy danh sách tất cả các phát hiện trùng lặp, ánh xạ sang DTO.
     * Cần @Transactional để đảm bảo các mối quan hệ LAZY được load.
     */
    @Transactional(readOnly = true)
    public List<DuplicateDetectionDTO> getAllDetections() {
        List<DuplicateDetection> detections = duplicationRepository.findAll();
        return detections.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách các tùy chọn duy nhất cho bộ lọc (môn học và người gửi).
     *
     * @return Map chứa danh sách các môn học và người gửi.
     */
    @Transactional(readOnly = true)
    public Map<String, List<String>> getDistinctFilterOptions() {
        List<DuplicateDetection> allDetections = duplicationRepository.findAll();
        
        Set<String> subjects = new HashSet<>();
        Set<String> submitters = new HashSet<>();

        for (DuplicateDetection detection : allDetections) {
            if (detection.getNewQuestion() != null && detection.getNewQuestion().getCourse() != null) {
                subjects.add(detection.getNewQuestion().getCourse().getCourseName());
            }
            if (detection.getNewQuestion() != null && detection.getNewQuestion().getCreator() != null) {
                submitters.add(detection.getNewQuestion().getCreator().getFullName());
            }
        }
        
        return Map.of(
            "subjects", subjects.stream().sorted().collect(Collectors.toList()),
            "submitters", submitters.stream().sorted().collect(Collectors.toList())
        );
    }

    /**
     * Lấy chi tiết một phát hiện trùng lặp theo ID, ánh xạ sang DTO.
     */
    @Transactional(readOnly = true)
    public DuplicateDetectionDTO getDuplicationDetailsById(Integer id) {
        Optional<DuplicateDetection> optionalDetection = duplicationRepository.findById(id);

        if (optionalDetection.isEmpty()) {
            return null; // Hoặc throw một exception NotFound
        }

        DuplicateDetection detection = optionalDetection.get();
        return mapToDto(detection);
    }

    /**
     * Xử lý một phát hiện trùng lặp (chấp nhận, từ chối, gửi lại).
     *
     * @param id ID của phát hiện trùng lặp.
     * @param action Hành động xử lý ("accept", "reject", "send_back").
     * @param feedback Góp ý/ghi chú.
     * @param processorId ID của người xử lý.
     */
    @Transactional
    public void processDetection(Integer id, String action, String feedback, Integer processorId) throws IllegalArgumentException { // THÊM THROW Ở ĐÂY
        Optional<DuplicateDetection> optionalDetection = duplicationRepository.findById(id);

        if (optionalDetection.isEmpty()) {
            throw new RuntimeException("Duplicate detection not found with ID: " + id);
        }

        DuplicateDetection detection = optionalDetection.get();

        // Cập nhật trạng thái và hành động
        detection.setAction(action);
        detection.setFeedback(feedback);
        detection.setProcessedAt(LocalDateTime.now());

        // Cập nhật trạng thái dựa trên hành động
        switch (action) {
            case "accept":
                detection.setStatus("accepted");
                // Logic bổ sung nếu cần (ví dụ: cập nhật trạng thái câu hỏi trong bảng questions)
                break;
            case "reject":
                detection.setStatus("rejected");
                break;
            case "send_back":
                detection.setStatus("sent_back");
                break;
            default:
                // Xử lý hành động không hợp lệ
                throw new IllegalArgumentException("Invalid action: " + action);
        }

        // Tìm User entity đầy đủ từ processorId
        if (processorId != null) {
            userRepository.findById(processorId).ifPresent(detection::setProcessedBy);
        }

        duplicationRepository.save(detection);
    }

    /**
     * Hàm helper để ánh xạ từ DuplicateDetection Entity sang DuplicateDetectionDTO.
     */
    private DuplicateDetectionDTO mapToDto(DuplicateDetection detection) {
        // Tạo QuestionDetailDTO cho New Question
        QuestionDetailDTO newQDto = null;
        if (detection.getNewQuestion() != null) {
            newQDto = new QuestionDetailDTO(
                detection.getNewQuestion().getQuestionId(),
                detection.getNewQuestion().getContent(),
                detection.getNewQuestion().getCourse() != null ? detection.getNewQuestion().getCourse().getCourseName() : null,
                detection.getNewQuestion().getClo() != null ? detection.getNewQuestion().getClo().getCloName() : null,
                detection.getNewQuestion().getDifficultyLevel(),
                detection.getNewQuestion().getCreator() != null ? detection.getNewQuestion().getCreator().getFullName() : null,
                detection.getNewQuestion().getCreatedAt() != null ? detection.getNewQuestion().getCreatedAt().format(DATE_FORMATTER) : null
            );
        }

        // Tạo QuestionDetailDTO cho Similar Question
        QuestionDetailDTO similarQDto = null;
        if (detection.getSimilarQuestion() != null) {
            similarQDto = new QuestionDetailDTO(
                detection.getSimilarQuestion().getQuestionId(),
                detection.getSimilarQuestion().getContent(),
                detection.getSimilarQuestion().getCourse() != null ? detection.getSimilarQuestion().getCourse().getCourseName() : null,
                detection.getSimilarQuestion().getClo() != null ? detection.getSimilarQuestion().getClo().getCloName() : null,
                detection.getSimilarQuestion().getDifficultyLevel(),
                detection.getSimilarQuestion().getCreator() != null ? detection.getSimilarQuestion().getCreator().getFullName() : null,
                detection.getSimilarQuestion().getCreatedAt() != null ? detection.getSimilarQuestion().getCreatedAt().format(DATE_FORMATTER) : null
            );
        }

        // Xử lý trường feedback để tách ra aiAnalysisText và aiRecommendation
        String feedback = detection.getFeedback();
        String aiAnalysisText = "No AI analysis available.";
        String aiRecommendation = "No AI recommendation available.";

        if (feedback != null && !feedback.isEmpty()) {
            String analysisPrefix = "AI Analysis:";
            String recommendationPrefix = "AI Recommendation:";

            int analysisStart = feedback.indexOf(analysisPrefix);
            int recommendationStart = feedback.indexOf(recommendationPrefix);

            if (analysisStart != -1) {
                analysisStart += analysisPrefix.length();
                if (recommendationStart != -1 && recommendationStart > analysisStart) {
                    aiAnalysisText = feedback.substring(analysisStart, recommendationStart).trim();
                } else {
                    aiAnalysisText = feedback.substring(analysisStart).trim();
                }
            }

            if (recommendationStart != -1) {
                recommendationStart += recommendationPrefix.length();
                aiRecommendation = feedback.substring(recommendationStart).trim();
            }
        }

        // Trả về DuplicateDetectionDTO với đầy đủ dữ liệu
        return new DuplicateDetectionDTO(
            detection.getDetectionId(),
            newQDto,
            similarQDto,
            detection.getSimilarityScore(),
            detection.getStatus(),
            aiAnalysisText,
            aiRecommendation
        );
    }
}