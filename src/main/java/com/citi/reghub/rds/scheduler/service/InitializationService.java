package com.citi.reghub.rds.scheduler.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

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
	// default output name, used when the user set output path is empty.
    private static final String DEFAULT_OUTPUT = System.getProperty("java.io.tmpdir") + File.separator + "rds";

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

	private String error;
	
	private boolean validated = false;

	// validate the user provided output path. If it's not provided, the default folder will be used.
	public boolean validateOutputPath() {
		if (outputDir == null || outputDir.isEmpty()) {
			outputPath = Paths.get(DEFAULT_OUTPUT);
			System.setProperty("rds.scheduler.export.outputpath", DEFAULT_OUTPUT);
		}
		else {
			outputPath = Paths.get(outputDir);
		}

		if (!Files.isReadable(outputPath) && !Files.isWritable(outputPath)) {
			error = "Output path is not accessible.";
			return false;
		}

		if (!exists(outputPath)) {
			try {
				createDirectory(outputPath);
			} catch (IOException e) {
				error = outputPath.toString() + " cannot be created: " + e.getMessage();
				validated = false;
				return false;
			}
		}

		return true;
	}

	// validate if the mongoDB is available and the mongoexport works.
	// It will test against a list of databases and the associated collections.
	// If any one of them validated, then the validation will pass. 
	// For those validation failed, it will be logged.
	public boolean validateMongoDBs() {
		Map<String, List<String>> databases = metadataService.getDatabases();
		System.out.println("InitializationService.validateMongoDB(): databases" + databases);
		for (String db : databases.keySet()) {
			for (String collection : databases.get(db)) {
				if (validateOneMongoDB(db, collection)) {
					validated = true;	// if any passed, then the validation succeeds.
				}
			}
		}

		return validated;
	}

	// validate if the mongoDB is available and the mongoexport works
	public boolean validateMongoDB() {
		return validateOneMongoDB(metadataService.getDatabase(), metadataService.getCollection());
	}

	private boolean validateOneMongoDB(String db, String collection) {
		boolean passed = false;
		ExportResponse response = null;

		try {
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
		} catch (Exception e) {
			error = "Collection " + collection + " of database " + db + " is not available or the database is not working. Error message: " 
						+ (response == null? "" : response.getLastMessage() + ", ") + e.getMessage();
			return false;
		}

		if (response != null) {
			passed = response.isSuccessful();
		}
		return passed;
	}

	public boolean isValidated() {
		return validated;
	}

	public String getOutputPath() {
		return outputPath.toString();
	}

	public String getError() {
		return error;
	}

    private boolean exists(Path path) {
    	return Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    }
    
    private void createDirectory(Path path) throws IOException {
    	Files.createDirectory(path);
    }

}
