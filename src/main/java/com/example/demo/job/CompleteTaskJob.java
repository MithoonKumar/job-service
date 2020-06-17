package com.example.demo.job;


import com.example.demo.model.Task;
import com.example.demo.model.TaskExecution;
import com.example.demo.model.TaskStatus;
import com.example.demo.model.TaskType;
import com.example.demo.service.TaskExecutionService;
import com.example.demo.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class CompleteTaskJob implements Runnable{
    private Logger logger = LoggerFactory.getLogger(CompleteTaskJob.class.getName());

    private TaskService taskService;
    private TaskExecutionService taskExecutionService;

    public CompleteTaskJob(TaskService taskService, TaskExecutionService taskExecutionService) {
        this.taskService = taskService;
        this.taskExecutionService = taskExecutionService;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (taskService) {
                Task newTask = taskService.getTask();
                if (newTask != null) {
                    try {
                        this.processTask(newTask);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        logger.error("Error while processing tasks", e);
                    }
                }
            }
        }
    }


    private void processTask(Task task) throws InterruptedException {
        TaskExecution taskExecution = new TaskExecution();
        taskExecution.setStartTime(new Date());
        logger.info("Started processing task with id: " + task.getTaskId());
        Thread.sleep(task.getDuration());
        logger.info("Completed processing task with id: " + task.getTaskId());
        taskExecution.setEndTime(new Date());
        taskExecution.setTaskId(task.getTaskId());
        this.updateTask(task, taskExecution);
    }

    private void updateTask(Task task, TaskExecution taskExecution) {
        this.taskService.removeTaskFromProcessingMap(task);
        if (task.getTaskType().equals(TaskType.ONE_TIME)) {
            this.taskService.changeTaskStatus(task.getTaskId(), TaskStatus.COMPLETED);
        } else {
            this.taskService.addProcessedRecurringTask(task);
        }
        this.taskExecutionService.addTaskExecutionEntry(taskExecution);
    }

}
