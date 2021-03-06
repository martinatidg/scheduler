package com.citi.reghub.rds.scheduler.service;

import java.util.Date;
import java.util.concurrent.Future;

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
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.citi.reghub.rds.scheduler.RdsSchedulerConfiguration;

@Service
public class SchedulerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RdsSchedulerConfiguration.class);
	private int launchCount = 1;
	private Future<?> future;

	@Autowired
	private JobLauncher laucher;

	@Autowired
	private Job rdsBackupJob;

	@Autowired
	private TaskScheduler taskScheduler;

	public void lauchJob() {
		LOGGER.info("Start scheduler");
		future = taskScheduler.scheduleAtFixedRate(() -> {
			LOGGER.info("Launch job {} ......", launchCount);

			try {
				laucher.run(rdsBackupJob, new JobParametersBuilder().addDate("date", new Date()).toJobParameters());
			} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
					| JobParametersInvalidException e) {
				LOGGER.error("Job {} failed:\n{}", launchCount, e);
			}

			LOGGER.info("Finished job {}.\n", launchCount);
			++launchCount;
		}, 10000);
	}

	public void cancelScheduler() {
		future.cancel(true);
	}
}
