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

        achievementList.add(new Achievement("Number Novice", "Complete the first level: Numbers", 1, 1, false));
        achievementList.add(new Achievement("Good Greeter", "Complete the second level: Essentials", 2, 10, false));
        achievementList.add(new Achievement("Food Fighter", "Complete the third level: Food", 3, 10, false));
        achievementList.add(new Achievement("Helping Hand", "Complete the fourth level: Help", 4, 10, false));
        achievementList.add(new Achievement("Perseverance", "Revise your saved words" , 5, 10, false));
        achievementList.add(new Achievement("Dedicated", "Revise your mastered words", 6, 10, false));
        achievementList.add(new Achievement("Quick Quick Quick", "Complete a test in under 30 seconds", 7, 10, false));
        achievementList.add(new Achievement("Self-improver", "Check out all components in your profile", 9, 10, false));
        achievementList.add(new Achievement("Lingo Learner", "Complete all learning tasks", 8, 10, false));
        achievementList.add(new Achievement("Lingo Legend", "Complete a level without any mistakes", 10, 10, false));

        return achievementList;
    }

}

