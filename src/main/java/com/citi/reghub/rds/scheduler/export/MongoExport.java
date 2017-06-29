package com.citi.reghub.rds.scheduler.export;

import java.util.Collection;
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
		host, port, ssl, sslAllowInvalidCertificates, authenticationDatabase, authenticationMechanism, db, collection, query, jsonArray, out, username, password, limit;

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
//		String cmd = binaryPath + " " + getCommandLineKeys();
		String cmd = binaryPath + " " + getLocalCommandLineKeys(false);

		RuntimeProcess process = new RuntimeProcess(cmd);
		RuntimeProcessResult result = process.execute();

		return buildResponse(result, false);
	}

	public ExportResponse validateMongoDB() throws Exception {
//		String cmd = binaryPath + " " + getCommandLineKeys();
		String cmd = binaryPath + " " + getLocalCommandLineKeys(true);

		RuntimeProcess process = new RuntimeProcess(cmd);
		RuntimeProcessResult result = process.execute();

		return buildResponse(result, true);
	}

	private ExportResponse buildResponse(RuntimeProcessResult result, boolean validate) {
		ExportResponse response = new ExportResponse();
		Collection<String> errors = result.getError();

		response.setExportPath(getOutputPath(validate));
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

	private String getCommandLineKeys(boolean validate) {
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
		if (validate) {
			sb.append(keys.limit + "1");
		}
		else {
			sb.append(keys.query + createQueryBetween(exportRequest));
//			sb.append(keys.query + createQuery(exportRequest));
		}
		sb.append(keys.jsonArray);
		sb.append(keys.out + getOutputPath(validate));

		LOGGER.info("MongoExport command line: {}", sb.toString());

		sb.append(keys.username + username);
		sb.append(keys.password + password);

		return sb.toString();
	}

	private String getLocalCommandLineKeys(boolean validate) {
		StringBuilder sb = new StringBuilder();

		sb.append(keys.db + exportRequest.getDatabase());
		sb.append(keys.collection + exportRequest.getCollection());
		if (validate) {
			sb.append(keys.limit + "1");
		}
		else {
			sb.append(keys.query + createQueryBetween(exportRequest));
//			sb.append(keys.query + createQuery(exportRequest));
		}
		sb.append(keys.out + getOutputPath(validate));

		LOGGER.info("MongoExport command line: {}", sb.toString());

		return sb.toString();
	}

	private String createQuery(ExportRequest er) {
		StringBuilder sb = new StringBuilder();

		sb.append("\"{lastUpdatedTs : {$lte : new Date(");
		sb.append(er.getLastTimeStamp().getTimeInMillis());
		sb.append(")},");
		sb.append(" isRDSEligible : true} \" ");

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

	private String getOutputPath(boolean validate) {
		String outpath = this.outputPath;
		if (validate) {
			outpath += "validation";
		}
		else {
			outpath += exportRequest.getRequestId() + "." + exportRequest.getDatabase() + "." + exportRequest.getCollection();
		}

		return outpath;
	}

	private static boolean isLinux() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0 ? false : true;
	}

}
