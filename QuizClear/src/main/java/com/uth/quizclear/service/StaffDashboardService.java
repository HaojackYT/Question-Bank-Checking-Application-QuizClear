package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.StaffDashboardDTO;
import com.uth.quizclear.model.dto.ChartDataDTO;
import com.uth.quizclear.model.dto.TaskDTO;
import com.uth.quizclear.model.dto.DuplicateWarningDTO;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.entity.Subject;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.DuplicateDetection;
import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.TasksRepository;
import com.uth.quizclear.repository.SubjectRepository;
import com.uth.quizclear.repository.ExamRepository;
import com.uth.quizclear.repository.DuplicateDetectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StaffDashboardService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private DuplicateDetectionRepository duplicateDetectionRepository;    public StaffDashboardDTO getDashboardForStaff(Long staffId) {
        StaffDashboardDTO dashboard = new StaffDashboardDTO();
        
        // Get current month for statistics - using current date for better data
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
        
        // Also check last 3 months for more data
        LocalDateTime threeMonthsAgo = now.minusMonths(3);
        
        System.out.println("=== STAFF DASHBOARD DEBUG ===");
        System.out.println("Staff ID: " + staffId);
        System.out.println("Current month: " + startOfMonth + " to " + endOfMonth);
        System.out.println("Extended range: " + threeMonthsAgo + " to " + now);
        
        // Set basic statistics from database with better fallback
        long totalSubjects = subjectRepository.count();
        long totalQuestions = questionRepository.count();
        
        // Count duplicates only for this staff's questions
        long staffDuplicates = 0;
        try {
            // Get all questions created by this staff
            List<Question> staffQuestions = questionRepository.findByCreatedBy_UserId(staffId);
            Set<Long> staffQuestionIds = staffQuestions.stream()
                .map(Question::getQuestionId)
                .collect(Collectors.toSet());
            
            System.out.println("Staff has " + staffQuestions.size() + " questions");
            
            // Count duplicates involving staff's questions
            List<DuplicateDetection> allDuplicates = duplicateDetectionRepository.findAll();
            staffDuplicates = allDuplicates.stream()
                .filter(d -> staffQuestionIds.contains(d.getNewQuestionId()) || 
                            staffQuestionIds.contains(d.getSimilarQuestionId()))
                .count();
        } catch (Exception e) {
            System.err.println("Error counting staff duplicates: " + e.getMessage());
            staffDuplicates = duplicateDetectionRepository.count(); // fallback to total
        }        
        long totalExams = examRepository.count();
        
        // Use reasonable defaults if no data
        if (totalSubjects == 0) totalSubjects = 5;
        if (totalQuestions == 0) totalQuestions = 25;
        if (totalExams == 0) totalExams = 8;
        
        System.out.println("Total Subjects: " + totalSubjects);
        System.out.println("Total Questions: " + totalQuestions);
        System.out.println("Staff Duplicates: " + staffDuplicates);
        System.out.println("Total Exams: " + totalExams);
        
        dashboard.setTotalSubjects(Math.toIntExact(totalSubjects));
        dashboard.setTotalQuestions(Math.toIntExact(totalQuestions));
        dashboard.setDuplicateQuestions(Math.toIntExact(staffDuplicates));
        dashboard.setExamsCreated(Math.toIntExact(totalExams));
        
        // Statistics for this month - check multiple time ranges
        List<Subject> recentSubjects = subjectRepository.findRecentSubjects(startOfMonth);
        if (recentSubjects.isEmpty()) {
            recentSubjects = subjectRepository.findRecentSubjects(threeMonthsAgo);
        }
        dashboard.setSubjectsThisMonth(recentSubjects.size());
        System.out.println("Recent Subjects: " + recentSubjects.size());
        
        List<Question> recentQuestions = questionRepository.findByCreatedAtBetween(startOfMonth, endOfMonth);
        if (recentQuestions.isEmpty()) {
            recentQuestions = questionRepository.findByCreatedAtBetween(threeMonthsAgo, now);
        }
        dashboard.setQuestionsThisMonth(recentQuestions.size());
        System.out.println("Recent Questions: " + recentQuestions.size());
        
        // For exams this month, use the recent exams query
        List<Exam> recentExams = examRepository.findRecentExamsByDepartmentScope("", startOfMonth);
        if (recentExams.isEmpty()) {
            recentExams = examRepository.findRecentExamsByDepartmentScope("", threeMonthsAgo);
        }
        dashboard.setExamsThisMonth(recentExams.size());
        System.out.println("Recent Exams: " + recentExams.size());

        // Create bar chart data (Question Progress by Subject)
        dashboard.setBarChart(createBarChartData());

        // Create pie chart data (Question difficulty ratio)
        dashboard.setPieChart(createPieChartData());

        // Set recent tasks
        dashboard.setRecentTasks(getRecentTasks(staffId));
        
        // Set duplicate warnings
        dashboard.setDuplicateWarnings(getDuplicateWarnings(staffId));        System.out.println("=== END STAFF DASHBOARD DEBUG ===");
        
        return dashboard;
    }    private ChartDataDTO createBarChartData() {
        ChartDataDTO barChart = new ChartDataDTO();
        
        // Get question distribution by course (we'll use course names as subject names)
        List<Object[]> questionDistribution = questionRepository.getQuestionDistributionByCourse();
        
        List<String> labels = new ArrayList<>();
        List<Number> createdData = new ArrayList<>();
        List<Number> targetData = new ArrayList<>();
        
        // Convert the results to chart data
        for (Object[] row : questionDistribution) {
            String courseName = (String) row[0];
            Long count = (Long) row[1];
            
            labels.add(courseName);
            createdData.add(count);
            // Set target as a reasonable number (e.g., created + 20% or minimum 10)
            targetData.add(Math.max(count + 5, 10));
        }
        
        // If no data, provide meaningful default values
        if (labels.isEmpty()) {
            labels = Arrays.asList("Computer Science", "Mathematics", "Physics", "Chemistry", "Biology");
            createdData = Arrays.asList(15, 12, 8, 10, 7);
            targetData = Arrays.asList(20, 15, 12, 15, 10);
        }
        
        barChart.setLabels(labels);
        
        // Dataset for Questions Created (Blue)
        ChartDataDTO.ChartDatasetDTO createdDataset = new ChartDataDTO.ChartDatasetDTO();
        createdDataset.setLabel("Questions Created");
        createdDataset.setData(createdData);
        createdDataset.setBackgroundColor("#007bff");
        
        // Dataset for Target (Red/Pink)
        ChartDataDTO.ChartDatasetDTO targetDataset = new ChartDataDTO.ChartDatasetDTO();
        targetDataset.setLabel("Target");
        targetDataset.setData(targetData);
        targetDataset.setBackgroundColor("#FF6B6B");
        
        barChart.setDatasets(Arrays.asList(createdDataset, targetDataset));
        return barChart;
    }    private ChartDataDTO createPieChartData() {
        ChartDataDTO pieChart = new ChartDataDTO();
        
        // Get question difficulty statistics
        List<Object[]> difficultyStats = questionRepository.getQuestionDifficultyStatistics();
        
        List<String> labels = new ArrayList<>();
        List<Number> data = new ArrayList<>();
        List<String> backgroundColors = Arrays.asList("#8979FF", "#FF928A", "#3CC3DF", "#FFAE4C", "#34C759");
        
        // Convert the results to chart data
        for (Object[] row : difficultyStats) {
            DifficultyLevel level = (DifficultyLevel) row[0];
            Long count = (Long) row[1];
            
            labels.add(level.name());
            data.add(count);
        }        
        // If no data, provide meaningful default values
        if (labels.isEmpty()) {
            labels = Arrays.asList("Easy", "Medium", "Hard", "Expert");
            data = Arrays.asList(35, 40, 20, 5);
        }
        
        pieChart.setLabels(labels);
        
        // Create dataset for pie chart
        ChartDataDTO.ChartDatasetDTO dataset = new ChartDataDTO.ChartDatasetDTO();
        dataset.setData(data);
        dataset.setBackgroundColors(backgroundColors);
        
        pieChart.setDatasets(Arrays.asList(dataset));
        return pieChart;
    }    public List<TaskDTO> getRecentTasks(Long staffId) {
        List<TaskDTO> taskDTOs = new ArrayList<>();
        
        System.out.println("=== GETTING RECENT TASKS ===");
        System.out.println("Staff ID: " + staffId);
          try {
            // First try to get tasks assigned to the staff
            List<Tasks> tasks = tasksRepository.findTop5ByAssignedTo_UserIdOrderByDueDateDesc(staffId);
            System.out.println("Found " + tasks.size() + " assigned tasks for staff " + staffId);            // If no assigned tasks, try to get any tasks from repository
            if (tasks.isEmpty()) {
                List<Tasks> allTasks = tasksRepository.findAll();
                tasks = allTasks.stream()
                    .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                    .limit(10)
                    .collect(Collectors.toList());
                System.out.println("Found " + tasks.size() + " recent tasks from all tasks");
            }
            
            // Convert Tasks to TaskDTO
            for (Tasks task : tasks) {
                System.out.println("Task: " + task.getTitle() + ", Status: " + task.getStatus() + ", Due: " + task.getDueDate());
                TaskDTO taskDTO = new TaskDTO();
                taskDTO.setId(task.getTaskId());
                taskDTO.setTitle(task.getTitle());
                taskDTO.setStatus(task.getStatus() != null ? task.getStatus().name() : "PENDING");
                taskDTO.setSubject(task.getCourse() != null ? task.getCourse().getCourseName() : "General");
                taskDTO.setDueDate(task.getDueDate());
                taskDTOs.add(taskDTO);
            }
            
        } catch (Exception e) {
            System.err.println("Error fetching recent tasks: " + e.getMessage());
            e.printStackTrace();
        }        
        System.out.println("Returning " + taskDTOs.size() + " real task DTOs from database");
        return taskDTOs;
    }public List<DuplicateWarningDTO> getDuplicateWarnings(Long staffId) {
        List<DuplicateWarningDTO> warningDTOs = new ArrayList<>();
        
        System.out.println("=== GETTING DUPLICATE WARNINGS ===");
        System.out.println("Staff ID: " + staffId);
        
        try {
            // Get recent duplicate detections for this staff
            List<DuplicateDetection> detections = duplicateDetectionRepository.findTop5ByDetectedByOrderByDetectedAtDesc(staffId);
            System.out.println("Found " + detections.size() + " duplicate detections for staff " + staffId);
              // If no staff-specific detections, get recent general detections
            if (detections.isEmpty()) {
                detections = duplicateDetectionRepository.findAll().stream()
                    .sorted((d1, d2) -> d2.getDetectedAt().compareTo(d1.getDetectedAt()))
                    .limit(5)
                    .collect(Collectors.toList());
                System.out.println("Using fallback: Found " + detections.size() + " general detections");
            }
            
            // Convert database detections to DTOs
            for (DuplicateDetection detection : detections) {
                System.out.println("Detection: similarity=" + detection.getSimilarityScore() + 
                                 ", status=" + detection.getStatus() + 
                                 ", q1=" + detection.getNewQuestionId() + 
                                 ", q2=" + detection.getSimilarQuestionId());
                
                DuplicateWarningDTO warningDTO = new DuplicateWarningDTO();
                
                // Set similarity score
                if (detection.getSimilarityScore() != null) {
                    warningDTO.setSimilarity(detection.getSimilarityScore().doubleValue());
                } else {
                    warningDTO.setSimilarity(0.85); // Default high similarity
                }
                
                // Get question contents
                try {
                    Question newQuestion = questionRepository.findById(detection.getNewQuestionId()).orElse(null);
                    Question similarQuestion = questionRepository.findById(detection.getSimilarQuestionId()).orElse(null);
                    
                    warningDTO.setQuestion1(newQuestion != null ? newQuestion.getContent() : "Question not found");
                    warningDTO.setQuestion2(similarQuestion != null ? similarQuestion.getContent() : "Question not found");
                } catch (Exception e) {
                    warningDTO.setQuestion1("Error loading question");
                    warningDTO.setQuestion2("Error loading question");
                }
                
                warningDTOs.add(warningDTO);
            }
        } catch (Exception e) {
            System.err.println("Error fetching duplicate warnings: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Returning " + warningDTOs.size() + " warning DTOs");
        return warningDTOs;
    }
}
