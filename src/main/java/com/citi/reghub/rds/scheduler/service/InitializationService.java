package com.citi.reghub.rds.scheduler.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.citi.reghub.rds.scheduler.export.ExportRequest;
import com.citi.reghub.rds.scheduler.export.ExportResponse;
import com.citi.reghub.rds.scheduler.export.ExportService;
/**
 * @author Martin Tan
 *
 * validate the output path and check if MongDB works properly
 */

@Service
public class InitializationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationService.class);
	// default output name, used when the user set output path is empty.
    //private static final String DEFAULT_OUTPUT = System.getProperty("java.io.tmpdir") + File.separator + "rds";
    private static final String DEFAULT_OUTPUT = System.getProperty("user.home") + File.separator + "rds";

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
	

	private Path outputPath;

	private List<String> errors = new ArrayList<>();
	
	private boolean validated = false;

	// validate the user provided output path. If it's not provided, the default folder will be used.
	public void validateOutputPath() throws ValidationException {
		if (outputDir == null || outputDir.isEmpty()) {
			outputPath = Paths.get(DEFAULT_OUTPUT);
			System.setProperty("rds.scheduler.export.outputpath", DEFAULT_OUTPUT);
		}
		else {
			outputPath = Paths.get(outputDir);
		}

		if (!exists(outputPath)) {
			try {
				createDirectory(outputPath);
			} catch (IOException e) {
				throw new ValidationException(outputPath.toString() + " cannot be created: " + e.getMessage());
			}
		}

		if (!Files.isReadable(outputPath) && !Files.isWritable(outputPath)) {
			throw new ValidationException("Output path is not accessible.");
		}
	}

	// validate if the mongoDB is available and the mongoexport works.
	// It will test against a list of databases and the associated collections.
	// If any one of them validated, then the validation will pass. 
	// For those validation failed, it will be logged.
	public void validateMongoDBs() throws ValidationException {
		ValidationException vex = new ValidationException();

		validated = false;
		Map<String, List<String>> databases = metadataService.getDatabases();

		for (Map.Entry<String, List<String>> db : databases.entrySet()) {
			for (String collection : db.getValue()) {
				try {
					validateOneMongoDB(db.getKey(), collection);
					validated = true;	// if no exception for any call of validateOneMongoDB,then the validation is passed.
				} catch (Exception e) {
					Exception ex = new Exception("Collection " + collection + " of database " + db + " is failed. Error message: " + e.getMessage());
					vex.addSuppressed(ex);
					LOGGER.info(ex.getMessage());
				}
			}
		}

		if (!validated) {
			throw vex;
		}
	}

	// validate if the mongoDB is available and the mongoexport works
	public void validateMongoDB() throws ValidationException {
		try {
			validateOneMongoDB(metadataService.getDatabase(), metadataService.getCollection());
			validated = true;	// if no exception, then validation passed.
		} catch (Exception e) {
			throw new ValidationException("Output path is not accessible.");
		}
	}

	private void validateOneMongoDB(String db, String collection) throws Exception {
		boolean passed = false;
		ExportResponse response = null;

		ExportRequest request = new ExportRequest();
		request.setRequestId("validate");
		request.setHostname(host);
		request.setPort(port);

		request.setDatabase(db);
		request.setCollection(collection);
		request.setLimit(1);

		Calendar fromTimestamp = new GregorianCalendar(1980, 5, 19, 13, 0, 0);	// month start from 0. So 5 is June.
		Calendar toTimestamp = new GregorianCalendar();

		request.setFromTimeStamp(fromTimestamp);
		request.setToTimeStamp(toTimestamp);

		response = exportService.submitRequest(request).get();

		if (response != null) {
			passed = response.isSuccessful();
		}
		else {
			passed = false;
		}

		if (!passed) {
			throw new Exception("ExportService failed.");
		}
	}

	public boolean isValidated() {
		return validated;
	}

	public String getOutputPath() {
		return outputPath.toString();
	}

    private boolean exists(Path path) {
    	return Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    }
    
    private void createDirectory(Path path) throws IOException {
    	Files.createDirectory(path);
    }

}
