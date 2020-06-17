package com.example.demo.service;

import com.example.demo.constants.AppConstants;
import com.example.demo.helper.Helper;
import com.example.demo.model.Task;
import com.example.demo.model.TaskPriority;
import com.example.demo.model.TaskStatus;
import com.example.demo.model.TaskType;
import com.example.demo.repository.ActiveTaskRepository;
import com.example.demo.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class TaskService {
    private Logger logger = LoggerFactory.getLogger(TaskService.class.getName());

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ActiveTaskRepository activeTaskRepository;

    public boolean addTask (HashMap<String, Object> requestBodyMap) {
        try {
            Task task = new Task();
            task.setTaskId((String) requestBodyMap.get(AppConstants.TASK_ID));
            Date date = new Date();
            task.setScheduledStartTime(new Date(date.getTime() + (Integer)requestBodyMap.get(AppConstants.START_TIME)));
            task.setTaskPriority(TaskPriority.valueOf((String) requestBodyMap.get(AppConstants.TASK_PRIORITY)));
            task.setTaskStatus(TaskStatus.ACTIVE);
            task.setTaskType(TaskType.valueOf((String) requestBodyMap.get(AppConstants.TASK_TYPE)));
            task.setDuration((Integer) requestBodyMap.get(AppConstants.TASK_DURATION));
            if (requestBodyMap.containsKey(AppConstants.SCHEDULE)) {
                task.setSchedule((String)requestBodyMap.get(AppConstants.SCHEDULE));
                date = Helper.getNextCronDate(task.getSchedule());
                if (date == null) {
                    throw new Exception("Error while getting next date for cron job");
                }
                task.setScheduledStartTime(date);
            }
            this.taskRepository.addTask(task);
            this.activeTaskRepository.addTaskToPriorityQueue(task);
        } catch (Exception e) {
            logger.error("Error while adding task");
            return false;
        }
        return true;
    }

    public void addProcessedRecurringTask(Task task) {
        task.setScheduledStartTime(Helper.getNextCronDate(task.getSchedule()));
        try {
            this.taskRepository.updateTaskStartTime(task.getTaskId(), task.getScheduledStartTime());
            this.activeTaskRepository.addTaskToPriorityQueue(task);
        } catch (Exception e) {
            logger.error("Error while updating data in task table", e);
        }

    }

    public boolean changeTaskStatus(String taskId, TaskStatus taskStatus) {
        try {
            Task task = this.taskRepository.getTask(taskId);
            if (task == null) {
                return false;
            }
            if (task.getTaskStatus() == TaskStatus.COMPLETED) {
                return false;
            }
            if (this.activeTaskRepository.isTaskBeingProcessed(task)) {
                return false;
            }
            if (task.getTaskStatus() == TaskStatus.ACTIVE && (taskStatus == TaskStatus.INACTIVE || taskStatus == TaskStatus.COMPLETED)) {
                this.activeTaskRepository.removeTaskFromPriorityQueue(task);
                this.taskRepository.updateTaskStatus(taskId, taskStatus);
                return true;
            }
            if (task.getTaskStatus() == TaskStatus.INACTIVE && taskStatus == TaskStatus.ACTIVE) {
                task.setTaskStatus(taskStatus);
                if (task.getTaskType() == TaskType.RECURRING) {
                    task.setScheduledStartTime(Helper.getNextCronDate(task.getSchedule()));
                    this.taskRepository.updateTaskStartTime(task.getTaskId(), task.getScheduledStartTime());
                }
                this.activeTaskRepository.addTaskToPriorityQueue(task);
                this.taskRepository.updateTaskStatus(taskId, taskStatus);
                return true;
            }
        } catch (Exception e) {
            logger.error("Error while updating data in repository", e);
        }
        return false;
    }


    public List<Task> getAllActiveTasks() {
        List<Task> taskList = new ArrayList<>();
        try {
            taskList =  this.taskRepository.getAllActiveTasks();
        } catch (Exception e) {
            logger.error("Error while fetching data from repository", e);
        }
        return taskList;
    }

    public Task getTask() {
        return this.activeTaskRepository.getTask();
    }

    public void removeTaskFromProcessingMap(Task task) {
        this.activeTaskRepository.removeTaskFromProcessingTaskMap(task);
    }

}
