package com.example.grwth.model;

public class Goal {
    public int id;

    public String startDate;
    public String completionDate;
    public String goalDate;
    public String skills;
    public int rewardId;

    public int getId() { return id; }

    public void setId(int goalId) { id = goalId; }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String date) {
        startDate = date;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String date) {
        completionDate = date;
    }

    public String getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(String date) {
        goalDate = date;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String taggedSkills) {
        skills = taggedSkills;
    }

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardIdNum) {
        rewardId = rewardIdNum;
    }
}
