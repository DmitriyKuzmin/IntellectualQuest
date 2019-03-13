package com.kdp.quest.model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TargetManager {
    private static TargetManager instance;

    private ListIterator<Target> targetIterator;
    private List<Target> targets;
    private Integer currentIterator = 0;

    private TargetManager(ArrayList<Target> targets) {
        this.targets = targets;
        targetIterator = targets.listIterator();
    }

    public static TargetManager getInstance(ArrayList<Target> targets) {
        if (instance == null)
            instance = new TargetManager(targets);

        return instance;
    }

    public Target getNextTarget() {
        if (targetIterator.hasNext()) {
            currentIterator = targetIterator.nextIndex();
            return targetIterator.next();
        }
        return null;
    }

    public Target getCurrentTarget() {
        if (currentIterator != null)
            return targets.get(currentIterator);

        return null;
    }

    public ArrayList<String> getTrackingFileName() {
        ArrayList<String> trackingFileName = new ArrayList<>();
        for (Target target : targets) {
            trackingFileName.add(target.getPathTargetFile());
        }
        return trackingFileName;
    }
}
