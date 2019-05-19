package com.kdp.quest.model.list;



import com.kdp.quest.model.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TargetList {
    private static TargetList instance;

    private ListIterator<Target> targetIterator;
    private List<Target> targets;
    private Integer currentIterator = 0;
    private Integer countTargets;


    public static TargetList getInstance() {
        if (instance == null)
            instance = new TargetList();

        return instance;
    }

    public void setData(List<Target> targets){
        this.targets = targets;
        countTargets = targets.size();
        targetIterator = targets.listIterator();
        this.nextTarget();
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
            trackingFileName.add(target.getPathTargetMapFile());
        }
        return trackingFileName;
    }

    public Integer getCountTargets() {
        return countTargets;
    }

    public void resetIterator(){
        targetIterator = targets.listIterator(0);
    }
}
