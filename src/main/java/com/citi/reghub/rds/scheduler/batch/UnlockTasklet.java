package com.citi.reghub.rds.scheduler.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.citi.reghub.rds.scheduler.export.ExportRequest;
import com.citi.reghub.rds.scheduler.service.ZooKeeper;

public class UnlockTasklet implements Tasklet {
	private static final Logger LOGGER = LoggerFactory.getLogger(UnlockTasklet.class);

	@Autowired
	private ZooKeeper keeper;

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		LOGGER.info("Step 6: release lock.");

		ExportRequest request = (ExportRequest) arg1.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("metadata");
		keeper.setFromTimestamp(request.getToTimeStamp());

		LOGGER.info("request at step 6:\n" + request);
		LOGGER.info("ZooKeeper at step 6:\n" + keeper);

		return RepeatStatus.FINISHED;
	}

}
