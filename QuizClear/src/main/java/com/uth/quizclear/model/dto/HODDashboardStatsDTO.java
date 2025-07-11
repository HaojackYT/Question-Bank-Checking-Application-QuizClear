package com.uth.quizclear.model.dto;

public class HODDashboardStatsDTO {
    private long lecturerCount;
    private long pendingCount;
    private long approvedCount;
    private long rejectedCount;

    private long lecturerChange;
    private long pendingChange;
    private long approvedChange;
    private long rejectedChange;

    public HODDashboardStatsDTO(long lecturerCount, long lecturerChange, long pendingCount, long pendingChange,
            long approvedCount, long approvedChange, long rejectedCount, long rejectedChange) {
        this.lecturerCount = lecturerCount;
        this.lecturerChange = lecturerChange;
        this.pendingCount = pendingCount;
        this.pendingChange = pendingChange;
        this.approvedCount = approvedCount;
        this.approvedChange = approvedChange;
        this.rejectedCount = rejectedCount;
        this.rejectedChange = rejectedChange;
    }

    public long getLecturerCount() {
        return lecturerCount;
    }

    public long getPendingCount() {
        return pendingCount;
    }

    public long getApprovedCount() {
        return approvedCount;
    }

    public long getRejectedCount() {
        return rejectedCount;
    }

    public void setLecturerCount(long lecturerCount) {
        this.lecturerCount = lecturerCount;
    }

    public void setPendingCount(long pendingCount) {
        this.pendingCount = pendingCount;
    }

    public void setApprovedCount(long approvedCount) {
        this.approvedCount = approvedCount;
    }

    public void setRejectedCount(long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public long getLecturerChange() {
        return lecturerChange;
    }

    public long getPendingChange() {
        return pendingChange;
    }

    public long getApprovedChange() {
        return approvedChange;
    }

    public long getRejectedChange() {
        return rejectedChange;
    }

    public void setLecturerChange(long lecturerChange) {
        this.lecturerChange = lecturerChange;
    }

    public void setPendingChange(long pendingChange) {
        this.pendingChange = pendingChange;
    }

    public void setApprovedChange(long approvedChange) {
        this.approvedChange = approvedChange;
    }

    public void setRejectedChange(long rejectedChange) {
        this.rejectedChange = rejectedChange;
    }

    // Additional getters for frontend compatibility
    public long getTotalLecturers() {
        return lecturerCount;
    }

    public long getLecturersChange() {
        return lecturerChange;
    }

    public long getPendingApprovals() {
        return pendingCount;
    }

    public long getApprovedThisMonth() {
        return approvedCount;
    }

    public long getTotalSubjects() {
        return rejectedCount;
    }

    public long getSubjectCount() {
        return rejectedCount;
    }

    public long getSubjectChange() {
        return rejectedChange;
    }

    public long getActiveSubjects() {
        return rejectedCount;
    }

    @Override
    public String toString() {
        return "HODDashboardStatsDTO{" +
                "lecturerCount=" + lecturerCount +
                ", lecturerChange=" + lecturerChange +
                ", pendingCount=" + pendingCount +
                ", pendingChange=" + pendingChange +
                ", approvedCount=" + approvedCount +
                ", approvedChange=" + approvedChange +
                ", rejectedCount=" + rejectedCount +
                ", rejectedChange=" + rejectedChange +
                '}';
    }

}
