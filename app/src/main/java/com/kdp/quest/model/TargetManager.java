package com.kdp.quest.model;



import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TargetManager {
    private static TargetManager instance;

    private ListIterator<Target> targetIterator;
    private List<Target> targets;
    private Integer currentIterator = 0;

    private TargetManager(List<Target> targets) {
        this.targets = targets;
        targetIterator = targets.listIterator();
    }

    public static TargetManager getInstance(List<Target> targets) {
        if (instance == null)
            instance = new TargetManager(targets);

        return instance;
    }

    public void nextTarget() {
        if (targetIterator.hasNext()) {
            currentIterator = targetIterator.nextIndex();
            targetIterator.next();
        }
    }

    public Target getCurrentTarget() {
        if (currentIterator != null)
            return targets.get(currentIterator);

        return null;
    }

    public List<String> getTrackingFileName() {
        List<String> trackingFileName = new ArrayList<>();
        for (Target target : targets) {
            trackingFileName.add(target.getPathTargetFile());
        }
        return trackingFileName;
    }
}
