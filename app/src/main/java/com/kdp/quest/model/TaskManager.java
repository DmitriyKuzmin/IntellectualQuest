package com.kdp.quest.model;

import java.util.List;
import java.util.ListIterator;

public class TaskManager {
    private static TaskManager instance;

    private ListIterator<Task> taskIterator;
    private List<Task> tasks;
    private Integer currentIterator = 0;


    public static TaskManager getInstance(List<Task> tasks) {
        if (instance == null)
            instance = new TaskManager(tasks);

        return instance;
    }

    private TaskManager(List<Task> tasks) {
        this.tasks = tasks;
        taskIterator = tasks.listIterator();
    }

    public void nextTask() {
        if (taskIterator.hasNext()) {
            currentIterator = taskIterator.nextIndex();
            taskIterator.next();
        }
    }

    public Task getCurrentTask() {
        if (currentIterator != null)
            return tasks.get(currentIterator);

        return null;
    }
}
