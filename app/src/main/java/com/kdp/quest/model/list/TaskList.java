package com.kdp.quest.model.list;

import com.kdp.quest.model.Task;

import java.util.List;
import java.util.ListIterator;

public class TaskList {
    private static TaskList instance;

    private ListIterator<Task> taskIterator;
    private List<Task> tasks;
    private Integer currentIterator = 0;
    private Integer countTasks;

    public static TaskList getInstance() {
        if (instance == null)
            instance = new TaskList();

        return instance;
    }

    private TaskList() {
    }

    public void setData(List<Task> tasks) {
        this.tasks = tasks;
        countTasks = tasks.size();
        taskIterator = tasks.listIterator();
        this.nextTask();
    }

    public void nextTask() {
        if (taskIterator.hasNext()) {
            currentIterator = taskIterator.nextIndex();
            taskIterator.next();
        } else
            currentIterator = countTasks;
    }

    public Task getCurrentTask() {
        if (currentIterator != null)
            return tasks.get(currentIterator);

        return null;
    }

    public Integer getCurrentIterator() {
        return currentIterator;
    }

    public Integer getCountTasks() {
        return countTasks;
    }


    public void resetIterator() {
        taskIterator = tasks.listIterator(0);
    }
}
