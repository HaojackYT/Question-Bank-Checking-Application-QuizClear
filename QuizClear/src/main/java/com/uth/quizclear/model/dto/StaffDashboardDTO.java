package com.uth.quizclear.model.dto;

import java.util.List;

public class StaffDashboardDTO {
    // Thống kê tổng quan
    private int totalSubjects;
    private int totalQuestions;
    private int duplicateQuestions;
    private int examsCreated;
    private int subjectsThisMonth;
    private int questionsThisMonth;
    private int examsThisMonth;

    // Biểu đồ
    private ChartDataDTO barChart;
    private ChartDataDTO pieChart;

    // Danh sách nhiệm vụ gần đây
    private List<TaskDTO> recentTasks;

    // Danh sách cảnh báo trùng lặp
    private List<DuplicateWarningDTO> duplicateWarnings;
    private int totalDuplicateWarnings; // Tổng số cảnh báo trùng lặp (để kiểm tra logic "See more...")

    // Danh sách nhiệm vụ đầy đủ cho See more
    private List<TaskDTO> allTasks;

    // Getter & Setter
    public int getTotalSubjects() {
        return totalSubjects;
    }

    public void setTotalSubjects(int totalSubjects) {
        this.totalSubjects = totalSubjects;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getDuplicateQuestions() {
        return duplicateQuestions;
    }

    public void setDuplicateQuestions(int duplicateQuestions) {
        this.duplicateQuestions = duplicateQuestions;
    }

    public int getExamsCreated() {
        return examsCreated;
    }

    public void setExamsCreated(int examsCreated) {
        this.examsCreated = examsCreated;
    }

    public int getSubjectsThisMonth() {
        return subjectsThisMonth;
    }

    public void setSubjectsThisMonth(int subjectsThisMonth) {
        this.subjectsThisMonth = subjectsThisMonth;
    }

    public int getQuestionsThisMonth() {
        return questionsThisMonth;
    }

    public void setQuestionsThisMonth(int questionsThisMonth) {
        this.questionsThisMonth = questionsThisMonth;
    }

    public int getExamsThisMonth() {
        return examsThisMonth;
    }

    public void setExamsThisMonth(int examsThisMonth) {
        this.examsThisMonth = examsThisMonth;
    }

    public ChartDataDTO getBarChart() {
        return barChart;
    }

    public void setBarChart(ChartDataDTO barChart) {
        this.barChart = barChart;
    }

    public ChartDataDTO getPieChart() {
        return pieChart;
    }

    public void setPieChart(ChartDataDTO pieChart) {
        this.pieChart = pieChart;
    }

    public List<TaskDTO> getRecentTasks() {
        return recentTasks;
    }

    public void setRecentTasks(List<TaskDTO> recentTasks) {
        this.recentTasks = recentTasks;
    }

    public List<DuplicateWarningDTO> getDuplicateWarnings() {
        return duplicateWarnings;
    }

    public void setDuplicateWarnings(List<DuplicateWarningDTO> duplicateWarnings) {
        this.duplicateWarnings = duplicateWarnings;
    }

    public int getTotalDuplicateWarnings() {
        return totalDuplicateWarnings;
    }

    public void setTotalDuplicateWarnings(int totalDuplicateWarnings) {
        this.totalDuplicateWarnings = totalDuplicateWarnings;
    }

    public List<TaskDTO> getAllTasks() {
        return allTasks;
    }

    public void setAllTasks(List<TaskDTO> allTasks) {
        this.allTasks = allTasks;
    }
}
