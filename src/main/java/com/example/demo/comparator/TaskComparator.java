package com.example.demo.comparator;

import com.example.demo.model.Task;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        int decisionVal = task1.getScheduledStartTime().compareTo(task2.getScheduledStartTime());
        if (decisionVal == 0) {
            return task1.getTaskPriority().compareTo(task2.getTaskPriority());
        }
        return decisionVal;
    }
}
