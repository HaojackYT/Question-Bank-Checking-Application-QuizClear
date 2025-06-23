package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.SL_ReviewApprovalTaskDTO;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.repository.SL_ReviewApprovalTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SL_ReviewApprovalService {

    @Autowired
    private SL_ReviewApprovalTaskRepository taskRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");

    public List<SL_ReviewApprovalTaskDTO> getTasks(String search, String filter) {
        TaskStatus status = parseFilterToStatus(filter);
        List<Tasks> tasks = taskRepository.findTasksBySearchAndStatus(search, status);

        return tasks.stream().map(task -> {
            SL_ReviewApprovalTaskDTO dto = new SL_ReviewApprovalTaskDTO();
            dto.setTaskId(task.getTaskId());
            dto.setTitle(task.getTitle());
            dto.setLecturerName(task.getAssignedTo().getFullName());
            dto.setTotalQuestions(task.getTotalQuestions());
            dto.setDueDate(task.getDueDate() != null ? task.getDueDate().format(DATE_FORMATTER) : "");
            dto.setRawStatus(task.getStatus());
            dto.setStatus(convertStatusToDisplay(task.getStatus()));
            return dto;
        }).collect(Collectors.toList());
    }

    public void approveTask(Integer taskId) {
        Tasks task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(TaskStatus.completed);
        task.setCompletedAt(java.time.LocalDateTime.now());
        taskRepository.save(task);
    }

    public void declineTask(Integer taskId, String declineReason) {
        Tasks task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(TaskStatus.cancelled);
        task.setCompletedAt(java.time.LocalDateTime.now());
        // Lưu lý do từ chối vào bảng comments hoặc một trường riêng (nếu cần)
        taskRepository.save(task);
    }

    private TaskStatus parseFilterToStatus(String filter) {
        if (filter == null || filter.equals("All Tasks")) {
            return null; // Lấy tất cả trạng thái
        }
        switch (filter) {
            case "Completed":
                return TaskStatus.pending;
            case "Approved":
                return TaskStatus.completed;
            case "In Progress":
                return TaskStatus.in_progress;
            default:
                return null;
        }
    }

    private String convertStatusToDisplay(TaskStatus status) {
        switch (status) {
            case in_progress:
                return "in progress";
            case pending:
                return "Completed";
            case completed:
                return "Approved";
            case cancelled:
                return "Cancelled";
            default:
                return status.name();
        }
    }
}