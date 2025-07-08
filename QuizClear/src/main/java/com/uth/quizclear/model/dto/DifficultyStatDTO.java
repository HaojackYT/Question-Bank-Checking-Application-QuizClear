package com.uth.quizclear.model.dto;

public class DifficultyStatDTO {
    private String difficultyLevel;
    private long count;
    private double percent;

    public DifficultyStatDTO() {}

    public DifficultyStatDTO(String difficultyLevel, long count, double percent) {
        this.difficultyLevel = difficultyLevel;
        this.count = count;
        this.percent = percent;
    }

    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }

    public double getPercent() { return percent; }
    public void setPercent(double percent) { this.percent = percent; }
}