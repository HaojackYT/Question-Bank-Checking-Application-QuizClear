package com.uth.quizclear.model.dto;

import java.util.List;
import java.util.Map;

public class ExamOverviewDTO {
    private Long examId;
    private String examTitle;
    private String examCode;
    private String courseName;
    private String semester;
    private String academicYear;
    private int totalQuestions;
    private int versionCount;
    private String deadline;
    private String createdDate;
    private List<TeamMemberDTO> assignedTeam;
    private Map<String, DifficultyStat> difficultyStats;

    public ExamOverviewDTO(Long examId, String examTitle, String examCode, String courseName, String semester,
            String academicYear, int totalQuestions, int versionCount, String deadline, String createdDate,
            List<TeamMemberDTO> assignedTeam, Map<String, DifficultyStat> difficultyStats) {
        this.examId = examId;
        this.examTitle = examTitle;
        this.examCode = examCode;
        this.courseName = courseName;
        this.semester = semester;
        this.academicYear = academicYear;
        this.totalQuestions = totalQuestions;
        this.versionCount = versionCount;
        this.deadline = deadline;
        this.createdDate = createdDate;
        this.assignedTeam = assignedTeam;
        this.difficultyStats = difficultyStats;
    }

    public static class TeamMemberDTO {
        private String fullName;
        private String role;
        private String avatarLetter;
        
        public TeamMemberDTO(String fullName, String role, String avatarLetter) {
            this.fullName = fullName;
            this.role = role;
            this.avatarLetter = avatarLetter;
        }
        
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        
        public String getAvatarLetter() { return avatarLetter; }
        public void setAvatarLetter(String avatarLetter) { this.avatarLetter = avatarLetter; }
    }

    public static class DifficultyStat {
        private String label;
        private int count;
        private int percent;
        
        public DifficultyStat(String label, int count, int percent) {
            this.label = label;
            this.count = count;
            this.percent = percent;
        }
        
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }

        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        
        public int getPercent() { return percent; }
        public void setPercent(int percent) { this.percent = percent; }
    }

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public String getExamCode() { return examCode; }
    public void setExamCode(String examCode) { this.examCode = examCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getVersionCount() { return versionCount; }
    public void setVersionCount(int versionCount) { this.versionCount = versionCount; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public List<TeamMemberDTO> getAssignedTeam() { return assignedTeam; }
    public void setAssignedTeam(List<TeamMemberDTO> assignedTeam) { this.assignedTeam = assignedTeam; }

    public Map<String, DifficultyStat> getDifficultyStats() { return difficultyStats; }
    public void setDifficultyStats(Map<String, DifficultyStat> difficultyStats) { this.difficultyStats = difficultyStats; }
}
