package com.uth.quizclear.dto;

import java.util.List;

public class SLDashboardChartDTO {
    private SubjectProgressData progressData;
    private OverallProgressData overallProgress;
    
    public SLDashboardChartDTO() {}
    
    public static class SubjectProgressData {
        private List<String> subjects;
        private List<Integer> completed;
        private List<Integer> targets;
        
        public SubjectProgressData() {}
        
        public SubjectProgressData(List<String> subjects, List<Integer> completed, List<Integer> targets) {
            this.subjects = subjects;
            this.completed = completed;
            this.targets = targets;
        }
        
        public List<String> getSubjects() {
            return subjects;
        }
        
        public void setSubjects(List<String> subjects) {
            this.subjects = subjects;
        }
        
        public List<Integer> getCompleted() {
            return completed;
        }
        
        public void setCompleted(List<Integer> completed) {
            this.completed = completed;
        }
        
        public List<Integer> getTargets() {
            return targets;
        }
        
        public void setTargets(List<Integer> targets) {
            this.targets = targets;
        }
    }
    
    public static class OverallProgressData {
        private double completionRate;
        private int totalCompleted;
        private int totalTarget;
        
        public OverallProgressData() {}
        
        public OverallProgressData(double completionRate, int totalCompleted, int totalTarget) {
            this.completionRate = completionRate;
            this.totalCompleted = totalCompleted;
            this.totalTarget = totalTarget;
        }
        
        public double getCompletionRate() {
            return completionRate;
        }
        
        public void setCompletionRate(double completionRate) {
            this.completionRate = completionRate;
        }
        
        public int getTotalCompleted() {
            return totalCompleted;
        }
        
        public void setTotalCompleted(int totalCompleted) {
            this.totalCompleted = totalCompleted;
        }
        
        public int getTotalTarget() {
            return totalTarget;
        }
        
        public void setTotalTarget(int totalTarget) {
            this.totalTarget = totalTarget;
        }
    }
    
    public SubjectProgressData getProgressData() {
        return progressData;
    }
    
    public void setProgressData(SubjectProgressData progressData) {
        this.progressData = progressData;
    }
    
    public OverallProgressData getOverallProgress() {
        return overallProgress;
    }
    
    public void setOverallProgress(OverallProgressData overallProgress) {
        this.overallProgress = overallProgress;
    }
}
