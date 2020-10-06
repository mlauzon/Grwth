package com.example.grwth.model;

public class Checklist {
    public int id;

    public String name;

    public int getId() { return id; }

    public void setId(int taskId) { id = taskId; }

    public String getName() {
        return name;
    }

    public void setName(String checklistName) {
        name = checklistName;
    }
}
