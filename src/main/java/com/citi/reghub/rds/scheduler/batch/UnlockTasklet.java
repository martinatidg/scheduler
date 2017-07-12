package com.citi.reghub.rds.scheduler.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.citi.reghub.rds.scheduler.export.ExportRequest;
import com.citi.reghub.rds.scheduler.service.ZooKeeperService;

public class UnlockTasklet implements Tasklet {
	private static final Logger LOGGER = LoggerFactory.getLogger(UnlockTasklet.class);

	@Autowired
	private ZooKeeperService zooKeeperService;

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		LOGGER.info("Step 6: Start Unlock tasklet.");

		ExportRequest request = (ExportRequest) arg1.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("request");
		zooKeeperService.setFromTimestamp(request.getToTimeStamp());

		LOGGER.trace("Step 6: Export request: {}.", request);
		LOGGER.trace("Step 6: zooKeeperService: {}.", zooKeeperService);

		LOGGER.info("Step 6: Unlock tasklet was finished.");

		return RepeatStatus.FINISHED;
	}
}
