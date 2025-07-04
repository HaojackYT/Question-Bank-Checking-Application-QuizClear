package com.uth.quizclear.model.dto;

public class SLDashboardStatsDTO {
    private int pendingAssignments;
    private int newAssignments;
    private int activeLecturers;
    private int totalLecturersAssigned;
    private int questionsNeedingReview;
    private int newReviews;
    private double completionRate;
    private String completionTrend;
    
    // Default constructor
    public SLDashboardStatsDTO() {}
    
    // Full constructor
    public SLDashboardStatsDTO(int pendingAssignments, int newAssignments, 
                              int activeLecturers, int totalLecturersAssigned,
                              int questionsNeedingReview, int newReviews,
                              double completionRate, String completionTrend) {
        this.pendingAssignments = pendingAssignments;
        this.newAssignments = newAssignments;
        this.activeLecturers = activeLecturers;
        this.totalLecturersAssigned = totalLecturersAssigned;
        this.questionsNeedingReview = questionsNeedingReview;
        this.newReviews = newReviews;
        this.completionRate = completionRate;
        this.completionTrend = completionTrend;
    }
    
    // Getters and setters
    public int getPendingAssignments() {
        return pendingAssignments;
    }
    
    public void setPendingAssignments(int pendingAssignments) {
        this.pendingAssignments = pendingAssignments;
    }
    
    public int getNewAssignments() {
        return newAssignments;
    }
    
    public void setNewAssignments(int newAssignments) {
        this.newAssignments = newAssignments;
    }
    
    public int getActiveLecturers() {
        return activeLecturers;
    }
    
    public void setActiveLecturers(int activeLecturers) {
        this.activeLecturers = activeLecturers;
    }
    
    public int getTotalLecturersAssigned() {
        return totalLecturersAssigned;
    }
    
    public void setTotalLecturersAssigned(int totalLecturersAssigned) {
        this.totalLecturersAssigned = totalLecturersAssigned;
    }
    
    public int getQuestionsNeedingReview() {
        return questionsNeedingReview;
    }
    
    public void setQuestionsNeedingReview(int questionsNeedingReview) {
        this.questionsNeedingReview = questionsNeedingReview;
    }
    
    public int getNewReviews() {
        return newReviews;
    }
    
    public void setNewReviews(int newReviews) {
        this.newReviews = newReviews;
    }
    
    public double getCompletionRate() {
        return completionRate;
    }
    
    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }
    
    public String getCompletionTrend() {
        return completionTrend;
    }
    
    public void setCompletionTrend(String completionTrend) {
        this.completionTrend = completionTrend;
    }
}
