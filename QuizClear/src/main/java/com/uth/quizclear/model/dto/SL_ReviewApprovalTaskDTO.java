package com.uth.quizclear.model.dto;

import com.uth.quizclear.model.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class SL_ReviewApprovalTaskDTO {
    private Integer taskId;
    private String title;
    private String lecturerName; // full_name từ bảng users
    private Integer totalQuestions;
    private String dueDate; // Định dạng ngày: dd/MM/yy
    private String status; // Trạng thái hiển thị: In Prog., Completed, Approved, Cancelled
    private TaskStatus rawStatus; // Trạng thái gốc trong DB để kiểm tra logic

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");

    // Phương thức format ngày
    public void setDueDateFromLocalDateTime(LocalDateTime dueDate) {
        this.dueDate = dueDate != null ? dueDate.format(DATE_FORMATTER) : "";
    }

    // Phương thức chuyển đổi trạng thái hiển thị
    public void setStatusFromRawStatus(TaskStatus rawStatus) {
        this.rawStatus = rawStatus;
        if (rawStatus != null) {
            switch (rawStatus) {
                case in_progress:
                    this.status = "in progress";
                    break;
                case pending:
                    this.status = "Completed";
                    break;
                case completed:
                    this.status = "Approved";
                    break;
                case cancelled:
                    this.status = "Cancelled";
                    break;
                default:
                    this.status = rawStatus.name();
            }
        } else {
            this.status = "";
        }
    }
    public Integer getTaskId() {
    return taskId;
}

public void setTaskId(Integer taskId) {
    this.taskId = taskId;
}

public String getTitle() {
    return title;
}

public void setTitle(String title) {
    this.title = title;
}

public String getLecturerName() {
    return lecturerName;
}

public void setLecturerName(String lecturerName) {
    this.lecturerName = lecturerName;
}

public Integer getTotalQuestions() {
    return totalQuestions;
}

public void setTotalQuestions(Integer totalQuestions) {
    this.totalQuestions = totalQuestions;
}

public String getDueDate() {
    return dueDate;
}

public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

public TaskStatus getRawStatus() {
    return rawStatus;
}

public void setRawStatus(TaskStatus rawStatus) {
    this.rawStatus = rawStatus;
}

}