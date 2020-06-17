create table task (
    taskId varchar(20) not null primary key,
    scheduledStartTime DATETIME,
    taskType varchar(20) not null,
    taskStatus varchar(20) not null,
    taskPriority varchar(20) not null,
    schedule varchar(20),
    taskDuration int not null
);

create table taskExecution (
    executionId int not null auto_increment primary key,
    taskId varchar(20) not null,
    startTime DATETIME not null,
    endTime DATETIME not null
);