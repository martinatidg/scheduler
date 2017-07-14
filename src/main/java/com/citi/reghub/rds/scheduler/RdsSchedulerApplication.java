package com.citi.reghub.rds.scheduler;

import java.util.concurrent.ExecutionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RdsSchedulerApplication {
    private RdsSchedulerApplication() {
        throw new UnsupportedOperationException("This class contains static methods only and cannot be instantiated.");
    }

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		SpringApplication.run(RdsSchedulerApplication.class, args);
	}
}
