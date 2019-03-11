package com.kdp.quest;


public class Target {
    private static final String TARGET_DIR = "ImageTarget/";
    private static final String TARGET_FORMAT = ".2dmap";

    private String name;


    Target(String name) {
        this.name = name;
    }

    public String getPathTargetFile() {
        return TARGET_DIR + name + TARGET_FORMAT;
    }

    String getName() {
        return name;
    }
}
