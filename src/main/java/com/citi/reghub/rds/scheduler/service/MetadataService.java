package com.citi.reghub.rds.scheduler.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Martin Tan
 *
 */
@Service
public class MetadataService {
	// key: database name, value: a list of collections in the database.
	private Map<String, List<String>> databases;

	private String database;
	private String collection;

	public Map<String, List<String>> getDatabases() {
		databases = new HashMap<>();

		List<String> collections = new ArrayList<>();
		collections.add("entities_rds");
		collections.add("entity");
		databases.put("simulator", collections);

		collections = new ArrayList<>();
		collections.add("employee");
		collections.add("blog");
		collections.add("entity");
		databases.put("foobar", collections);

		return databases;
	}

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
