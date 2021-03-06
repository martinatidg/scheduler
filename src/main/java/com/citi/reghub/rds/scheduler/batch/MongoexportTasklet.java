package com.citi.reghub.rds.scheduler.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.citi.reghub.rds.scheduler.export.ExportRequest;
import com.citi.reghub.rds.scheduler.export.ExportResponse;
import com.citi.reghub.rds.scheduler.export.ExportService;
import com.citi.reghub.rds.scheduler.service.SchedulerService;

public class MongoexportTasklet implements Tasklet {
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoexportTasklet.class);
	@Autowired
	private ExportService exportService;
	@Autowired
	private SchedulerService schedulerService;

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		LOGGER.info("Step 3: Start Mongoexport tasklet.");
		ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		ExportRequest request = (ExportRequest) executionContext.get("request");
		
		LOGGER.trace("Step 3: Export request: {}: ", request);

		ExportResponse exportResponse = exportService.submitRequest(request).get();
		
		if (!exportResponse.isSuccessful()) {
			LOGGER.error(exportResponse.getLastMessage());
			schedulerService.cancelScheduler();
			throw new JobExecutionException("Step3: Mongoexport tasklet failed.");
		}

		LOGGER.trace("Step 3: Mongoexport response: {} ", exportResponse);

		executionContext.put("response", exportResponse);

		LOGGER.info("Step 3: Mongoexport tasklet was finished.");

		return RepeatStatus.FINISHED;
	}
}
