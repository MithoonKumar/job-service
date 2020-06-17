package com.example.demo.repository;

import com.example.demo.comparator.TaskComparator;
import com.example.demo.model.Task;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.PriorityQueue;

@Repository
public class ActiveTaskRepository {
    private PriorityQueue<Task> taskPriorityQueue;
    private HashMap<String, Task> tasksBeingProcessed;

    public Task getTask () {
        Date now = new Date();
        Task newTask = null;
        synchronized (taskPriorityQueue) {
            if (taskPriorityQueue.size() > 0 && now.getTime() > taskPriorityQueue.peek().getScheduledStartTime().getTime()) {
                newTask = taskPriorityQueue.peek();
                taskPriorityQueue.poll();
                addTaskToProcessingTaskMap(newTask);
            }
        }
        return newTask;
    }

    public ActiveTaskRepository() {
        this.taskPriorityQueue = new PriorityQueue<>(new TaskComparator());
        this.tasksBeingProcessed = new HashMap<>();
    }

    public void addTaskToPriorityQueue (Task task) {
        synchronized (taskPriorityQueue) {
            taskPriorityQueue.add(task);
        }
    }

    public void addTaskToProcessingTaskMap(Task task) {
        tasksBeingProcessed.put(task.getTaskId(), task);
    }

    public void removeTaskFromProcessingTaskMap(Task task) {
        tasksBeingProcessed.remove(task.getTaskId());
    }

    public boolean isTaskBeingProcessed(Task task) {
        return tasksBeingProcessed.containsKey(task.getTaskId());
    }

    public void removeTaskFromPriorityQueue (Task task) {
        synchronized (taskPriorityQueue) {
            taskPriorityQueue.remove(task);
        }
    }

}
