package com.uth.quizclear.model.dto;

import java.time.LocalDate;
import java.util.List;

public class ExamCreateDTO {

    // Step1
    private String examTitle;
    private String examCode;
    private Long subjectId;
    private Long courseId;
    private String semester;
    private String examType;
    private LocalDate deadlineDate;
    private Integer durationMinutes;
    private String description;
    private String instructions;
    // Các trường cho các bước tiếp theo (structure, assignment, review)

    // Step2
    private Integer totalQuestions;
    private Integer totalVersions;
    private Integer percentBasic;
    private Integer percentIntermediate;
    private Integer percentAdvanced;
    private Integer percentExpert;
    private Boolean checkDuplicateQuestions;
    private Boolean checkCloCoverage;
    private Boolean randomizeQuestionOrder;
    private Boolean randomizeAnswerOptions;

    // Step3
    private Long departmentHeadId;
    private Long subjectExpertId;
    private Long reviewerId;
    private java.time.LocalDate questionCreationDeadline;
    private java.time.LocalDate reviewDeadline;
    private Boolean notifyByEmail;
    private Boolean notifyDeadlineReminders;
    private Boolean notifyProgressUpdates;
    private Boolean notifyFeedback;
    private String additionalInstructions;

    // Step 4
    private List<CloDistributionDTO> cloDistributions;

    public class CloDistributionDTO { 
        String name;
        Integer percent;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getPercent() { return percent; }
        public void setPercent(Integer percent) { this.percent = percent; }
    }

    public ExamCreateDTO() { }

    // Step1
    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public String getExamCode() { return examCode; }
    public void setExamCode(String examCode) { this.examCode = examCode; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }

    public LocalDate getDeadlineDate() { return deadlineDate; }
    public void setDeadlineDate(LocalDate deadlineDate) { this.deadlineDate = deadlineDate; }
    
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    // Step 2
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public Integer getTotalVersions() { return totalVersions; }
    public void setTotalVersions(Integer totalVersions) { this.totalVersions = totalVersions; }

    public Integer getPercentBasic() { return percentBasic; }
    public void setPercentBasic(Integer percentBasic) { this.percentBasic = percentBasic; }

    public Integer getPercentIntermediate() { return percentIntermediate; }
    public void setPercentIntermediate(Integer percentIntermediate) { this.percentIntermediate = percentIntermediate; }

    public Integer getPercentAdvanced() { return percentAdvanced; }
    public void setPercentAdvanced(Integer percentAdvanced) { this.percentAdvanced = percentAdvanced; }

    public Integer getPercentExpert() { return percentExpert; }
    public void setPercentExpert(Integer percentExpert) { this.percentExpert = percentExpert; }

    public Boolean getCheckDuplicateQuestions() { return checkDuplicateQuestions; }
    public void setCheckDuplicateQuestions(Boolean checkDuplicateQuestions) { this.checkDuplicateQuestions = checkDuplicateQuestions; }

    public Boolean getCheckCloCoverage() { return checkCloCoverage; }
    public void setCheckCloCoverage(Boolean checkCloCoverage) { this.checkCloCoverage = checkCloCoverage; }

    public Boolean getRandomizeQuestionOrder() { return randomizeQuestionOrder; }
    public void setRandomizeQuestionOrder(Boolean randomizeQuestionOrder) { this.randomizeQuestionOrder = randomizeQuestionOrder; }

    public Boolean getRandomizeAnswerOptions() { return randomizeAnswerOptions; }
    public void setRandomizeAnswerOptions(Boolean randomizeAnswerOptions) { this.randomizeAnswerOptions = randomizeAnswerOptions; }

    // Step3
    public Long getDepartmentHeadId() { return departmentHeadId; }
    public void setDepartmentHeadId(Long departmentHeadId) { this.departmentHeadId = departmentHeadId; }

    public Long getSubjectExpertId() { return subjectExpertId; }
    public void setSubjectExpertId(Long subjectExpertId) { this.subjectExpertId = subjectExpertId; }

    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }

    public java.time.LocalDate getQuestionCreationDeadline() { return questionCreationDeadline; }
    public void setQuestionCreationDeadline(java.time.LocalDate questionCreationDeadline) { this.questionCreationDeadline = questionCreationDeadline; }

    public java.time.LocalDate getReviewDeadline() { return reviewDeadline; }
    public void setReviewDeadline(java.time.LocalDate reviewDeadline) { this.reviewDeadline = reviewDeadline; }

    public Boolean getNotifyByEmail() { return notifyByEmail; }
    public void setNotifyByEmail(Boolean notifyByEmail) { this.notifyByEmail = notifyByEmail; }

    public Boolean getNotifyDeadlineReminders() { return notifyDeadlineReminders; }
    public void setNotifyDeadlineReminders(Boolean notifyDeadlineReminders) { this.notifyDeadlineReminders = notifyDeadlineReminders; }

    public Boolean getNotifyProgressUpdates() { return notifyProgressUpdates; }
    public void setNotifyProgressUpdates(Boolean notifyProgressUpdates) { this.notifyProgressUpdates = notifyProgressUpdates; }

    public Boolean getNotifyFeedback() { return notifyFeedback; }
    public void setNotifyFeedback(Boolean notifyFeedback) { this.notifyFeedback = notifyFeedback; }

    public String getAdditionalInstructions() { return additionalInstructions; }
    public void setAdditionalInstructions(String additionalInstructions) { this.additionalInstructions = additionalInstructions; }

    // Step4
    public List<CloDistributionDTO> getCloDistributions() { return cloDistributions; }
    public void setCloDistributions(List<CloDistributionDTO> cloDistributions) { this.cloDistributions = cloDistributions; }

}
