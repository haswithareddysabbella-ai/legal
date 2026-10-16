package com.legalsearch.models;

import java.util.ArrayList;
import java.util.List;

public class Document {
    private String id;
    private String title;
    private String content;
    private List<Clause> clauses;

    public Document(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.clauses = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public List<Clause> getClauses() { return clauses; }

    public void addClause(Clause clause) {
        this.clauses.add(clause);
    }
}