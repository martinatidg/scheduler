package com.citi.reghub.rds.scheduler.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.citi.reghub.rds.scheduler.export.ExportRequest;
import com.citi.reghub.rds.scheduler.util.ZooKeeper;

public class LockTasklet implements Tasklet {
	private static final Logger LOGGER = LoggerFactory.getLogger(LockTasklet.class);
	@Autowired
	ZooKeeper zooKeeper;

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		LOGGER.info("Step 2: Obtain Lock.");

		ExportRequest request = (ExportRequest) arg1.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("metadata");
		request.setFromTimeStamp(zooKeeper.getFromTimestamp());
		request.setToTimeStamp(zooKeeper.getToTimeStamp());
		
		LOGGER.info("request at step 2:\n" + request);
		LOGGER.info("ZooKeeper at step 2:\n" +zooKeeper);


		return RepeatStatus.FINISHED;
	}

}
