package com.citi.reghub.rds.scheduler.export;

import java.time.LocalDateTime;

/**
 * @author Michael Rootman	
 *
 */
public class ExportRequest {

	private String hostname;
	private int port;
	private String database;
	private String collection;
	private LocalDateTime lastTimeStamp;

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public LocalDateTime getLastTimeStamp() {
		return lastTimeStamp;
	}

	public void setLastTimeStamp(LocalDateTime lastTimeStamp) {
		this.lastTimeStamp = lastTimeStamp;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
