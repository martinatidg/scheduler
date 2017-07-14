package com.citi.reghub.rds.scheduler.export;

import java.nio.file.Paths;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.citi.reghub.rds.scheduler.process.RuntimeProcess;
import com.citi.reghub.rds.scheduler.process.RuntimeProcessResult;

/**
 * 
 * @see https://docs.mongodb.com/manual/reference/program/mongoexport/
 * 
 * @author Michael Rootman
 *
 */
@Component
@Scope("prototype")
public class MongoExport implements Callable<ExportResponse> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoExport.class);

	@Value("${rds.scheduler.mongo.binaryPath}")
	private String binaryPath;
	@Value("${rds.scheduler.export.outputpath}")
	private String outputPath;
	@Value("${rds.scheduler.mongo.username}")
	private String username;
	@Value("${rds.scheduler.mongo.password}")
	private String password;
	private ExportRequest exportRequest;

	enum Keys {
		HOST("host"), PORT("port"), SSL("ssl"), SAIC("sslAllowInvalidCertificates"),
		AUTH_DB("authenticationDatabase"), AUTH_MSM("authenticationMechanism"), 
		DB("db"), COLLECTION("collection"), QUERY("query"), JSON_ARRAY("jsonArray"),
		OUT("out"), USERNAME("username"), PASSWORD("password"), LIMIT("limit");

		private static final String OS_KEY_PREFIX;
		private String key;

		static {
			boolean isLinux = !System.getProperty("os.name").toLowerCase().contains("win");
			OS_KEY_PREFIX = isLinux ? " --" : " /";
		}

		private Keys(String key) {
			this.key = key;
		}

		@Override
		public String toString() {
			return OS_KEY_PREFIX + key + " ";
		}
	}

	public void setRequest(ExportRequest er) {
		validateExportRequest(er);
		this.exportRequest = er;
	}

	@Override
	public ExportResponse call() throws Exception {
		String cmd = binaryPath;
		if (exportRequest.getHostname().contains("localhost")) {
			cmd += " " + getLocalCommandLineKeys();
		}
		else {
			cmd += " " + getCommandLineKeys();
		}

		RuntimeProcess process = new RuntimeProcess(cmd);
		RuntimeProcessResult result = process.execute();

		return buildResponse(result);
	}

	private ExportResponse buildResponse(RuntimeProcessResult result) {
		ExportResponse response = new ExportResponse();

		response.setExportPath(getOutputPath());
		response.setSuccessful(result.isCompleteSuccessfully());
		response.setLastMessage(result.getError().stream().skip(result.getError().size() - 1).findFirst().orElse("--"));

		if (response.isSuccessful()) {
			response.setRecords(response.getLastMessage().split(" ")[1]);
		}

		return response;
	}

	private void validateExportRequest(ExportRequest er) {
		if (er == null || er.getHostname() == null || er.getCollection() == null || er.getDatabase() == null || er.getRequestId() == null) {
			throw new IllegalArgumentException("ExportResult cannot be null or have any of it's variables set to null.");
		}
	}

	private String getCommandLineKeys() {
		StringBuilder sb = new StringBuilder();

		sb.append(Keys.HOST + exportRequest.getHostname());
		sb.append(Keys.PORT + "" + exportRequest.getPort());
		sb.append(Keys.SSL);
		sb.append(Keys.SAIC);
		sb.append(Keys.AUTH_DB + "admin");
		sb.append(Keys.AUTH_MSM + "SCRAM-SHA-1");
		sb.append(Keys.DB + exportRequest.getDatabase());
		sb.append(Keys.COLLECTION + exportRequest.getCollection());
		if (exportRequest.getLimit() > 0) {
			sb.append(Keys.LIMIT + "" + exportRequest.getLimit());
		}
		sb.append(Keys.QUERY + createQueryBetween(exportRequest));
		sb.append(Keys.JSON_ARRAY);
		sb.append(Keys.OUT + getOutputPath());

		LOGGER.info("MongoExport command line: {}", sb);

		sb.append(Keys.USERNAME + username);
		sb.append(Keys.PASSWORD + password);


		return sb.toString();
	}

	private String getLocalCommandLineKeys() {
		StringBuilder sb = new StringBuilder();

		sb.append(Keys.DB + exportRequest.getDatabase());
		sb.append(Keys.COLLECTION + exportRequest.getCollection());
		if (exportRequest.getLimit() > 0) {
			sb.append(Keys.LIMIT + " " + exportRequest.getLimit());
		}
		sb.append(Keys.QUERY + createQueryBetween(exportRequest));
		sb.append(Keys.OUT + getOutputPath());

		LOGGER.info("MongoExport command line: {}", sb);

		return sb.toString();
	}

	private String createQueryBetween(ExportRequest er) {
		StringBuilder sb = new StringBuilder();

		sb.append("\"{lastUpdatedTs : {");
		sb.append("$gte : new Date(");
		sb.append(er.getFromTimeStamp().getTimeInMillis());
		sb.append("), ");
		sb.append("$lte : new Date(");
		sb.append(er.getToTimeStamp().getTimeInMillis());
		sb.append(")},");
		sb.append(" isRDSEligible : true}\" ");

		return sb.toString();
	}

	private String getOutputPath() {
		String exportFileName = exportRequest.getRequestId() + "." + exportRequest.getDatabase() + "." + exportRequest.getCollection();
		return Paths.get(this.outputPath, exportRequest.getRequestId(), exportFileName).toString();
	}
}
