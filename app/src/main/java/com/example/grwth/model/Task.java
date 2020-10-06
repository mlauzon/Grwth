package com.example.grwth.model;

public class Task {
    public int id;
    public String checklist;
    public String description;
    public String skills;
    public Boolean status;

    // Constructor for adding empty tasks
    public Task() {
        checklist = "";
        description = "";
        skills = "";
        status = false;
    }

    // Constructor for loading tasks from database
    public Task(int task_id, String checklist_name, String description_text, String skill_tags, Boolean completion_status) {
        id = task_id;
        checklist = checklist_name;
        description = description_text;
        skills = skill_tags;
        status = completion_status;
    }

    public int getId() { return id; }

    public void setId(int task_id) { id = task_id; }

    public String getChecklist() {
        return checklist;
    }

    public void setChecklist(String checklist_name) {
        checklist = checklist_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description_text) {
        description = description_text;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skill_tags){
        skills = skill_tags;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean completion_status) {
        status = completion_status;
    }
}
