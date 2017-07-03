package com.citi.reghub.rds.scheduler.service;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.citi.reghub.rds.scheduler.RdsSchedulerConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(RdsSchedulerConfiguration.class)
public class TestMetadataService {
	@Autowired
	MetadataService meta;

	@Test
	public void testGetDatabases() {
		Map<String, List<String>> dbs = meta.getDatabases();
		Assert.assertNotNull(dbs);
	}
	
	@Test
	public void testGetDatabase() {
		String db = meta.getDatabase();
		Assert.assertNotNull("Database name is null.", db);
		Assert.assertNotEquals("Database name is empty.", db, "");
	}
	
	@Test
	public void testGetCollection() {
		String col = meta.getDatabase();
		Assert.assertNotNull("Collection name is null.", col);
		Assert.assertNotEquals("Collection name is empty.", col, "");
	}
}
