package com.example.demo.service;

import com.example.demo.model.TaskExecution;
import com.example.demo.repository.TaskExecutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskExecutionService {

    private Logger logger = LoggerFactory.getLogger(TaskExecutionService.class.getName());

    @Autowired
    private TaskExecutionRepository taskExecutionRepository;

    public void addTaskExecutionEntry(TaskExecution taskExecution) {
        try {
            taskExecutionRepository.addTaskExecution(taskExecution);
        } catch (Exception e) {
            logger.error("Error while putting entry in taskExecution table", e);
        }
    }

    public List<TaskExecution> getAllTaskExecutions(Long startTime, Long endTime) {
        try {
            Date fromTime = new Date(startTime), toTime = new Date(endTime);
            return this.taskExecutionRepository.getAllExecutions(fromTime, toTime);
        } catch (Exception e) {
            logger.error("Error while fetching taskExecutions from repository", e);
        }
        return null;
    }

}
