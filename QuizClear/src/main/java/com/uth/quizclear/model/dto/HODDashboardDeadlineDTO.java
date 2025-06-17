package com.uth.quizclear.model.dto;

public class HODDashboardDeadlineDTO {
    private String title;
    private String course;
    private String note;

    public HODDashboardDeadlineDTO(String title, String course, String note) {
        this.title = title;
        this.course = course;
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public String getCourse() {
        return course;
    }

    public String getNote() {
        return note;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
