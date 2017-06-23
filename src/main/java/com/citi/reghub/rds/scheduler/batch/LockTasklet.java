package com.citi.reghub.rds.scheduler.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class LockTasklet implements Tasklet {
	private static final Logger LOGGER = LoggerFactory.getLogger(LockTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		LOGGER.info("Step 2: Obtain Lock.");

		String value = (String) arg1.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("value");
		LOGGER.info("Value passed from step 1: " + value);

		return RepeatStatus.FINISHED;
	}

}
