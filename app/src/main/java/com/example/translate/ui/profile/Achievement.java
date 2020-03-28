package com.example.translate.ui.profile;

import java.util.ArrayList;

public class Achievement {
    private String name;
    private String description;
    private int currentProgress;
    private int totalProgress;
    private boolean isAchieved;

    public Achievement() {

    }

    public Achievement(String name, String description, int currentProgress, int totalProgress, boolean isAchieved) {
        this.name = name;
        this.description = description;
        this.currentProgress = currentProgress;
        this.totalProgress = totalProgress;
        this.isAchieved = isAchieved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAchieved() {
        return isAchieved;
    }

    public void setAchieved(boolean achieved) {
        isAchieved = achieved;
    }

    public int getTotalProgress() {
        return totalProgress;
    }

    public void setTotalProgress(int totalProgress) {
        this.totalProgress = totalProgress;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public static ArrayList<Achievement> getAchievements() {

        ArrayList<Achievement> achievementList = new ArrayList<>();

        achievementList.add(new Achievement("one", "do something", 1, 10, false));
        achievementList.add(new Achievement("two", "do something", 2, 10, false));
        achievementList.add(new Achievement("three", "do something", 3, 10, false));
        achievementList.add(new Achievement("four", "do something", 4, 10, false));
        achievementList.add(new Achievement("five", "do something", 5, 10, false));
        achievementList.add(new Achievement("six", "do something", 6, 10, false));
        achievementList.add(new Achievement("seven", "do something", 7, 10, false));
        achievementList.add(new Achievement("eight", "do something", 8, 10, false));
        achievementList.add(new Achievement("nine", "do something", 9, 10, false));
        achievementList.add(new Achievement("ten", "do something", 10, 10, false));

        return achievementList;
    }

}

