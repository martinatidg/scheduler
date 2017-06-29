package com.citi.reghub.rds.scheduler.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.citi.reghub.rds.scheduler.export.ExportRequest;
import com.citi.reghub.rds.scheduler.export.ExportResponse;
import com.citi.reghub.rds.scheduler.export.MongoExport;

@Service
public class ValidateService {
	@Autowired
	private MetadataService metadata;
	@Autowired
	private MongoExport mongoExport;
	
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

	public boolean validate() {
		if (outputDir.isEmpty()) {
			outputPath = Dir.DEFAULT_OUTPUT.path();
		}
		else {
			outputPath = Paths.get(outputDir);
		}

		if (!Dir.exists(outputPath)) {
			try {
				Dir.createDirectory(outputPath);
			} catch (IOException e) {
				error = e.getMessage();
				validated = false;
				return false;
			}
		}

		ExportResponse response;
		try {
			ExportRequest request = new ExportRequest();
			request.setRequestId("validate");
			request.setHostname(host);
			request.setPort(port);

			request.setDatabase(metadata.getDatabase());
			request.setCollection(metadata.getCollection());

			mongoExport.setRequest(request);
			response = mongoExport.validateMongoDB();
		} catch (Exception e) {
			error = e.getMessage();
			validated = false;
			return false;
		}

		if (response != null) {
			validated = response.isSuccessful();
		}
		return validated;
	}

//	public MetadataService getMetadata() {
//		return metadata;
//	}
//
//	public void setMetadata(MetadataService metadata) {
//		this.metadata = metadata;
//	}

	public boolean isValidated() {
		return validated;
	}

	public String getOutputPath() {
		System.out.println("ValidateService:getOutputPath() = " + outputPath.toString());
		return outputPath.toString();
	}

//	public void setOutputPath(String outputPath) {
//		this.outputPath = Paths.get(outputPath);
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}

//	public void setError(String error) {
//		this.error = error;
//	}

//	public String getMessage() {
//
//		return message;
//	}

	public String getError() {
		return error;
	}
}
