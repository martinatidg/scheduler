package com.citi.reghub.rds.scheduler;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class RdsSchedulerConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(RdsSchedulerConfiguration.class);
	private int launchCount = 0;
	@Autowired
	private JobLauncher laucher;

	@Autowired
	private Job rdsBackupJob;

	@Value("${rds.scheduler.concurrencyLimit}")
	private int concurrencyLimit;

	@Bean
	public AsyncTaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("rds-");
		taskExecutor.setConcurrencyLimit(concurrencyLimit);

		return taskExecutor;
	}

	@Scheduled(fixedRate = 60000)
	public void lauchJob() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		++launchCount;

		LOGGER.info("launch job " + launchCount + " from scheduler .....");
		laucher.run(rdsBackupJob, new JobParametersBuilder().addDate("date", new Date()).toJobParameters());
		LOGGER.info("finished job " + launchCount + " in scheduler.\n");

	}
}
