package com.example.demo.controller;


import com.example.demo.model.Task;
import com.example.demo.model.TaskExecution;
import com.example.demo.model.TaskStatus;
import com.example.demo.service.TaskExecutionService;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@Controller
public class RequestController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskExecutionService taskExecutionService;

    @RequestMapping(method = RequestMethod.GET, value = "/tasks")
    @ResponseBody
    public List<Task> getTasks(HttpServletRequest request, HttpServletResponse response) {
        return this.taskService.getAllActiveTasks();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tasks")
    @ResponseBody
    public void addTask(@RequestBody HashMap<String, Object> body, HttpServletRequest request, HttpServletResponse response) {
        boolean status = this.taskService.addTask(body);
        if (status) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/tasks")
    @ResponseBody
    public void updateTaskStatus(@RequestParam String taskId, @RequestParam String taskStatus, HttpServletRequest request, HttpServletResponse response) {
        if (TaskStatus.valueOf(taskStatus) == TaskStatus.COMPLETED) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        boolean status = this.taskService.changeTaskStatus(taskId, TaskStatus.valueOf(taskStatus));
        if (status) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/taskExecutions")
    @ResponseBody
    public List<TaskExecution> getTaskExecutions(@RequestParam Long fromTime, @RequestParam Long toTime, HttpServletRequest request, HttpServletResponse response) {
        return this.taskExecutionService.getAllTaskExecutions(fromTime, toTime);
    }


}
