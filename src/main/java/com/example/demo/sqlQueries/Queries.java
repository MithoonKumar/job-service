package com.example.demo.sqlQueries;

public class Queries {
    public static String getAllActiveTasksSql = "SELECT * FROM task WHERE taskStatus = ?";
    public static String addTaskSql = "INSERT INTO task (taskId, scheduledStartTime, taskType, taskStatus, taskPriority, schedule, taskDuration) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static String updateTaskSql = "UPDATE task set taskStatus = ? where taskId = ? " ;
    public static String updateTaskStartTimeSql = "UPDATE task set scheduledStartTime = ? where taskId = ? ";
    public static String getAllExecutionsSql = "SELECT * FROM taskExecution WHERE startTime > ? and endTime < ?";
    public static String addExecutionSql = "INSERT INTO taskExecution (taskId, startTime, endTime) VALUES (?, ?, ?)";
    public static String truncateTaskTable = "truncate task";
    public static String truncateTaskExecutionTable = "truncate taskExecution";
    public static String getTaskSql = "select * from task where taskId = ?";
}
