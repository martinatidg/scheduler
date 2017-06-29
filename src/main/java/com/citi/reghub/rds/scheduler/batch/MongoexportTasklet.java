package com.citi.reghub.rds.scheduler.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.citi.reghub.rds.scheduler.export.ExportRequest;
import com.citi.reghub.rds.scheduler.export.ExportResponse;
import com.citi.reghub.rds.scheduler.export.ExportService;

public class MongoexportTasklet implements Tasklet {
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoexportTasklet.class);
	@Autowired
	private ExportService exportService;

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		LOGGER.info("Step 3: Invoke mongoexport.");
		ExportRequest request = (ExportRequest) arg1.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("metadata");
		
		LOGGER.info("request at step 2: " + request);

		ExportResponse exportResponse = exportService.submitRequest(request).get();
		
		if (!exportResponse.isSuccessful()) {
			LOGGER.error(exportResponse.getLastMessage());
			System.exit(-1);	// the job restart may be handled here
			//return null;
		}

		LOGGER.info("mongoexport result:\n" + exportResponse);

		arg1.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("mongoRespone", exportResponse);

		return RepeatStatus.FINISHED;

	}


}
