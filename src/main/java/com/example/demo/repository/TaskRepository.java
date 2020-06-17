package com.example.demo.repository;

import com.example.demo.constants.AppConstants;
import com.example.demo.model.Task;
import com.example.demo.model.TaskPriority;
import com.example.demo.model.TaskStatus;
import com.example.demo.model.TaskType;
import com.example.demo.sqlQueries.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class TaskRepository extends JdbcDaoSupport {
    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
    }

    public List<Task> getAllActiveTasks() {
        String sql = Queries.getAllActiveTasksSql;
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql, new Object[]{TaskStatus.ACTIVE.toString()});
        List<Task> taskList = new ArrayList<>();
        for(Map<String, Object> row:rows){
            Task task = new Task();
            task.setTaskId((String)row.get(AppConstants.TASK_ID));
            task.setScheduledStartTime((Date)row.get(AppConstants.SCHEDULED_START_TIME));
            task.setSchedule((String)row.get(AppConstants.SCHEDULE));
            task.setDuration((Integer)row.get(AppConstants.TASK_DURATION));
            task.setTaskPriority(TaskPriority.valueOf((String)row.get(AppConstants.TASK_PRIORITY)));
            task.setTaskType(TaskType.valueOf((String)row.get(AppConstants.TASK_TYPE)));
            taskList.add(task);
        }
        return taskList;
    }

    public void addTask(Task task) {
        String sql = Queries.addTaskSql ;
        getJdbcTemplate().update(sql, new Object[]{
                task.getTaskId(), task.getScheduledStartTime(), task.getTaskType().toString(), task.getTaskStatus().toString(),
                task.getTaskPriority().toString(), task.getSchedule(), task.getDuration()
        });
    }

    public Task getTask(String taskId) {
        String sql = Queries.getTaskSql ;
        return (Task)getJdbcTemplate().queryForObject(sql, new Object[]{taskId}, new RowMapper<Task>(){
            @Override
            public Task mapRow(ResultSet rs, int rwNumber) throws SQLException {
                Task task = new Task();
                task.setTaskId(rs.getString(AppConstants.TASK_ID));
                task.setScheduledStartTime(rs.getTimestamp(AppConstants.SCHEDULED_START_TIME));
                task.setSchedule((rs.getString(AppConstants.SCHEDULE)));
                task.setDuration(rs.getInt(AppConstants.TASK_DURATION));
                task.setTaskPriority(TaskPriority.valueOf(rs.getString(AppConstants.TASK_PRIORITY)));
                task.setTaskType(TaskType.valueOf(rs.getString(AppConstants.TASK_TYPE)));
                task.setTaskStatus(TaskStatus.valueOf(rs.getString(AppConstants.TASK_STATUS)));
                return task;
            }
        });
    }


    public void updateTaskStatus(String taskId, TaskStatus taskStatus) {
        String sql = Queries.updateTaskSql;
        getJdbcTemplate().update(sql, new Object[]{
                taskStatus.toString(), taskId
        });
    }

    public void updateTaskStartTime(String taskId, Date date) {
        String sql = Queries.updateTaskStartTimeSql;
        getJdbcTemplate().update(sql, new Object[]{
                date, taskId
        });
    }

    public void truncateTaskTable() {
        String sql = Queries.truncateTaskTable;
        getJdbcTemplate().update(sql);
    }

    public void truncateTaskExecutionTable() {
        String sql = Queries.truncateTaskExecutionTable;
        getJdbcTemplate().update(sql);
    }

}
