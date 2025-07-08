package com.uth.quizclear.model.dto;

public class ExamStatusStatDTO {
    private String subjectName;
    private long total;
    private long draft;
    private long pending;
    private long approved;
    private long rejected;
    private double approvalRate;

    public ExamStatusStatDTO() {}

    public ExamStatusStatDTO(String subjectName, long total, long draft, long pending, long approved, long rejected,
            double approvalRate) {
        this.subjectName = subjectName;
        this.total = total;
        this.draft = draft;
        this.pending = pending;
        this.approved = approved;
        this.rejected = rejected;
        this.approvalRate = approvalRate;
    }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public long getDraft() { return draft; }
    public void setDraft(long draft) { this.draft = draft; }

    public long getPending() { return pending; }
    public void setPending(long pending) { this.pending = pending; }

    public long getApproved() { return approved; }
    public void setApproved(long approved) { this.approved = approved; }

    public long getRejected() { return rejected; }
    public void setRejected(long rejected) { this.rejected = rejected; }

    public double getApprovalRate() { return approvalRate; }
    public void setApprovalRate(double approvalRate) { this.approvalRate = approvalRate; }
}