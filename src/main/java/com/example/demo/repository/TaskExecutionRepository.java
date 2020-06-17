package com.example.demo.repository;

import com.example.demo.constants.AppConstants;
import com.example.demo.model.TaskExecution;
import com.example.demo.sqlQueries.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class TaskExecutionRepository extends JdbcDaoSupport {
    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
    }

    public List<TaskExecution> getAllExecutions(Date startTime, Date endTime) {
        String sql = Queries.getAllExecutionsSql;
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql, new Object[]{ startTime, endTime });
        List<TaskExecution> taskExecutionList = new ArrayList<>();
        for(Map<String, Object> row:rows){
            TaskExecution taskExecution = new TaskExecution();
            taskExecution.setTaskId((String)row.get(AppConstants.TASK_ID));
            taskExecution.setStartTime((Date)row.get(AppConstants.START_TIME));
            taskExecution.setEndTime((Date)row.get(AppConstants.END_TIME));
            taskExecutionList.add(taskExecution);
        }
        return taskExecutionList;
    }

    public void addTaskExecution(TaskExecution taskExecution) {
        String sql = Queries.addExecutionSql ;
        getJdbcTemplate().update(sql, new Object[]{
                taskExecution.getTaskId(), taskExecution.getStartTime(), taskExecution.getEndTime()
        });
    }
}
