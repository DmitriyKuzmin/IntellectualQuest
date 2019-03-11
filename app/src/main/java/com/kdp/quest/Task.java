package com.kdp.quest;


public class Task {
    private static final String TASK_DIR = "TrackingResult/";
    private static final String TASK_FORMAT = ".png";

    private String name;
    private String answer;

    Task(String name, String answer) {
        this.name = name;
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public String getAnswer() {
        return answer;
    }

    String getPathTaskFile() {
        return TASK_DIR + name + TASK_FORMAT;
    }
}
