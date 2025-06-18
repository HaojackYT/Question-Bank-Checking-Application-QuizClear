package com.uth.quizclear.model.dto;


public class HODDashboardChartsDTO {
    private String subject;
    private int created;
    private int target;

    public HODDashboardChartsDTO(String subject, int created, int target) {
        this.subject = subject;
        this.created = created;
        this.target = target;
    }

    public String getSubject() {
        return subject;
    }

    public int getCreated() {
        return created;
    }
    public int getTarget() {
        return target;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public void setCreated(int created) {
        this.created = created;
    }
    public void setTarget(int target) {
        this.target = target;
    }
    @Override
    public String toString() {
        return "HODDashboardChartsDTO{" +
                "subject='" + subject + '\'' +
                ", created=" + created +
                ", target=" + target +
                '}';
    }
}
