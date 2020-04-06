package com.example.translate.ui.dashboard;

public class Achievement {

    private String id;
    private String name;
    private String description;
    private int currentProgress;
    private int totalProgress;
    private String complete;


    public Achievement() {
    }

    public Achievement(String id, String name, String description, int currentProgress, int totalProgress, String complete) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.currentProgress = currentProgress;
        this.totalProgress = totalProgress;
        this.complete = complete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public int getProgressTotal() {
        return totalProgress;
    }

    public void setProgressTotal(int progressTotal) {
        this.totalProgress = totalProgress;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }


}


