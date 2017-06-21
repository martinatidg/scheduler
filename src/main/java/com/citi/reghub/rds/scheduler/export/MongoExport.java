package com.citi.reghub.rds.scheduler.export;

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

	private static String osKeyPrefix = isLinux() ? " --" : " /";
	@Value("${rds.scheduler.mongo.binaryPath}")
	private String binaryPath;
	@Value("${rds.scheduler.export.outputpath}")
	private String outputPath;
	@Value("${rds.scheduler.mongo.username}")
	private String username;
	@Value("${rds.scheduler.mongo.password}")
	private String password;
	private ExportRequest exportRequest;

	enum keys {
		host, port, ssl, sslAllowInvalidCertificates, authenticationDatabase, authenticationMechanism, db, collection, query, jsonArray, out, username, password;

		@Override
		public String toString() {
			return osKeyPrefix + super.toString() + " ";
		}
	}

	public void setRequest(ExportRequest er) {
		validateExportRequest(er);
		this.exportRequest = er;
	}

	@Override
	public ExportResponse call() throws Exception {
		String cmd = binaryPath + " " + getCommandLineKeys();

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
		if (er == null || er.getHostname() == null || er.getCollection() == null || er.getDatabase() == null
				|| er.getRequestId() == null) {
			throw new IllegalArgumentException(
					"ExportResult cannot be null or have any of it's variables set to null.");
		}
	}

	private String getCommandLineKeys() {
		StringBuilder sb = new StringBuilder();

		sb.append(keys.host + exportRequest.getHostname());
		sb.append(keys.port + "" + exportRequest.getPort());
		//
		sb.append(keys.ssl);
		sb.append(keys.sslAllowInvalidCertificates);
		sb.append(keys.authenticationDatabase + "admin");
		sb.append(keys.authenticationMechanism + "SCRAM-SHA-1");
		//
		sb.append(keys.db + exportRequest.getDatabase());
		sb.append(keys.collection + exportRequest.getCollection());
		// sb.append(keys.query + createQuery(exportRequest));
		sb.append(keys.jsonArray);
		sb.append(keys.out + getOutputPath());

		LOGGER.info("MongoExport command line: {}", sb.toString());

		sb.append(keys.username + username);
		sb.append(keys.password + password);

		return sb.toString();
	}

	private String createQuery(ExportRequest er) {
		StringBuilder sb = new StringBuilder();

		sb.append("\"{lastUpdatedTs : {$gte : ISODate('");
		sb.append(er.getLastTimeStamp());
		sb.append("')},");
		sb.append(" isRDSEligible : $eq : true} \" ");

		return sb.toString();
	}

	private String getOutputPath() {
		return this.outputPath + exportRequest.getRequestId() + "." + exportRequest.getDatabase() + "."
				+ exportRequest.getCollection();
	}

	private static boolean isLinux() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0 ? false : true;
	}

}
