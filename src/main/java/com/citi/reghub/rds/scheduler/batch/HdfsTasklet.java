package com.citi.reghub.rds.scheduler.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.citi.reghub.rds.scheduler.export.ExportResponse;

public class HdfsTasklet implements Tasklet {
	private static final Logger LOGGER = LoggerFactory.getLogger(HdfsTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		LOGGER.info("Step 5: Start HDFS tasklet.");

		ExportResponse response = (ExportResponse) arg1.getStepContext().getStepExecution().getJobExecution()
				.getExecutionContext().get("mongoRespone");

		LOGGER.trace("Step 5: Response: {}", response);

		LOGGER.info("Step 5: HDFS tasklet was finished.");

		return RepeatStatus.FINISHED;
	}
}
