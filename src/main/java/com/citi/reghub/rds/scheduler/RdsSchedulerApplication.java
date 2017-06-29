package com.citi.reghub.rds.scheduler;

import java.util.concurrent.ExecutionException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RdsSchedulerApplication {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		SpringApplication.run(RdsSchedulerApplication.class, args);
	}

	
}
