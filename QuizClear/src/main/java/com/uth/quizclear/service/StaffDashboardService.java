package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.StaffDashboardDTO;
import com.uth.quizclear.model.dto.TaskDTO;
import com.uth.quizclear.model.dto.DuplicateWarningDTO;
import com.uth.quizclear.model.dto.ChartDataDTO;
import com.uth.quizclear.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StaffDashboardService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private DuplicateDetectionRepository duplicateDetectionRepository;

    public StaffDashboardDTO getDashboardForStaff(Long staffId) {
        StaffDashboardDTO dto = new StaffDashboardDTO();
        // Tổng số môn học
        dto.setTotalSubjects((int) courseRepository.count());
        // Tổng số câu hỏi
        dto.setTotalQuestions((int) questionRepository.count());
        // Số lượng câu hỏi trùng lặp
        dto.setDuplicateQuestions((int) duplicateDetectionRepository.count());
        // Số đề thi đã tạo
        dto.setExamsCreated((int) examRepository.count());
        // Số môn/câu hỏi/đề thi trong tháng này (giả lập, có thể bổ sung logic)
        dto.setSubjectsThisMonth(2);
        dto.setQuestionsThisMonth(123);
        dto.setExamsThisMonth(8);

        // Biểu đồ tiến độ câu hỏi theo môn (bar chart) - lấy dữ liệu thực
        List<Object[]> courseStats = questionRepository.getQuestionDistributionByCourse();
        List<String> courseLabels = new ArrayList<>();
        List<Number> createdData = new ArrayList<>();
        for (Object[] row : courseStats) {
            courseLabels.add((String) row[0]);
            createdData.add((Number) row[1]);
        }
        // Nếu có target, lấy thêm hoặc set cứng
        List<Number> targetData = new ArrayList<>(Collections.nCopies(courseLabels.size(), 50)); // ví dụ target 50 cho
                                                                                                 // mỗi môn

        ChartDataDTO barChart = new ChartDataDTO();
        barChart.setLabels(courseLabels);
        ChartDataDTO.ChartDatasetDTO created = new ChartDataDTO.ChartDatasetDTO();
        created.setLabel("Created");
        created.setBackgroundColor("#8979FF");
        created.setData(createdData);
        ChartDataDTO.ChartDatasetDTO target = new ChartDataDTO.ChartDatasetDTO();
        target.setLabel("Target");
        target.setBackgroundColor("#FF928A");
        target.setData(targetData);
        barChart.setDatasets(Arrays.asList(created, target));
        dto.setBarChart(barChart);

        // Biểu đồ tỉ lệ độ khó câu hỏi (pie chart) - lấy dữ liệu thực
        List<Object[]> diffStats = questionRepository.getQuestionDifficultyStatistics();
        List<String> diffLabels = new ArrayList<>();
        List<Number> diffData = new ArrayList<>();
        for (Object[] row : diffStats) {
            diffLabels.add(row[0].toString());
            diffData.add((Number) row[1]);
        }
        ChartDataDTO pieChart = new ChartDataDTO();
        pieChart.setLabels(diffLabels);
        ChartDataDTO.ChartDatasetDTO pieData = new ChartDataDTO.ChartDatasetDTO();
        pieData.setLabel("Difficulty");
        pieData.setBackgroundColor(null);
        pieData.setData(diffData);
        pieChart.setDatasets(Collections.singletonList(pieData));
        dto.setPieChart(pieChart);

        // Danh sách nhiệm vụ gần đây (tất cả task của staffId, ưu tiên In Progress và
        // Pending)
        List<TaskDTO> allTasks = tasksRepository.findByAssignedTo_UserIdOrderByDueDateDesc(staffId)
                .stream()
                .map(task -> {
                    TaskDTO t = new TaskDTO();
                    t.setId(task.getTaskId());
                    t.setTitle(task.getTitle());
                    String status = (task.getStatus() != null) ? task.getStatus().name() : null;

                    // Debug log để xem giá trị status thực tế
                    System.out.println("DEBUG Task: " + task.getTitle() + " - Status from DB: " + status);

                    if (status == null || status.trim().isEmpty()) {
                        t.setStatus("Pending");
                        System.out.println(
                                "DEBUG: Task '" + task.getTitle() + "' has NULL/empty status, setting to Pending");
                    } else {
                        // Normalize status và so sánh - chỉ 2 trạng thái: In Progress và Pending
                        String normalizedStatus = status.toLowerCase().trim().replace("_", " ");
                        if ("in progress".equals(normalizedStatus)) {
                            t.setStatus("In Progress");
                        } else {
                            // Tất cả status khác đều chuyển thành Pending (bao gồm "completed", "pending",
                            // etc.)
                            System.out.println(
                                    "DEBUG: Converting status '" + status + "' to Pending for task: "
                                            + task.getTitle());
                            t.setStatus("Pending");
                        }
                    }

                    // Debug final status
                    System.out.println("DEBUG Task: " + task.getTitle() + " - Final Status: " + t.getStatus());

                    t.setSubject(task.getCourse() != null ? task.getCourse().getCourseName() : "");
                    t.setDueDate(task.getDueDate());
                    return t;
                })
                .collect(Collectors.toList());
        // recentTasks: chỉ lấy 3 task gần nhất
        dto.setRecentTasks(allTasks.stream().limit(3).collect(Collectors.toList()));
        // allTasks: trả về toàn bộ cho API see more
        dto.setAllTasks(allTasks);

        // Danh sách cảnh báo trùng lặp (top 5 warning của staffId cho dashboard)
        List<DuplicateWarningDTO> warnings = duplicateDetectionRepository
                .findTop5ByDetectedByOrderByDetectedAtDesc(staffId)
                .stream().map(det -> {
                    DuplicateWarningDTO w = new DuplicateWarningDTO();
                    w.setSimilarity(
                            det.getSimilarityScore() != null ? det.getSimilarityScore().doubleValue() * 100 : 0);
                    w.setQuestion1(
                            (det.getNewQuestionId() != null
                                    && questionRepository.findById(det.getNewQuestionId()).isPresent())
                                            ? questionRepository.findById(det.getNewQuestionId()).get().getContent()
                                            : "");
                    w.setQuestion2(
                            (det.getSimilarQuestionId() != null
                                    && questionRepository.findById(det.getSimilarQuestionId()).isPresent())
                                            ? questionRepository.findById(det.getSimilarQuestionId()).get().getContent()
                                            : "");
                    return w;
                }).collect(Collectors.toList());
        dto.setDuplicateWarnings(warnings);

        // Lấy tổng số warnings để kiểm tra có nhiều hơn 5 không
        long totalWarnings = duplicateDetectionRepository.findByDetectedBy(staffId).size();
        dto.setTotalDuplicateWarnings((int) totalWarnings);

        return dto;
    }
}
