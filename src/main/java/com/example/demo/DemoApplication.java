package com.example.demo;

import com.example.demo.job.ThreadManager;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	private ThreadManager threadManager;
	@Autowired
	private TaskRepository taskRepository;

	@PostConstruct
	void startJob() throws InterruptedException {
		this.threadManager.startProcessing();
		this.taskRepository.truncateTaskTable();
		this.taskRepository.truncateTaskExecutionTable();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
