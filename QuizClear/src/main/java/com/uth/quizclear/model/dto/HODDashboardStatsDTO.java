package com.uth.quizclear.model.dto;



public class HODDashboardStatsDTO {
    private long lecturerCount;
    private long pendingCount;
    private long approvedCount;
    private long rejectedCount;

    public HODDashboardStatsDTO(long lecturerCount, long pendingCount, long approvedCount, long rejectedCount) {
        this.lecturerCount = lecturerCount;
        this.pendingCount = pendingCount;
        this.approvedCount = approvedCount;
        this.rejectedCount = rejectedCount;
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
    @Override
    public String toString() {
        return "HODDashboardStatsDTO{" +
                "lecturerCount=" + lecturerCount +
                ", pendingCount=" + pendingCount +
                ", approvedCount=" + approvedCount +
                ", rejectedCount=" + rejectedCount +
                '}';
    }
}
