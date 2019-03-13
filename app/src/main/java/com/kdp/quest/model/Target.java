package com.kdp.quest.model;


import android.support.annotation.NonNull;

public class Target {
    private static final String TARGET_DIR = "ImageTarget/";
    private static final String TARGET_FORMAT = ".2dmap";

    private String name;


    public Target(String name) {
        this.name = name;
    }

    String getPathTargetFile() {
        return TARGET_DIR + name + TARGET_FORMAT;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Target{" +
                "name='" + name + '\'' +
                '}';
    }
}
