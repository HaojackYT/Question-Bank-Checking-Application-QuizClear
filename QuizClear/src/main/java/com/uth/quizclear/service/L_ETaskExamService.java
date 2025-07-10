package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.L_ETaskExamDTO;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.ExamAssignment;
import com.uth.quizclear.model.entity.Plan;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.repository.ExamAssignmentRepository;
import com.uth.quizclear.repository.ExamRepository;
import com.uth.quizclear.repository.L_ETaskExamRepository;
import com.uth.quizclear.repository.PlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class L_ETaskExamService {

    private static final Logger logger = LoggerFactory.getLogger(L_ETaskExamService.class);

    @Autowired
    private L_ETaskExamRepository taskRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamAssignmentRepository examAssignmentRepository;

    public List<L_ETaskExamDTO> getCreateExamTasksByUserId(Integer userId) {
        logger.info("Fetching create_exam tasks for userId: {}", userId);
        if (userId == null) {
            logger.error("UserId is null, cannot fetch tasks");
            return List.of();
        }
        List<Tasks> tasks = taskRepository.findCreateExamTasksByUserId(userId);
        logger.info("Found {} create_exam tasks for userId: {}", tasks.size(), userId);

        if (tasks.isEmpty()) {
            logger.warn("No create_exam tasks found for userId: {}. Checking all tasks...", userId);
            List<Tasks> allTasks = taskRepository.findAllTasksByUserId(userId);
            logger.info("Found {} total tasks for userId: {}", allTasks.size(), userId);
            if (allTasks.isEmpty()) {
                logger.error("No tasks assigned to userId: {}. Check user assignment in tasks table.", userId);
            } else {
                logger.warn("Tasks found but none with task_type = 'create_exam'. Check task_type values.");
            }
        }

        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public L_ETaskExamDTO getTaskDetails(Integer taskId, Integer userId) {
        logger.info("Fetching details for taskId: {} and userId: {}", taskId, userId);
        if (taskId == null || userId == null) {
            logger.error("TaskId or UserId is null, cannot fetch task details");
            throw new IllegalArgumentException("TaskId or UserId cannot be null");
        }

        Tasks task = taskRepository.findTaskByIdAndUserId(taskId, userId);
        if (task == null) {
            logger.error("Task not found or not assigned to userId: {} for taskId: {}", userId, taskId);
            throw new IllegalArgumentException("Task not found or not assigned to this user");
        }

        L_ETaskExamDTO dto = convertToDTO(task);

        // Lấy thông tin từ plans nếu có plan_id
        if (task.getPlanId() != null) {
            try {
                Map<String, Integer> difficultyCounts = getPlanDifficultyCounts(task.getPlanId());
                dto.setTotalRecognition(difficultyCounts.getOrDefault("total_recognition", 0));
                dto.setTotalComprehension(difficultyCounts.getOrDefault("total_comprehension", 0));
                dto.setTotalBasicApplication(difficultyCounts.getOrDefault("total_basic_application", 0));
                dto.setTotalAdvancedApplication(difficultyCounts.getOrDefault("total_advanced_application", 0));
                // Tính Total Questions từ tổng 4 trường độ khó
                dto.setTotalQuestions(difficultyCounts.getOrDefault("total_recognition", 0) +
                                      difficultyCounts.getOrDefault("total_comprehension", 0) +
                                      difficultyCounts.getOrDefault("total_basic_application", 0) +
                                      difficultyCounts.getOrDefault("total_advanced_application", 0));
            } catch (Exception e) {
                logger.error("Error fetching difficulty counts for taskId: {}. Error: {}", taskId, e.getMessage());
                dto.setTotalQuestions(0); // Gán giá trị mặc định nếu có lỗi
            }
        }

        // Lấy duration_minutes từ exam dựa trên plan_id
        Integer durationMinutes = getTaskDurationMinutes(taskId, userId);
        if (durationMinutes != null && durationMinutes > 0) {
            dto.setDurationMinutes(durationMinutes);
        } else {
            dto.setDurationMinutes(60); // Giá trị mặc định nếu không tìm thấy
            logger.warn("No valid duration minutes found for taskId: {}. Using default duration: 60 minutes", taskId);
        }

        return dto;
    }

    public void acceptTask(Integer taskId, Integer userId) {
        logger.info("Accepting taskId: {} for userId: {}", taskId, userId);
        if (taskId == null || userId == null) {
            logger.error("TaskId or UserId is null, cannot accept task");
            throw new IllegalArgumentException("TaskId or UserId cannot be null");
        }
        Tasks task = taskRepository.findTaskByIdAndUserId(taskId, userId);
        if (task == null) {
            logger.error("Task not found or not assigned to userId: {} for taskId: {}", userId, taskId);
            throw new IllegalArgumentException("Task not found or not assigned to this user");
        }
        if (task.getStatus() == TaskStatus.pending) {
            try {
                task.acceptTask(task.getAssignedTo());
                taskRepository.save(task);
                logger.info("Task accepted successfully for taskId: {}", taskId);
            } catch (Exception e) {
                logger.error("Error accepting taskId: {}. Error: {}", taskId, e.getMessage());
                throw new RuntimeException("Failed to accept task: " + e.getMessage());
            }
        } else {
            logger.warn("Task cannot be accepted in current status: {} for taskId: {}", task.getStatus(), taskId);
            throw new IllegalStateException("Task cannot be accepted in current status: " + task.getStatus());
        }
    }

    private L_ETaskExamDTO convertToDTO(Tasks task) {
        logger.debug("Converting taskId: {} to DTO", task.getTaskId());
        if (task == null) {
            logger.error("Task is null, cannot convert to DTO");
            return new L_ETaskExamDTO(); // Trả về DTO rỗng nếu task null
        }
        String subject = task.getCourse() != null ? task.getCourse().getCourseName() : "Unknown Subject";
        String assignedByName = task.getAssignedBy() != null ? task.getAssignedBy().getFullName() : "Unknown";
        L_ETaskExamDTO dto = new L_ETaskExamDTO();
        dto.setTaskId(task.getTaskId());
        dto.setPlanId(task.getPlanId());
        dto.setSubject(subject);
        dto.setDescription(task.getDescription());
        dto.setTotalQuestions(task.getTotalQuestions()); // Giá trị tạm thời, sẽ được cập nhật trong getTaskDetails
        dto.setDeadline(task.getDueDate() != null ? task.getDueDate().toString() : "N/A");
        dto.setStatus(task.getStatus() != null ? task.getStatus().toString() : "UNKNOWN");
        dto.setAssignedByName(assignedByName);
        dto.setTotalRecognition(task.getTotalRecognition());
        dto.setTotalComprehension(task.getTotalComprehension());
        dto.setTotalBasicApplication(task.getTotalBasicApplication());
        dto.setTotalAdvancedApplication(task.getTotalAdvancedApplication());
        dto.setDurationMinutes(task.getDurationMinutes() != null ? task.getDurationMinutes() : null); // Giá trị tạm thời
        logger.debug("DTO created: {}", dto);
        return dto;
    }

    // Thêm phương thức getPlanDifficultyCounts
    public Map<String, Integer> getPlanDifficultyCounts(Integer planId) {
        logger.info("Fetching difficulty counts for planId: {}", planId);
        if (planId == null) {
            logger.error("PlanId is null, cannot fetch difficulty counts");
            return new HashMap<>(); // Trả về map rỗng nếu planId null
        }
        Map<String, Integer> difficultyCounts = new HashMap<>();
        Optional<Plan> planOpt = planRepository.findById((long) planId);
        if (planOpt.isPresent()) {
            Plan plan = planOpt.get();
            difficultyCounts.put("total_recognition", plan.getTotalRecognition() != null ? plan.getTotalRecognition() : 0);
            difficultyCounts.put("total_comprehension", plan.getTotalComprehension() != null ? plan.getTotalComprehension() : 0);
            difficultyCounts.put("total_basic_application", plan.getTotalBasicApplication() != null ? plan.getTotalBasicApplication() : 0);
            difficultyCounts.put("total_advanced_application", plan.getTotalAdvancedApplication() != null ? plan.getTotalAdvancedApplication() : 0);
        } else {
            logger.warn("Plan not found for planId: {}. Setting default values.", planId);
            difficultyCounts.put("total_recognition", 0);
            difficultyCounts.put("total_comprehension", 0);
            difficultyCounts.put("total_basic_application", 0);
            difficultyCounts.put("total_advanced_application", 0);
        }
        return difficultyCounts;
    }

    public Integer getTaskDurationMinutes(Integer taskId, Integer userId) {
        logger.info("Fetching duration minutes for taskId: {} and userId: {}", taskId, userId);
        if (taskId == null || userId == null) {
            logger.error("TaskId or UserId is null, cannot fetch duration minutes");
            return null;
        }
        Optional<Tasks> taskOpt = taskRepository.findById( taskId);
        if (taskOpt.isEmpty()) {
            logger.warn("Task not found for taskId: {}. Returning default duration: 60 minutes.", taskId);
            return 60; // Giá trị mặc định nếu task không tồn tại
        }
        Tasks task = taskOpt.get();
        if (task.getPlanId() == null) {
            logger.warn("PlanId is null for taskId: {}. Returning default duration: 60 minutes.", taskId);
            return 60;
        }
        Optional<Plan> planOpt = planRepository.findById(task.getPlanId().longValue());
        if (planOpt.isEmpty()) {
            logger.warn("Plan not found for planId: {}. Returning default duration: 60 minutes.", task.getPlanId());
            return 60;
        }
        Long courseId = planOpt.get().getCourseId();
        if (courseId == null) {
            logger.warn("CourseId is null for planId: {}. Returning default duration: 60 minutes.", task.getPlanId());
            return 60;
        }
        try {
            // Ưu tiên lấy từ exams dựa trên plan_id
            Optional<Exam> examOpt = examRepository.findByPlanId(task.getPlanId().longValue());
            if (examOpt.isPresent()) {
                Integer examDuration = examOpt.get().getDurationMinutes();
                if (examDuration != null && examDuration > 0) {
                    logger.info("Found duration minutes: {} from exam for taskId: {}", examDuration, taskId);
                    return examDuration;
                }
            } else {
                logger.warn("No exam found for planId: {}. Returning default duration: 60 minutes.", task.getPlanId());
            }

            // Fallback: Kiểm tra exam_assignments (dù không có duration_minutes)
            Optional<ExamAssignment> assignmentOpt = examAssignmentRepository.findByCourseIdAndAssignedTo(courseId.longValue(), userId);
            if (assignmentOpt.isPresent()) {
                logger.warn("ExamAssignment found but no duration_minutes column. Returning default duration: 60 minutes.");
            } else {
                logger.warn("No ExamAssignment found for courseId: {} and userId: {}.", courseId, userId);
            }

            logger.warn("No valid duration minutes found for taskId: {}. Returning default duration: 60 minutes.", taskId);
            return 60; // Giá trị mặc định
        } catch (Exception e) {
            logger.error("Error fetching duration minutes for taskId: {}. Error: {}. Returning default duration: 60 minutes.", taskId, e.getMessage());
            return 60;
        }
    }
}