package com.citi.reghub.rds.scheduler.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.citi.reghub.rds.scheduler.export.MongoExport;

public class EncryptCompressTasklet implements Tasklet {
	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptCompressTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		LOGGER.info("Step 4: Encrypt and Compress.");
		return RepeatStatus.FINISHED;
	}

}
