package com.legalsearch.models;

public class LegalCase {
    private String caseId;
    private String title;
    private String requiredSpecialization;
    private String assignedLawyerId;

    public LegalCase(String caseId, String title, String requiredSpecialization) {
        this.caseId = caseId;
        this.title = title;
        this.requiredSpecialization = requiredSpecialization;
        this.assignedLawyerId = null;
    }

    public String getCaseId() { return caseId; }
    public String getTitle() { return title; }
    public String getRequiredSpecialization() { return requiredSpecialization; }
    public String getAssignedLawyerId() { return assignedLawyerId; }
    public void setAssignedLawyerId(String lawyerId) { this.assignedLawyerId = lawyerId; }
}