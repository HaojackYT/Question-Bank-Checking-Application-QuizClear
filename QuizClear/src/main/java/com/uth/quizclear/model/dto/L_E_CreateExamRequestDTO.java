package com.uth.quizclear.model.dto;

public class L_E_CreateExamRequestDTO {

    private String requester;
    private String timeExam;
    private String subject;
    private String deadline;
    private int remember;
    private int understand;
    private int applyBasic;
    private int applyAdvanced;

    public L_E_CreateExamRequestDTO() {
    }

    public L_E_CreateExamRequestDTO(String requester, String timeExam, String subject, String deadline, int remember, int understand, int applyBasic, int applyAdvanced) {
        this.requester = requester;
        this.timeExam = timeExam;
        this.subject = subject;
        this.deadline = deadline;
        this.remember = remember;
        this.understand = understand;
        this.applyBasic = applyBasic;
        this.applyAdvanced = applyAdvanced;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getTimeExam() {
        return timeExam;
    }

    public void setTimeExam(String timeExam) {
        this.timeExam = timeExam;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getRemember() {
        return remember;
    }

    public void setRemember(int remember) {
        this.remember = remember;
    }

    public int getUnderstand() {
        return understand;
    }

    public void setUnderstand(int understand) {
        this.understand = understand;
    }

    public int getApplyBasic() {
        return applyBasic;
    }

    public void setApplyBasic(int applyBasic) {
        this.applyBasic = applyBasic;
    }

    public int getApplyAdvanced() {
        return applyAdvanced;
    }

    public void setApplyAdvanced(int applyAdvanced) {
        this.applyAdvanced = applyAdvanced;
    }
}
