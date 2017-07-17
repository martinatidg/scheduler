package com.citi.reghub.rds.scheduler;

import java.util.concurrent.ExecutionException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RdsSchedulerApplication {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		SpringApplication.run(RdsSchedulerApplication.class, args);
	}

	// Sonar requires to provide a private constructor for classes with only static methods.
	// However if a private constructor is provided, the Spring Boot default test will fail.
	// This method is a workaround to avoid the sonar complaint.
	public void doNothing() {
		throw new UnsupportedOperationException("Workaround to avoid Sonar complains for no private constructor.");
	}
}
