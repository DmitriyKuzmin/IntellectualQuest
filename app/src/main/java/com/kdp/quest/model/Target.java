package com.kdp.quest.model;


import android.support.annotation.NonNull;

public class Target {
    private static final String TARGET_DIR = "ImageTarget/";
    private static final String TARGET_MAP_FORMAT = ".2dmap";
    private static final String TARGET_IMAGE_FORMAT = ".jpg";

    private String name;
    private String description;

    public Target(String name, String description) {
        this.name = name;
        this.description = description;
    }

    String getPathTargetMapFile() {
        return TARGET_DIR + name + TARGET_MAP_FORMAT;
    }
    public String getPathTargetImageFile(){
        return TARGET_DIR + name + "_resized" + TARGET_IMAGE_FORMAT;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    @NonNull
    @Override
    public String toString() {
        return "Target{" +
                "name='" + name + '\'' +
                '}';
    }
}
