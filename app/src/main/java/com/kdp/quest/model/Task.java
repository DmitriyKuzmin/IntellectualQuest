package com.kdp.quest.model;

import androidx.annotation.NonNull;

public class Task {
    private static final String TASK_DIR = "TrackingResult/";
    private static final String TASK_FORMAT = ".png";

    private String name;
    private String answer;
    private String userAnswer;
    private Boolean isTrueUserAnswer;

    public Task(String name, String answer) {
        this.name = name;
        this.answer = answer;
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

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public void setTrueUserAnswer(Boolean trueUserAnswer) {
        isTrueUserAnswer = trueUserAnswer;
    }
}
