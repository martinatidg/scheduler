package com.citi.reghub.rds.scheduler.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class MongoExport {
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoExport.class);

	enum keys {
		host, port, ssl, sslAllowInvalidCertificates, authenticationDatabase, authenticationMechanism, db, collection, query, jsonArray, out, username, password;

		@Override
		public String toString() {
			return osKeyPrefix + super.toString() + " ";
		}
	}

	private static String osKeyPrefix = isLinux() ? " --" : " /";

	//
	@Value("#rds.scheduler.mongoBinaryPath ?: $mongoexport")
	private String binaryPath;
	private ExportRequest exportRequest;

	/**
	 * Invokes mongoExport tool
	 * 
	 * @see https://docs.mongodb.com/manual/reference/program/mongoexport/
	 * 
	 */

	public MongoExport(ExportRequest er) {
		validateExportRequest(er);
		this.exportRequest = er;
	}

	private void validateExportRequest(ExportRequest er) {
		// TODO: implement validation
		// throw IllegalArgumentException if validation fails.
	}

	// public Future<ExportResult> export() {
	// String cmd = binaryPath + " " + getCommandLineKeys();
	//
	// RuntimeProcess rp = new RuntimeProcess(cmd);
	// rp.execute();
	// }

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
		sb.append(keys.query + createQuery(exportRequest));
		sb.append(keys.jsonArray);
		sb.append(keys.out + getOutputPath());

		LOGGER.info("MongoExport command line: {}", sb.toString());

		sb.append(keys.username + "$MONGO_USER");
		sb.append(keys.password + "$MONGO_PASSWORD");

		return sb.toString();
	}

	private String createQuery(ExportRequest er) {
		StringBuilder sb = new StringBuilder();

		sb.append("{lastUpdatedTs : {$gte : ISODate('");
		sb.append(er.getLastTimeStamp()); // TODO: convert to proper format
		sb.append("')},");
		sb.append(" isRDSEligible : $eq : true}");

		return sb.toString();
	}

	private String getOutputPath() {
		return "C:\\Programs\\out.txt"; /// TODO: generate unique identifiable
										/// output path and file name
	}

	private static boolean isLinux() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0 ? false : true;
	}

	// public static void main(String[] args) {
	// MongoExport me = new MongoExport();
	//
	// me.setHostname("maas-gt-d1-u0031").setPort(37017).withDb("entities-dev").withCollection("entities_rds").export();
	// }

}
