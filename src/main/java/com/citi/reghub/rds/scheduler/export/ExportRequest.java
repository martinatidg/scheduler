package com.citi.reghub.rds.scheduler.export;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * @author Michael Rootman	
 *
 */
public class ExportRequest {

	private String requestId;
	private String hostname;
	private int port;
	private String database;
	private String collection;

	private Calendar lastTimeStamp;
	private Calendar fromTimeStamp;
	private Calendar toTimeStamp;

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

	public Calendar getLastTimeStamp() {
		return lastTimeStamp;
	}

	public void setLastTimeStamp(Calendar lastTimeStamp) {
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

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Calendar getFromTimeStamp() {
		return fromTimeStamp;
	}

	public void setFromTimeStamp(Calendar fromTimeStamp) {
		this.fromTimeStamp = fromTimeStamp;
	}

	public Calendar getToTimeStamp() {
		return toTimeStamp;
	}

	public void setToTimeStamp(Calendar toTimeStamp) {
		this.toTimeStamp = toTimeStamp;
	}
}
