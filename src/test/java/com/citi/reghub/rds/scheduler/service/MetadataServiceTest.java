package com.citi.reghub.rds.scheduler.service;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MetadataServiceTest {
	MetadataService metadataService;

	@Before
	public void init() {
		metadataService = new MetadataService();
	}

	@Test
	public void testGetDatabases() {
		Map<String, List<String>> dbs = metadataService.getDatabases();
		Assert.assertNotNull(dbs);
	}
}
