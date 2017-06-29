package com.citi.reghub.rds.scheduler.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.citi.reghub.rds.scheduler.export.ExportRequest;
import com.citi.reghub.rds.scheduler.export.ExportResponse;
import com.citi.reghub.rds.scheduler.export.ExportService;
import com.citi.reghub.rds.scheduler.export.MongoExport;
/**
 * @author Martin Tan
 *
 * validate the output path and check if MongDB works properly
 */

@Service
public class InitializationService {
	// default output name, used when the user set output path is empty.
    private static final String DEFAULT_OUTPUT = System.getProperty("user.home") + File.separator + "rds";

    @Autowired
	private MetadataService metadata;
	@Autowired
	private ExportService exportService;
//	private MongoExport mongoExport;
	
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
		}
		else {
			outputPath = Paths.get(outputDir);
		}

		if (!exists(outputPath)) {
			try {
				createDirectory(outputPath);
			} catch (IOException e) {
				error = "Output path is not accessible. error: " + e.getMessage();
				validated = false;
				return false;
			}
		}

		return true;
	}

	// validate if the mongoDB is available and the mongoexport works
	public boolean validateMongoDB() {
		ExportResponse response = null;
		try {
			ExportRequest request = new ExportRequest();
			request.setRequestId("validate");
			request.setHostname(host);
			request.setPort(port);

			request.setDatabase(metadata.getDatabase());
			request.setCollection(metadata.getCollection());
			request.setValidation(true);

//			mongoExport.setRequest(request);
			response = exportService.submitRequest(request).get();
		} catch (Exception e) {
			error = "MongoDB DB is not available or not working. error: " + (response == null? "" : response.getLastMessage());
			validated = false;
			return false;
		}

		if (response != null) {
			validated = response.isSuccessful();
		}
		return validated;
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
