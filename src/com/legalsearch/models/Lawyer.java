package com.legalsearch.models;

import java.util.List;

public class Lawyer {
    private String id;
    private String name;
    private String specialization;
    private int capacity;

    public Lawyer(String id, String name, String specialization, int capacity) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.capacity = capacity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public int getCapacity() { return capacity; }
}