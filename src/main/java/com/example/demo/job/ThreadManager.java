package com.example.demo.job;

import com.example.demo.constants.AppConstants;
import com.example.demo.service.TaskExecutionService;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadManager {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskExecutionService taskExecutionService;

    public void startProcessing () throws InterruptedException {
        Thread []threads = new Thread[AppConstants.NUMBER_OF_THREADS];
        for (int index = 0; index < threads.length; index++) {
            threads[index] = new Thread(new CompleteTaskJob(taskService, taskExecutionService));
            threads[index].start();
        }
    }
}
