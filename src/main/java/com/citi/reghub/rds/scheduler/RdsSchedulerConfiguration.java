package com.citi.reghub.rds.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class RdsSchedulerConfiguration {
	@Value("${rds.scheduler.concurrencyLimit}")
	private int concurrencyLimit;

	@Bean
	public AsyncTaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("rds-");
		taskExecutor.setConcurrencyLimit(concurrencyLimit);

		return taskExecutor;
	}
}
