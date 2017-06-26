package com.citi.reghub.rds.scheduler.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RdsMetadata {
	private String database;
	private String collection;

	public String getDatabase() {
		return database;
	}

	@Autowired
	public void setDatabase(@Value("simulator") String database) {
		this.database = database;
	}

	public String getCollection() {
		return collection;
	}

	@Autowired
	public void setCollection(@Value("entities_rds") String collection) {
		this.collection = collection;
	}
}
