package com.citi.reghub.rds.scheduler.batch;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import com.citi.reghub.rds.scheduler.compression.Compressor;
import com.citi.reghub.rds.scheduler.compression.Compressors;
import com.citi.reghub.rds.scheduler.export.ExportResponse;
import com.citi.reghub.rds.scheduler.util.Util;

public class EncryptCompressTasklet implements Tasklet {
	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptCompressTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		LOGGER.info("Step 4: Start Encryption and Compression tasklet.");

		ExecutionContext context = arg1.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		ExportResponse response = (ExportResponse) context.get("response");
		LOGGER.trace("Step 4: Export response: {}", response);
		
		String outputFile = response.getExportPath();
		Path outputPath = Paths.get(outputFile).getParent();

		Compressor compressor = Compressors.zipCompressor();
		Path zippath = compressor.compress(outputPath.toString());
		Util.deleteDirectory(Paths.get(outputFile).getParent().toString());

		context.put("zippedfile", zippath);

		LOGGER.info("Step 4: Encryption and Compression tasklet was finished.");

		return RepeatStatus.FINISHED;
	}
}
