package com.citi.reghub.rds.scheduler.export;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.citi.reghub.rds.scheduler.util.Util;

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
	private int limit;

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

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

	public boolean isInValid() {
		List<String> parameters = new ArrayList<>();
		parameters.add(hostname);
		parameters.add(collection);
		parameters.add(database);
		parameters.add(requestId);
		
		return parameters.contains(null) || parameters.contains("");
	}

	@Override
	public String toString() {
		return "ExportRequest [requestId=" + requestId + ", hostname=" + hostname + ", port=" + port + ", database="
				+ database + ", collection=" + collection + ", lastTimeStamp=" + Util.formatDate(lastTimeStamp) + ", fromTimeStamp="
				+ Util.formatDate(fromTimeStamp) + ", toTimeStamp=" + Util.formatDate(toTimeStamp) + "]";
	}
}
