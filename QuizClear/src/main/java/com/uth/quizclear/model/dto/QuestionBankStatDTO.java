package com.uth.quizclear.model.dto;

public class QuestionBankStatDTO {
    private String subjectName;
    private long total;
    private long approved;
    private long pending;
    private long rejected;
    private long duplicate;

    public QuestionBankStatDTO() {}

    public QuestionBankStatDTO(String subjectName, long total, long approved, long pending, long rejected,
            long duplicate) {
        this.subjectName = subjectName;
        this.total = total;
        this.approved = approved;
        this.pending = pending;
        this.rejected = rejected;
        this.duplicate = duplicate;
    }
    
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    
    public long getApproved() { return approved; }
    public void setApproved(long approved) { this.approved = approved; }
    
    public long getPending() { return pending; }
    public void setPending(long pending) { this.pending = pending; }
    
    public long getRejected() { return rejected; }
    public void setRejected(long rejected) { this.rejected = rejected; }
    
    public long getDuplicate() { return duplicate; }
    public void setDuplicate(long duplicate) { this.duplicate = duplicate; }
}