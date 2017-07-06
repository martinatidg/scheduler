package com.citi.reghub.rds.scheduler.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.citi.reghub.rds.scheduler.export.ExportRequest;
import com.citi.reghub.rds.scheduler.service.MetadataService;

public class MetadataTasklet implements Tasklet {
	private static final Logger LOGGER = LoggerFactory.getLogger(MetadataTasklet.class);

	@Autowired
	MetadataService meta;

	@Value("${rds.scheduler.mongo.host}")
	private String host;
	@Value("${rds.scheduler.mongo.port}")
	private int port;

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		LOGGER.info("Step 1: Start Metadata tasklet.");

		JobExecution job = arg1.getStepContext().getStepExecution().getJobExecution();
		LOGGER.trace("Step 1: id: {}, jobId: {}.", job.getId(), job.getJobId());

		ExportRequest request = new ExportRequest();
		request.setRequestId("" + job.getJobId());
		request.setHostname(host);
		request.setPort(port);

		request.setDatabase(meta.getDatabase());
		request.setCollection(meta.getCollection());

		LOGGER.trace("Step 1: Export request: {}.", request);
		
		job.getExecutionContext().put("metadata", request);

		LOGGER.info("Step 1: Metadata tasklet was finished.");

		return RepeatStatus.FINISHED;
	}

}
