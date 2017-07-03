package com.citi.reghub.rds.scheduler.service;

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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.citi.reghub.rds.scheduler.RdsSchedulerConfiguration;

@Service
public class SchedulerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RdsSchedulerConfiguration.class);
	private int launchCount = 0;

	@Autowired
	private JobLauncher laucher;

	@Autowired
	private Job rdsBackupJob;
	
	@Autowired
	private InitializationService initializationService;

	@Scheduled(fixedRate = 10000)
	public void lauchJob() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		if (!initializationService.isValidated()) {
			LOGGER.info("launch job " + launchCount + ": not validated. waiting for initialization.");
			return;
		}

		++launchCount;

		LOGGER.info("launch job " + launchCount + " from scheduler .....");
		laucher.run(rdsBackupJob, new JobParametersBuilder().addDate("date", new Date()).toJobParameters());
		LOGGER.info("finished job " + launchCount + " in scheduler.\n");

	}
}
