package com.uth.quizclear.model.dto;


public class HODDashboardChartsDTO {
    private String subject;
    private int created;
    private int target;
    private float percentage;

    public HODDashboardChartsDTO(String subject, int created, int target) {
        this.subject = subject;
        this.created = created;
        this.target = target;
        this.percentage = target != 0 ? (float) created / target * 100 : 0;
    }

    public float getPercentage() {
        return percentage;
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
                ", percentage=" + percentage +
                '}';
    }
}
