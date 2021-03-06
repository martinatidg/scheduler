package com.citi.reghub.rds.scheduler.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import com.citi.reghub.rds.scheduler.export.ExportRequest;
import com.citi.reghub.rds.scheduler.export.ExportResponse;
import com.citi.reghub.rds.scheduler.export.ExportService;

/**
 * @author Martin Tan
 *
 *         validate the output path and check if MongDB works properly
 */

@Service
public class InitializationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationService.class);
	static final String DEFAULT_OUTPUT_PATH = System.getProperty("user.home") + File.separator + "rds";

	@Autowired
	private MetadataService metadataService;
	@Autowired
	private ExportService exportService;

	@Value("${rds.scheduler.mongo.binaryPath}")
	private String binaryPath;
	@Value("${rds.scheduler.export.outputpath}")
	private String outputDir;
	@Value("${rds.scheduler.mongo.host}")
	private String host;
	@Value("${rds.scheduler.mongo.port}")
	private int port;

	// Validate the user provided output path. If it's not provided, the default
	// folder will be used.
	public void validateOutputPath() {
		Path outputPath;
		if (outputDir == null || outputDir.isEmpty()) {
			outputPath = Paths.get(DEFAULT_OUTPUT_PATH);
			System.setProperty("rds.scheduler.export.outputpath", DEFAULT_OUTPUT_PATH);
		} else {
			outputPath = Paths.get(outputDir);
		}

		if (!outputPath.toFile().exists()) {
			try {
				Files.createDirectory(outputPath);
			} catch (IOException e) {
				LOGGER.error("Output path '{}' cannot be created. ", outputPath.toString());
				throw new InitializationException(e);
			}
		}

		if (!Files.isReadable(outputPath) && !Files.isWritable(outputPath)) {
			throw new InitializationException("Output path is not accessible.");
		}
	}

	// Validate if the mongoDB is available and if 'mongoexport' works.
	// It will test against a list of databases and the associated collections.
	// If validation for any of them succeeds, then the initialization succeeds.
	// If validations for all fail, then the initialization fails.
	// All failed validations will be logged.
	public void validateMongoDBs() {
		InitializationException vex = new InitializationException("Initialization failed.");
		Map<String, List<String>> databases = metadataService.getDatabases();
		boolean validated = false;

		for (Map.Entry<String, List<String>> db : databases.entrySet()) {
			for (String collection : db.getValue()) {
				try {
					validateOneMongoDB(db.getKey(), collection);
					validated = true; // if no exception for any call of
										// validateOneMongoDB,then the
										// validation is passed.
				} catch (Exception e) {
					LOGGER.warn("Validation for Collection {} of database {} failed.", collection, db);
					vex.addSuppressed(e);
				}
			}
		}

		if (!validated) {
			throw vex; // the initialization failed.
		}
	}

	private void validateOneMongoDB(String db, String collection) {
		try {

			ExportRequest request = new ExportRequest();
			request.setRequestId("validate");
			request.setHostname(host);
			request.setPort(port);
			request.setDatabase(db);
			request.setCollection(collection);
			request.setLimit(1);

			// Create an old date so that 'mongoexport' will not miss any records.
			Calendar fromTimestamp = new GregorianCalendar(1980, 5, 19, 13, 0, 0);
			Calendar toTimestamp = new GregorianCalendar();
			request.setFromTimeStamp(fromTimestamp);
			request.setToTimeStamp(toTimestamp);

			ExportResponse response = exportService.submitRequest(request).get();

			boolean passed = response == null ? false : response.isSuccessful();
			if (!passed) {
				throw new InitializationException("ExportService failed.");
			}

			Path outPath = Paths.get(response.getExportPath()).getParent();
			FileSystemUtils.deleteRecursively(outPath.toFile());
		} catch (InterruptedException | ExecutionException e) {
			throw new InitializationException(e);
		}
	}
}
