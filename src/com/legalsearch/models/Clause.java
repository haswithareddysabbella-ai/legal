package com.legalsearch.models;

public class Clause {
    private String clauseId;
    private String category;
    private String text;
    private double riskScore;

    public Clause(String clauseId, String category, String text, double riskScore) {
        this.clauseId = clauseId;
        this.category = category;
        this.text = text;
        this.riskScore = riskScore;
    }

    public String getClauseId() { return clauseId; }
    public String getCategory() { return category; }
    public String getText() { return text; }
    public double getRiskScore() { return riskScore; }
}