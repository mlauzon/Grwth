package com.example.grwth.model;

public class Skill {
    public int id;
    public String description;
    public int level;

    // Constructor for adding empty Skills
    public Skill() {
        description = "";
        level = 10;
    }

    // Constructor for loading skills from database
    public Skill(int skill_id, String skill_description, int skill_level) {
        id = skill_id;
        description = skill_description;
        level = skill_level;
    }

    public int getId() { return id; }

    public void setId(int skillId) { id = skillId;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String skillDescription) {
        description = skillDescription;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int skillLevel) {
        level = skillLevel;
    }
}
