package com.kdp.quest.model;


import android.support.annotation.NonNull;

public class Task {
    private static final String TASK_DIR = "TrackingResult/";
    private static final String TASK_FORMAT = ".png";

    private String name;
    private String answer;

    public Task(String name, String answer) {
        this.name = name;
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public String getAnswer() {
        return answer;
    }

    public String getPathTaskFile() {
        return TASK_DIR + name + TASK_FORMAT;
    }

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
